package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta
import cl.duoc.corpaliv.repository.RepositorioTienda
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun sumarCantidad(idProducto: String) {
        _estado.update {
            it.copy(cantidades = it.cantidades + (idProducto to it.cantidadDe(idProducto) + 1))
        }
    }

    fun restarCantidad(idProducto: String) {
        _estado.update {
            val actual = it.cantidadDe(idProducto)
            if (actual > 1) it.copy(cantidades = it.cantidades + (idProducto to actual - 1)) else it
        }
    }

    fun realizarCompraDirecta(producto: Producto) {
        val cantidad = _estado.value.cantidadDe(producto.id)

        viewModelScope.launch {
            repositorioTienda.registrarVenta(producto, cantidad)
            _estado.update {
                it.copy(
                    listaVentas = repositorioTienda.ventas.value,
                    cantidades = it.cantidades - producto.id, // vuelve a 1
                    compraExitosa = true
                )
            }
            delay(2000)
            _estado.update { it.copy(compraExitosa = false) }
        }
    }
}
