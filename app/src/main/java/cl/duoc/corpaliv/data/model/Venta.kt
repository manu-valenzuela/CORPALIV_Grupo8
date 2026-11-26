package cl.duoc.corpaliv.data.model

data class Venta(
    val id: String,
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val precioTotal: Double,
    val fecha: String
)