package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioTienda
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelTienda(
    private val repositorioTienda: RepositorioTienda = RepositorioTienda()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPantallaTienda())
    val estado: StateFlow<EstadoPantallaTienda> = _estado.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val productos = repositorioTienda.listarProductos()
                _estado.update { it.copy(listaProductos = productos, error = null) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    // Panel del funcionario
    fun cargarVentas() {
        viewModelScope.launch {
            try {
                val ventas = repositorioTienda.listarVentas()
                _estado.update { it.copy(listaVentas = ventas) }
            } catch (e: Exception) {
                _estado.update { it.copy(listaVentas = emptyList()) }
            }
        }
    }

    // No permite elegir más unidades que el stock disponible
    fun sumarCantidad(idProducto: Long) {
        _estado.update {
            val stock = it.listaProductos.find { p -> p.id == idProducto }?.stock ?: 0
            val actual = it.cantidadDe(idProducto)
            if (actual < stock) it.copy(cantidades = it.cantidades + (idProducto to actual + 1)) else it
        }
    }

    fun restarCantidad(idProducto: Long) {
        _estado.update {
            val actual = it.cantidadDe(idProducto)
            if (actual > 1) it.copy(cantidades = it.cantidades + (idProducto to actual - 1)) else it
        }
    }

    fun realizarCompraDirecta(producto: Producto) {
        val cantidad = _estado.value.cantidadDe(producto.id)

        viewModelScope.launch {
            try {
                repositorioTienda.registrarVenta(producto.id, cantidad)
                val productos = repositorioTienda.listarProductos()
                _estado.update {
                    it.copy(
                        listaProductos = productos,
                        cantidades = it.cantidades - producto.id, // vuelve a 1
                        compraExitosa = true,
                        error = null
                    )
                }
                delay(2000)
                _estado.update { it.copy(compraExitosa = false) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }
}
