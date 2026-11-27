package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.Venta
import cl.duoc.corpaliv.data.remote.dto.ProductoRequestDto
import cl.duoc.corpaliv.data.remote.dto.VentaRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface VentasApiService {

    @GET("api/productos")
    suspend fun listarProductos(): List<Producto>

    @POST("api/productos")
    suspend fun crearProducto(@Body datos: ProductoRequestDto): Producto

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Long, @Body datos: ProductoRequestDto): Producto

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long)

    @GET("api/ventas")
    suspend fun listarVentas(): List<Venta>

    @POST("api/ventas")
    suspend fun registrarVenta(@Body datos: VentaRequestDto): Venta
}
