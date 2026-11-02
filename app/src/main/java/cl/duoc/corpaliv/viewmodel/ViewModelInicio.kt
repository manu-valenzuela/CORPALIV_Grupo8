package cl.duoc.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.DonacionErrores
import cl.duoc.corpaliv.model.DonacionUiState
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia
import cl.duoc.corpaliv.repository.DonacionDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoPantallaInicio(
    val cargando: Boolean = true,
    val listaNoticias: List<Noticia> = emptyList(),
    val proximoEvento: Evento? = null,
    val mensajePersistido: String = ""
)

class ViewModelInicio(application: Application) : AndroidViewModel(application) {

    private val donacionDataStore = DonacionDataStore(application)

    private val _estado = MutableStateFlow(EstadoPantallaInicio())
    val estado: StateFlow<EstadoPantallaInicio> = _estado

    private val _formState = MutableStateFlow(DonacionUiState())
    val formState: StateFlow<DonacionUiState> = _formState

    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        cargarDatosInicio()
        observarDonacionesDataStore()
    }

    fun cargarDatosInicio() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }

            delay(1200)

            val noticias = listOf(
                Noticia("1", "Proyecto Impulso Laboral Alba Lab", "Formación laboral y acompañamiento para personas con discapacidad múltiple.", "", "2026-10-15"),
                Noticia("2", "Escuela Jan Van Dijk amplía matrícula", "Acreditación ministerial permite aumentar la cobertura educativa.", "", "2026-10-10")
            )

            val evento = Evento("101", "Encuentro Anual de Benefactores y RSE", "2026-11-20", "Sede Central CORPALIV", "Jornada de integración con empresas y familias.")

            _estado.update {
                it.copy(
                    cargando = false,
                    listaNoticias = noticias,
                    proximoEvento = evento
                )
            }
        }
    }

    private fun observarDonacionesDataStore() {
        viewModelScope.launch {
            donacionDataStore.obtenerUltimaDonacion().collect { registro ->
                _estado.update { it.copy(mensajePersistido = registro) }
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
        val errores = DonacionErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            monto = if (estadoActual.monto.isBlank() || (estadoActual.monto.toDoubleOrNull() ?: 0.0) <= 0) "Monto no válido" else null
        )

        val hayErrores = listOfNotNull(errores.nombre, errores.correo, errores.monto).isNotEmpty()
        _formState.update { it.copy(errores = errores) }
        return !hayErrores
    }

    fun realizarDonacion(onExito: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                donacionDataStore.guardarDonacion(_formState.value.nombre, _formState.value.monto)
                _mostrarMensaje.value = true
                delay(2000)
                _mostrarMensaje.value = false
                onExito()
            }
        }
    }
}