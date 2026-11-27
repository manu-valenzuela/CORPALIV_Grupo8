package cl.duoc.corpaliv.data.remote.dto

data class DonacionRequestDto(
    val nombre: String,
    val correo: String,
    val monto: Int
)
