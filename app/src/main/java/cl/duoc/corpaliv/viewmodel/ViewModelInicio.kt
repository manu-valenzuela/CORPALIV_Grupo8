package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.DonacionExpress
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioDonaciones
import cl.duoc.corpaliv.repository.RepositorioNoticias
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelInicio(
    private val repositorioNoticias: RepositorioNoticias = RepositorioNoticias(),
    private val repositorioDonaciones: RepositorioDonaciones = RepositorioDonaciones()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPantallaInicio())
    val estado: StateFlow<EstadoPantallaInicio> = _estado

    private val _formState = MutableStateFlow(DonacionUiState())
    val formState: StateFlow<DonacionUiState> = _formState

    private val _listaDonaciones = MutableStateFlow<List<DonacionExpress>>(emptyList())
    val listaDonaciones: StateFlow<List<DonacionExpress>> = _listaDonaciones

    init {
        cargarDatosInicio()
    }

    fun cargarDatosInicio() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, errorNoticias = null) }
            try {
                val noticias = repositorioNoticias.obtenerNoticias()
                _estado.update { it.copy(cargando = false, listaNoticias = noticias) }
            } catch (e: Exception) {
                _estado.update { it.copy(cargando = false, errorNoticias = mensajeDeError(e)) }
            }
        }
    }

    // Panel del funcionario
    fun cargarDonaciones() {
        viewModelScope.launch {
            try {
                _listaDonaciones.value = repositorioDonaciones.listar()
            } catch (e: Exception) {
                _listaDonaciones.value = emptyList()
            }
        }
    }

    fun eliminarDonacion(id: Long) {
        viewModelScope.launch {
            try {
                repositorioDonaciones.eliminar(id)
                _listaDonaciones.value = repositorioDonaciones.listar()
            } catch (e: Exception) {
                // Si falla, la lista queda como estaba
            }
        }
    }

    fun onNombreChange(valor: String) {
        _formState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _formState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onMontoChange(valor: String) {
        _formState.update { it.copy(monto = valor, errores = it.errores.copy(monto = null)) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _formState.value
        val montoValor = estadoActual.monto.toDoubleOrNull() ?: 0.0

        val errorMonto = when {
            estadoActual.monto.isBlank() -> "Campo obligatorio"
            montoValor <= 0 -> "El monto debe ser mayor a $0"
            montoValor > 1_000_000 -> "El monto máximo permitido es $1.000.000"
            else -> null
        }

        val errores = DonacionErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            monto = errorMonto
        )

        val hayErrores = listOfNotNull(errores.nombre, errores.correo, errores.monto).isNotEmpty()
        _formState.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun realizarDonacion(onExito: () -> Unit) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val form = _formState.value
            try {
                repositorioDonaciones.registrar(form.nombre, form.correo, form.monto.toDouble().toInt())
                _formState.update { it.copy(donacionExitosa = true, errorEnvio = null) }
                delay(2000)
                _formState.update { it.copy(donacionExitosa = false) }
                onExito()
            } catch (e: Exception) {
                _formState.update { it.copy(errorEnvio = mensajeDeError(e)) }
            }
        }
    }
}
