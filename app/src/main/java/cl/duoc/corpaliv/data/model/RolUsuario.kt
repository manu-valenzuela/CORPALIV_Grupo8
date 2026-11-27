package cl.duoc.corpaliv.data.model

sealed interface RolUsuario {
    val etiqueta: String
    val codigo: String // como lo envía y recibe el microservicio auth

    data object Socio : RolUsuario {
        override val etiqueta: String = "Socio"
        override val codigo: String = "SOCIO"
    }

    data object Funcionario : RolUsuario {
        override val etiqueta: String = "Funcionario"
        override val codigo: String = "FUNCIONARIO"
    }

    companion object {
        fun desdeCodigo(codigo: String): RolUsuario =
            if (codigo == Funcionario.codigo) Funcionario else Socio
    }
}
