package cl.duoc.corpaliv.viewmodel

data class RegistroUsuarioErrores(
    val nombre: String? = null,
    val apellido: String? = null,
    val rut: String? = null,
    val fechaNacimiento: String? = null,
    val direccion: String? = null,
    val donacionMensual: String? = null,
    val correo: String? = null,
    val contrasena: String? = null
)