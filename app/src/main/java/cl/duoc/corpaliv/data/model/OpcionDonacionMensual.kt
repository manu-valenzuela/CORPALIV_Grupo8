package cl.duoc.corpaliv.data.model

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