package cl.duoc.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.ManejadorUbicacion
import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.viewmodel.RegistroUsuarioErrores
import cl.duoc.corpaliv.viewmodel.RegistroUsuarioUiState
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Usuario
import cl.duoc.corpaliv.data.remote.dto.UsuarioDto
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioAuth
import cl.duoc.corpaliv.repository.RepositorioUsuarios
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelUsuario @JvmOverloads constructor(
    application: Application,
    private val repositorioUsuarios: RepositorioUsuarios = RepositorioUsuarios(),
    private val repositorioAuth: RepositorioAuth = RepositorioAuth(),
    private val manejadorUbicacion: ManejadorUbicacion = ManejadorUbicacion(application)
) : AndroidViewModel(application) {

    private val _formState = MutableStateFlow(RegistroUsuarioUiState())
    val formState: StateFlow<RegistroUsuarioUiState> = _formState.asStateFlow()

    private val _listaUsuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val listaUsuarios: StateFlow<List<Usuario>> = _listaUsuarios.asStateFlow()

    // Panel del funcionario: perfiles (usuarios) con su rol (auth)
    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                _listaUsuarios.value = repositorioUsuarios.listar()
            } catch (e: Exception) {
                _listaUsuarios.value = emptyList()
            }
        }
    }

    // --- Actualizadores del estado del formulario ---

    fun onNombreChange(valor: String) {
        _formState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onApellidoChange(valor: String) {
        _formState.update { it.copy(apellido = valor, errores = it.errores.copy(apellido = null)) }
    }

    fun onRutChange(valor: String) {
        _formState.update { it.copy(rut = valor, errores = it.errores.copy(rut = null)) }
    }

    fun onFechaNacimientoChange(valor: String) {
        _formState.update { it.copy(fechaNacimiento = valor, errores = it.errores.copy(fechaNacimiento = null)) }
    }

    fun onDireccionChange(direccion: String, lat: Double? = null, lng: Double? = null) {
        _formState.update {
            it.copy(
                direccion = direccion,
                latitud = lat,
                longitud = lng,
                errorUbicacion = null,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onDonacionChange(donacion: OpcionDonacionMensual) {
        _formState.update { it.copy(donacionMensual = donacion, errores = it.errores.copy(donacionMensual = null)) }
    }

    fun onMontoPersonalizadoChange(texto: String) {
        val monto = texto.filter { it.isDigit() }.toIntOrNull() ?: 0
        onDonacionChange(OpcionDonacionMensual.Personalizado(monto))
    }

    fun onCorreoChange(valor: String) {
        _formState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onContrasenaChange(valor: String) {
        _formState.update { it.copy(contrasena = valor, errores = it.errores.copy(contrasena = null)) }
    }

    fun onAlternarContrasena() {
        _formState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    // --- Ubicación (GPS) ---

    fun obtenerUbicacion() {
        _formState.update { it.copy(cargandoUbicacion = true, errorUbicacion = null) }
        manejadorUbicacion.obtenerUbicacionActual(
            onResultado = { direccion, lat, lng ->
                _formState.update {
                    it.copy(
                        direccion = direccion,
                        latitud = lat,
                        longitud = lng,
                        cargandoUbicacion = false,
                        errores = it.errores.copy(direccion = null)
                    )
                }
            },
            onError = { mensaje ->
                _formState.update { it.copy(cargandoUbicacion = false, errorUbicacion = mensaje) }
            }
        )
    }

    fun onPermisoUbicacionDenegado() {
        _formState.update { it.copy(errorUbicacion = "Permiso de ubicación denegado") }
    }

    // --- Validación de datos ---

    fun validarFormulario(): Boolean {
        val estado = _formState.value

        val montoValido = when (val donacion = estado.donacionMensual) {
            is OpcionDonacionMensual.Personalizado -> donacion.montoLibre >= 1000
            else -> donacion.monto > 0
        }

        val errores = RegistroUsuarioErrores(
            nombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null,
            apellido = if (estado.apellido.isBlank()) "El apellido es obligatorio" else null,
            rut = if (!validarRutChileno(estado.rut)) "RUT inválido" else null,
            fechaNacimiento = if (!REGEX_FECHA.matches(estado.fechaNacimiento.trim())) "Usa el formato DD/MM/AAAA" else null,
            direccion = if (estado.direccion.isBlank()) "La dirección es obligatoria" else null,
            donacionMensual = if (!montoValido) "El monto mínimo es $1.000" else null,
            correo = if (!REGEX_CORREO.matches(estado.correo.trim())) "Correo no válido" else null,
            contrasena = if (estado.contrasena.length < 6) "Mínimo 6 caracteres" else null
        )

        _formState.update { it.copy(errores = errores) }

        return listOfNotNull(
            errores.nombre, errores.apellido, errores.rut,
            errores.fechaNacimiento, errores.direccion, errores.donacionMensual,
            errores.correo, errores.contrasena
        ).isEmpty()
    }

    private fun validarRutChileno(rut: String): Boolean {
        val limpio = rut.replace(".", "").replace("-", "").uppercase()
        if (limpio.length < 8) return false
        val cuerpo = limpio.dropLast(1)
        val dv = limpio.last()

        var suma = 0
        var multiplicador = 2
        for (i in cuerpo.reversed()) {
            val digito = Character.getNumericValue(i)
            if (digito < 0) return false
            suma += digito * multiplicador
            multiplicador = if (multiplicador < 7) multiplicador + 1 else 2
        }
        val dvEsperado = when (val res = 11 - (suma % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> res.toString()[0]
        }
        return dv == dvEsperado
    }

    // --- Persistencia y lógica de negocio ---

    // Crea la cuenta en auth y el perfil en usuarios.
    // Quien se registra queda con su sesión iniciada; un funcionario que crea un usuario conserva la suya.
    fun registrarSocio(onExito: () -> Unit) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val estado = _formState.value
            try {
                val esFuncionario = repositorioAuth.sesionActual.value?.rol == RolUsuario.Funcionario
                val nuevaSesion = repositorioAuth.registrar(estado.correo.trim(), estado.contrasena)
                if (!esFuncionario) repositorioAuth.guardarSesion(nuevaSesion)

                repositorioUsuarios.crear(
                    UsuarioDto(
                        nombre = estado.nombre,
                        apellido = estado.apellido,
                        rut = estado.rut,
                        fechaNacimiento = fechaParaServidor(estado.fechaNacimiento.trim()),
                        direccion = estado.direccion,
                        latitud = estado.latitud,
                        longitud = estado.longitud,
                        montoDonacionMensual = estado.donacionMensual.monto,
                        correo = estado.correo.trim()
                    )
                )

                _formState.update { it.copy(registroExitoso = true, errorEnvio = null) }
                delay(2000)
                _formState.value = RegistroUsuarioUiState()
                onExito()
            } catch (e: Exception) {
                _formState.update { it.copy(errorEnvio = mensajeDeError(e)) }
            }
        }
    }

    // El rol se cambia en auth; el usuario se identifica por su correo
    fun cambiarRolUsuario(correo: String, nuevoRol: RolUsuario) {
        viewModelScope.launch {
            try {
                repositorioUsuarios.cambiarRol(correo, nuevoRol)
                _listaUsuarios.value = repositorioUsuarios.listar()
            } catch (e: Exception) {
                // Si falla, la lista queda como estaba
            }
        }
    }

    // "DD/MM/AAAA" (formulario) -> "AAAA-MM-DD" (microservicio)
    private fun fechaParaServidor(fecha: String): String {
        val (dia, mes, anio) = fecha.split("/")
        return "$anio-$mes-$dia"
    }

    private companion object {
        val REGEX_CORREO = Regex("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")
        val REGEX_FECHA = Regex("^\\d{2}/\\d{2}/\\d{4}$")
    }
}
