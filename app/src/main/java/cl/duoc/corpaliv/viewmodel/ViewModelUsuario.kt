package cl.duoc.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.OpcionDonacionMensual
import cl.duoc.corpaliv.model.RegistroUsuarioErrores
import cl.duoc.corpaliv.model.RegistroUsuarioUiState
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario
import cl.duoc.corpaliv.repository.UsuarioDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ViewModelUsuario(application: Application) : AndroidViewModel(application) {

    private val usuarioDataStore = UsuarioDataStore(application)

    private val _formState = MutableStateFlow(RegistroUsuarioUiState())
    val formState: StateFlow<RegistroUsuarioUiState> = _formState.asStateFlow()

    private val _listaUsuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val listaUsuarios: StateFlow<List<Usuario>> = _listaUsuarios.asStateFlow()

    init {
        observarUsuarios()
    }

    private fun observarUsuarios() {
        viewModelScope.launch {
            usuarioDataStore.obtenerUsuarios().collect { usuarios ->
                _listaUsuarios.value = usuarios
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
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onDonacionChange(donacion: OpcionDonacionMensual) {
        _formState.update { it.copy(donacionMensual = donacion, errores = it.errores.copy(donacionMensual = null)) }
    }

    fun onCorreoChange(valor: String) {
        _formState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onContrasenaChange(valor: String) {
        _formState.update { it.copy(contrasena = valor, errores = it.errores.copy(contrasena = null)) }
    }

    fun onRolChange(nuevoRol: RolUsuario) {
        _formState.update { it.copy(rol = nuevoRol) }
    }

    fun setCargandoUbicacion(cargando: Boolean) {
        _formState.update { it.copy(cargandoUbicacion = cargando) }
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
            fechaNacimiento = if (estado.fechaNacimiento.isBlank()) "Selecciona una fecha" else null,
            direccion = if (estado.direccion.isBlank()) "La dirección es obligatoria" else null,
            donacionMensual = if (!montoValido) "El monto mínimo es $1.000" else null,
            correo = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(estado.correo).matches()) "Correo no válido" else null,
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

    fun registrarSocio(onExito: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                val estado = _formState.value
                val nuevoUsuario = Usuario(
                    id = UUID.randomUUID().toString(),
                    nombre = estado.nombre,
                    apellido = estado.apellido,
                    rut = estado.rut,
                    fechaNacimiento = estado.fechaNacimiento,
                    direccion = estado.direccion,
                    latitud = estado.latitud,
                    longitud = estado.longitud,
                    donacionMensual = estado.donacionMensual,
                    correo = estado.correo,
                    rol = estado.rol
                )
                usuarioDataStore.guardarUsuario(nuevoUsuario)
                _formState.value = RegistroUsuarioUiState()
                onExito()
            }
        }
    }

    fun cambiarRolUsuario(idUsuario: String, nuevoRol: RolUsuario) {
        viewModelScope.launch {
            usuarioDataStore.actualizarRolUsuario(idUsuario, nuevoRol)
        }
    }
}