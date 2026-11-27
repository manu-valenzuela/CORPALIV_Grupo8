package cl.duoc.corpaliv.data.model

data class Noticia(
    val id: Long,
    val titulo: String,
    val resumen: String,
    val urlImagen: String?,
    val fecha: String
)
