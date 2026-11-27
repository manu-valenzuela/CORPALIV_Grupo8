package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.remote.dto.CambioRolDto
import cl.duoc.corpaliv.data.remote.dto.CredencialesDto
import cl.duoc.corpaliv.data.remote.dto.CuentaDto
import cl.duoc.corpaliv.data.remote.dto.TokenDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun iniciarSesion(@Body datos: CredencialesDto): TokenDto

    @POST("api/auth/registro")
    suspend fun registrar(@Body datos: CredencialesDto): TokenDto

    @GET("api/auth/cuentas")
    suspend fun listarCuentas(): List<CuentaDto>

    @PATCH("api/auth/cuentas/{id}/rol")
    suspend fun cambiarRol(@Path("id") id: Long, @Body datos: CambioRolDto): CuentaDto
}
