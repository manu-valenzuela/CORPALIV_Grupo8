package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EstadoPantallaTienda(
    val cargando: Boolean = false,
    val listaProductos: List<Producto> = emptyList(),
    val error: String? = null
)

class ViewModelTienda : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPantallaTienda())
    val estado: StateFlow<EstadoPantallaTienda> = _estado.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true)

            val productosEstaticos = listOf(
                Producto(
                    id = "p1",
                    nombre = "Agenda Institucional 2026",
                    descripcion = "Agenda encuadernada con ilustraciones creadas por los estudiantes.",
                    precio = 12500.0,
                    urlImagen = ""
                ),
                Producto(
                    id = "p2",
                    nombre = "Tazón Solidario CORPALIV",
                    descripcion = "Tazón cerámico de 350 ml con diseño exclusivo.",
                    precio = 6500.0,
                    urlImagen = ""
                ),
                Producto(
                    id = "p3",
                    nombre = "Bolsa Ecológica de Tela",
                    descripcion = "Bolsa de algodón orgánico reutilizable.",
                    precio = 4500.0,
                    urlImagen = ""
                )
            )

            _estado.value = EstadoPantallaTienda(
                cargando = false,
                listaProductos = productosEstaticos
            )
        }
    }
}