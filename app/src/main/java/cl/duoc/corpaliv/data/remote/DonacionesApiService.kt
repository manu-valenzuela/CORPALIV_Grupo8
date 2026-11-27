package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.DonacionExpress
import cl.duoc.corpaliv.data.remote.dto.DonacionRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DonacionesApiService {

    @POST("api/donaciones")
    suspend fun registrar(@Body datos: DonacionRequestDto): DonacionExpress

    @GET("api/donaciones")
    suspend fun listar(): List<DonacionExpress>

    @DELETE("api/donaciones/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
