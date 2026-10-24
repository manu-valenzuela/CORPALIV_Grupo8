package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EstadoPantallaInicio(
    val cargando: Boolean = false,
    val listaNoticias: List<Noticia> = emptyList(),
    val proximoEvento: Evento? = null,
    val error: String? = null
)

class ViewModelInicio : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPantallaInicio())
    val estado: StateFlow<EstadoPantallaInicio> = _estado.asStateFlow()

    init {
        cargarDatosInicio()
    }

    fun cargarDatosInicio() {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true)

            // Simulación de carga de datos iniciales
            val noticiasEstaticas = listOf(
                Noticia(
                    id = "1",
                    titulo = "Nueva campaña de recaudo 2026",
                    resumen = "Acompáñanos en la gran colecta anual de CORPALIV.",
                    urlImagen = "",
                    fecha = "2026-10-15"
                ),
                Noticia(
                    id = "2",
                    titulo = "Talleres para la comunidad",
                    resumen = "Inscripciones abiertas para talleres de integración.",
                    urlImagen = "",
                    fecha = "2026-10-10"
                )
            )

            val eventoEstatico = Evento(
                id = "101",
                titulo = "Gala Benefactores CORPALIV",
                fecha = "2026-11-20",
                ubicacion = "Sede Central",
                descripcion = "Encuentro anual con colaboradores estratégicos."
            )

            _estado.value = EstadoPantallaInicio(
                cargando = false,
                listaNoticias = noticiasEstaticas,
                proximoEvento = eventoEstatico
            )
        }
    }

    fun procesarDonacionExpres(donacion: DonacionExpress) {
        viewModelScope.launch {
            // Lógica de procesamiento de pago/donación
        }
    }
}