package cl.duoc.corpaliv.viewmodel

data class EstadoIndicadores(
    val valorDolar: Double? = null,
    val valorUf: Double? = null,
    val mostrarConversion: Boolean = false
) {
    // La conversión solo se puede ofrecer si la API respondió
    val disponible: Boolean
        get() = valorDolar != null && valorUf != null
}
