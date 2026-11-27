package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta
import cl.duoc.corpaliv.data.remote.dto.VentaRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VentasApiService {

    @GET("api/productos")
    suspend fun listarProductos(): List<Producto>

    @GET("api/ventas")
    suspend fun listarVentas(): List<Venta>

    @POST("api/ventas")
    suspend fun registrarVenta(@Body datos: VentaRequestDto): Venta
}
