package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.model.Noticia
import retrofit2.http.GET

interface NoticiasApiService {

    @GET("api/noticias")
    suspend fun listar(): List<Noticia>
}
