package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.DonacionExpress
import cl.duoc.corpaliv.data.remote.DonacionesApiService
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv
import cl.duoc.corpaliv.data.remote.dto.DonacionRequestDto

class RepositorioDonaciones(
    private val api: DonacionesApiService = RetrofitCorpaliv.donaciones
) {
    suspend fun registrar(nombre: String, correo: String, monto: Int): DonacionExpress =
        api.registrar(DonacionRequestDto(nombre, correo, monto))

    suspend fun listar(): List<DonacionExpress> = api.listar()

    suspend fun eliminar(id: Long) = api.eliminar(id)
}
