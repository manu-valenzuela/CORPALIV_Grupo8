package cl.duoc.corpaliv.data.remote.dto

data class ProductoRequestDto(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val urlImagen: String?
)
