package cl.duoc.corpaliv.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Instancia propia para la API externa, separada de la de los microservicios
object RetrofitIndicadores {

    private const val URL_BASE = "https://mindicador.cl/"

    val api: IndicadoresApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IndicadoresApiService::class.java)
    }
}
