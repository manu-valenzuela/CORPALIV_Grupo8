package cl.duoc.corpaliv.data.model

sealed interface RolUsuario {
    val etiqueta: String

    data object Socio : RolUsuario {
        override val etiqueta: String = "Socio"
    }

    data object Voluntario : RolUsuario {
        override val etiqueta: String = "Voluntario"
    }

    data object Funcionario : RolUsuario {
        override val etiqueta: String = "Funcionario"
    }
}
