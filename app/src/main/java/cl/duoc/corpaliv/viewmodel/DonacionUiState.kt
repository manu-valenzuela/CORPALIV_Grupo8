package cl.duoc.corpaliv.viewmodel

// Modelo principal que representa el estado del formulario (Guía 11)
data class DonacionUiState(
    val nombre: String = "",
    val correo: String = "",
    val monto: String = "",
    val errores: DonacionErrores = DonacionErrores(),
    val donacionExitosa: Boolean = false
)

