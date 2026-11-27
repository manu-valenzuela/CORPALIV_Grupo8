package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.data.remote.dto.NoticiaRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoticiasApiService {

    @GET("api/noticias")
    suspend fun listar(): List<Noticia>

    @POST("api/noticias")
    suspend fun crear(@Body datos: NoticiaRequestDto): Noticia

    @PUT("api/noticias/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body datos: NoticiaRequestDto): Noticia

    @DELETE("api/noticias/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
