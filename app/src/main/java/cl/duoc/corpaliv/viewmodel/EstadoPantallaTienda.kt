package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta

data class EstadoPantallaTienda(
    val listaProductos: List<Producto> = emptyList(),
    val listaVentas: List<Venta> = emptyList(),
    val compraExitosa: Boolean = false
)