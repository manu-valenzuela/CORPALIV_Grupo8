package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.corpaliv.model.Producto
import cl.duoc.corpaliv.model.Venta
import cl.duoc.corpaliv.repository.RepositorioTienda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EstadoPantallaTienda(
    val listaProductos: List<Producto> = emptyList(),
    val listaVentas: List<Venta> = emptyList(),
    val mensajeConfirmacion: String? = null
)

class ViewModelTienda : ViewModel() {

    private val repositorioTienda = RepositorioTienda()

    private val _estado = MutableStateFlow(EstadoPantallaTienda())
    val estado: StateFlow<EstadoPantallaTienda> = _estado.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        val productos = repositorioTienda.obtenerProductos()
        _estado.update { it.copy(listaProductos = productos) }
    }

    fun realizarCompraDirecta(producto: Producto, cantidad: Int) {
        if (cantidad <= 0) return

        val venta = repositorioTienda.registrarVenta(producto, cantidad)

        _estado.update { estadoActual ->
            estadoActual.copy(
                listaVentas = repositorioTienda.ventas.value,
                mensajeConfirmacion = "¡Compra confirmada! ${venta.cantidad}x ${venta.nombreProducto} ($${venta.precioTotal.toInt()})"
            )
        }
    }

    fun limpiarMensaje() {
        _estado.update { it.copy(mensajeConfirmacion = null) }
    }
}