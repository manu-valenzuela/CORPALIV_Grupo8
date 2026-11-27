package cl.duoc.corpaliv.data.model

data class Venta(
    val id: String,
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val fecha: String
) {
    val precioTotal: Double
        get() = precioUnitario * cantidad
}
