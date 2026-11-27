package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.RespuestaIndicadores
import cl.duoc.corpaliv.data.remote.IndicadoresApiService
import cl.duoc.corpaliv.data.remote.RetrofitIndicadores

class RepositorioIndicadores(
    private val api: IndicadoresApiService = RetrofitIndicadores.api
) {
    suspend fun obtenerIndicadores(): RespuestaIndicadores = api.obtenerIndicadores()
}
