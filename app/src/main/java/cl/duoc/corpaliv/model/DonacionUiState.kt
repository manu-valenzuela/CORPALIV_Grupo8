package cl.duoc.corpaliv.model

// Modelo principal que representa el estado del formulario (Guía 11)[cite: 81]
data class DonacionUiState(
    val nombre: String = "",
    val correo: String = "",
    val monto: String = "",
    val errores: DonacionErrores = DonacionErrores(),
    val donacionExitosa: Boolean = false
)

// Modelo que almacena posibles errores individuales del formulario (Guía 11)[cite: 81]
data class DonacionErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val monto: String? = null
)