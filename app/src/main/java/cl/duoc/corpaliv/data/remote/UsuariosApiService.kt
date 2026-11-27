package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuariosApiService {

    @GET("api/usuarios")
    suspend fun listar(): List<UsuarioDto>

    @POST("api/usuarios")
    suspend fun crear(@Body datos: UsuarioDto): UsuarioDto
}
