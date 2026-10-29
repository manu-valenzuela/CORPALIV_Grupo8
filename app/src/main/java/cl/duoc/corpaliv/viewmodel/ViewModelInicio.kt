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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadoPantallaInicio(
    val cargando: Boolean = true, // Estado para mostrar el loader inicial (Guía 12)[cite: 74, 75]
    val listaNoticias: List<Noticia> = emptyList(),
    val proximoEvento: Evento? = null,
    val mensajePersistido: String = ""
)

class ViewModelInicio(application: Application) : AndroidViewModel(application) {

    private val donacionDataStore = DonacionDataStore(application)

    // Estado principal observable de la pantalla[cite: 74, 82]
    private val _estado = MutableStateFlow(EstadoPantallaInicio())
    val estado: StateFlow<EstadoPantallaInicio> = _estado

    // Estado interno del formulario (Guía 11)[cite: 82]
    private val _formState = MutableStateFlow(DonacionUiState())
    val formState: StateFlow<DonacionUiState> = _formState

    // Estado para mostrar u ocultar el mensaje animado (Guía 12)[cite: 74]
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        cargarDatosInicio()
    }

    fun cargarDatosInicio() {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true)

            // Simula demora para mostrar Loader (Guía 12)[cite: 74]
            delay(1200)

            // Contenido contextualizado de CORPALIV y Alba Lab[cite: 8]
            val noticias = listOf(
                Noticia("1", "Proyecto Impulso Laboral Alba Lab", "Formación laboral y acompañamiento para personas con discapacidad múltiple.", "", "2026-10-15"),
                Noticia("2", "Escuela Jan Van Dijk amplía matrícula", "Acreditación ministerial permite aumentar la cobertura educativa.", "", "2026-10-10")
            )

            val evento = Evento("101", "Encuentro Anual de Benefactores y RSE", "2026-11-20", "Sede Central CORPALIV", "Jornada de integración con empresas y familias.")

            // Cargar estado previo desde DataStore (Guía 12)[cite: 74]
            val guardado = donacionDataStore.obtenerUltimaDonacion().first()

            _estado.value = EstadoPantallaInicio(
                cargando = false,
                listaNoticias = noticias,
                proximoEvento = evento,
                mensajePersistido = guardado
            )
        }
    }

    // Funciones de modificación de campos (Guía 11)[cite: 82]
    fun onNombreChange(valor: String) {
        _formState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _formState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onMontoChange(valor: String) {
        _formState.update { it.copy(monto = valor, errores = it.errores.copy(monto = null)) }
    }

    // Validación global del formulario (Guía 11)[cite: 83]
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

    // Guardado persistente y animación de éxito (Guías 11 y 12)[cite: 74, 83]
    fun realizarDonacion(onExito: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                donacionDataStore.guardarDonacion(_formState.value.nombre, _formState.value.monto)
                _mostrarMensaje.value = true
                delay(2000) // Ocultar mensaje animado tras 2 segundos (Guía 12)[cite: 74]
                _mostrarMensaje.value = false
                onExito()
            }
        }
    }
}