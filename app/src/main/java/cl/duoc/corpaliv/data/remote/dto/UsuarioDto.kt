package cl.duoc.corpaliv.data.remote.dto

// Perfil tal como lo envía y recibe el microservicio usuarios
// (fechaNacimiento en formato "AAAA-MM-DD"; el id es null al crear)
data class UsuarioDto(
    val id: Long? = null,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val fechaNacimiento: String,
    val direccion: String,
    val latitud: Double?,
    val longitud: Double?,
    val montoDonacionMensual: Int,
    val correo: String
)
