package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.RespuestaIndicadores
import retrofit2.http.GET

interface IndicadoresApiService {

    @GET("api")
    suspend fun obtenerIndicadores(): RespuestaIndicadores
}
