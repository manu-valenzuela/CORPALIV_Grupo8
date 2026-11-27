package cl.duoc.corpaliv.data.model
data class Usuario(
    val id: String = "",
    val nombre: String,
    val apellido: String,
    val rut: String,
    val fechaNacimiento: String,
    val direccion: String,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val donacionMensual: OpcionDonacionMensual,
    val correo: String,
    val rol: RolUsuario = RolUsuario.Socio
)