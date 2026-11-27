package cl.duoc.corpaliv.data.remote.dto

// Cuerpo de error común de los microservicios: {"estado": 404, "mensaje": "..."}
data class ErrorDto(
    val estado: Int,
    val mensaje: String
)
