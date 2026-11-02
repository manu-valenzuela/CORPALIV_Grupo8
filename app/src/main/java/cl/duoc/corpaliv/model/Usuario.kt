package cl.duoc.corpaliv.model

enum class RolUsuario(val etiqueta: String) {
    SOCIO("Socio"),
    VOLUNTARIO("Voluntario"),
    FUNCIONARIO("Funcionario")
}

data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val rol: RolUsuario = RolUsuario.SOCIO
)

data class RegistroUsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val errores: RegistroUsuarioErrores = RegistroUsuarioErrores()
)

data class RegistroUsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null
)