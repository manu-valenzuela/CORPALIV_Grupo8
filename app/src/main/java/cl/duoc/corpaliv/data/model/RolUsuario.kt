package cl.duoc.corpaliv.data.model

sealed interface RolUsuario {
    val etiqueta: String

    data object Socio : RolUsuario {
        override val etiqueta: String = "Socio"
    }

    data class Voluntario(
        val areaAsignada: String = "General"
    ) : RolUsuario {
        override val etiqueta: String = "Voluntario"
    }

    data class Funcionario(
        val cargo: String = "Personal Administrativo"
    ) : RolUsuario {
        override val etiqueta: String = "Funcionario"
    }
}