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

sealed class OpcionDonacionMensual(
    val monto: Int,
    val etiqueta: String,
    val impacto: String
) {
    data object CincoMil : OpcionDonacionMensual(
        monto = 5000,
        etiqueta = "$5.000",
        impacto = "Financia 1 libro en sistema Braille para un niño"
    )

    data object QuinceMil : OpcionDonacionMensual(
        monto = 15000,
        etiqueta = "$15.000",
        impacto = "Cubre kit de materiales adaptados para talleres"
    )

    data object VeinticincoMil : OpcionDonacionMensual(
        monto = 25000,
        etiqueta = "$25.000",
        impacto = "Financia 1 sesión de tratamiento psicológico especializado"
    )

    data class Personalizado(val montoLibre: Int) : OpcionDonacionMensual(
        monto = montoLibre,
        etiqueta = "$$montoLibre",
        impacto = "Aporte voluntario directo a los programas de CORPALIV"
    )
}

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