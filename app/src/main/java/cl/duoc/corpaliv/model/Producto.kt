package cl.duoc.corpaliv.model

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val urlImagen: String,
    val enStock: Boolean = true
)