package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.SesionActual
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Instancias de Retrofit para los microservicios propios (uno por puerto)
object RetrofitCorpaliv {

    // 10.0.2.2 es el computador visto desde el emulador
    private const val HOST = "http://10.0.2.2"

    // Agrega el token a cada petición si hay una sesión iniciada
    private val cliente = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val token = SesionActual.sesion.value?.token
            val peticion = if (token != null) {
                cadena.request().newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                cadena.request()
            }
            cadena.proceed(peticion)
        }
        .build()

    private fun crear(puerto: Int): Retrofit =
        Retrofit.Builder()
            .baseUrl("$HOST:$puerto/")
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val donaciones: DonacionesApiService by lazy { crear(8083).create(DonacionesApiService::class.java) }
    val noticias: NoticiasApiService by lazy { crear(8084).create(NoticiasApiService::class.java) }
    val ventas: VentasApiService by lazy { crear(8085).create(VentasApiService::class.java) }
}
