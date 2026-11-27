package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta

data class EstadoPantallaTienda(
    val listaProductos: List<Producto> = emptyList(),
    val listaVentas: List<Venta> = emptyList(),
    val cantidades: Map<Long, Int> = emptyMap(),
    val compraExitosa: Boolean = false,
    val error: String? = null
) {
    // Cantidad elegida para un producto (1 si aún no se ha cambiado)
    fun cantidadDe(idProducto: Long): Int = cantidades[idProducto] ?: 1
}
