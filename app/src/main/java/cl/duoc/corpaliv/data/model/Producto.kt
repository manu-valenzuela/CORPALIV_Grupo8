package cl.duoc.corpaliv.data.model

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val urlImagen: String,
    val enStock: Boolean = true
)