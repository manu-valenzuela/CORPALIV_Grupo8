package cl.duoc.corpaliv.data.remote.dto

data class NoticiaRequestDto(
    val titulo: String,
    val resumen: String,
    val urlImagen: String?
)
