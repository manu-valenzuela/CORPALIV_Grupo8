package cl.duoc.corpaliv.data.model

data class DonacionExpress(
    val id: String,
    val nombre: String,
    val correo: String,
    val monto: Double,
    val fecha: String
)