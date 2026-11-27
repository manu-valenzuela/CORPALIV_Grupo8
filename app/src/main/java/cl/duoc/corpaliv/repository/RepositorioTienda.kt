package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv
import cl.duoc.corpaliv.data.remote.VentasApiService
import cl.duoc.corpaliv.data.remote.dto.ProductoRequestDto
import cl.duoc.corpaliv.data.remote.dto.VentaRequestDto

class RepositorioTienda(
    private val api: VentasApiService = RetrofitCorpaliv.ventas
) {
    suspend fun listarProductos(): List<Producto> = api.listarProductos()

    suspend fun crearProducto(datos: ProductoRequestDto): Producto = api.crearProducto(datos)

    suspend fun actualizarProducto(id: Long, datos: ProductoRequestDto): Producto =
        api.actualizarProducto(id, datos)

    suspend fun eliminarProducto(id: Long) = api.eliminarProducto(id)

    suspend fun listarVentas(): List<Venta> = api.listarVentas()

    suspend fun registrarVenta(productoId: Long, cantidad: Int): Venta =
        api.registrarVenta(VentaRequestDto(productoId, cantidad))
}
