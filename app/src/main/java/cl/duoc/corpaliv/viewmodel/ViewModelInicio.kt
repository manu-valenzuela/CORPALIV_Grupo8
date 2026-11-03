package cl.duoc.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.DonacionErrores
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.DonacionUiState
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia
import cl.duoc.corpaliv.repository.DonacionDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private val _listaDonaciones = MutableStateFlow<List<DonacionExpress>>(emptyList())
    val listaDonaciones: StateFlow<List<DonacionExpress>> = _listaDonaciones

    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        cargarDatosInicio()
        observarDonacionesDataStore()
    }

    fun cargarDatosInicio() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }
            delay(1000)

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
        viewModelScope.launch {
            donacionDataStore.obtenerDonaciones().collect { donaciones ->
                _listaDonaciones.value = donaciones
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
        if (validarFormulario()) {
            viewModelScope.launch {
                val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val idDonacion = "D-${System.currentTimeMillis()}"
                val montoDouble = _formState.value.monto.toDoubleOrNull() ?: 0.0

                val nuevaDonacion = DonacionExpress(
                    id = idDonacion,
                    nombre = _formState.value.nombre,
                    correo = _formState.value.correo,
                    monto = montoDouble,
                    fecha = fechaActual
                )

                donacionDataStore.guardarDonacion(nuevaDonacion)
                _mostrarMensaje.value = true
                delay(1500)
                _mostrarMensaje.value = false
                onExito()
            }
        }
    }
}