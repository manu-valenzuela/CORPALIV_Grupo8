package cl.duoc.corpaliv.model

enum class MetodoPago {
    TARJETA_CREDITO,
    TARJETA_DEBITO,
    TRANSFERENCIA_BANCARIA
}

data class DonacionExpress(
    val monto: Double,
    val metodoPago: MetodoPago
)