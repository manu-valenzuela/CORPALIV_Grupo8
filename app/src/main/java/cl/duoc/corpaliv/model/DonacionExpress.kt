package cl.duoc.corpaliv.model

data class DonacionExpress(
    val id: String,
    val nombre: String,
    val correo: String,
    val monto: Double,
    val fecha: String
)