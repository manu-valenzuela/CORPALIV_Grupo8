package cl.duoc.corpaliv.data.model

// Datos de quien inició sesión (el token lo entrega el microservicio auth)
data class Sesion(
    val token: String,
    val correo: String,
    val rol: RolUsuario
)
