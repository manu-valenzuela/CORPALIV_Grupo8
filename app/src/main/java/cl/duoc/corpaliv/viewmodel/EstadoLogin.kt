package cl.duoc.corpaliv.viewmodel

data class EstadoLogin(
    val correo: String = "",
    val contrasena: String = "",
    val error: String? = null,
    val loginExitoso: Boolean = false
)
