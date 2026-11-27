package cl.duoc.corpaliv.data.remote.dto

// El resto de los datos de la venta los completa el microservicio
data class VentaRequestDto(
    val productoId: Long,
    val cantidad: Int
)
