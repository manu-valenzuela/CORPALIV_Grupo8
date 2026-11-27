package cl.duoc.corpaliv.data.model

data class Producto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val urlImagen: String?
)
