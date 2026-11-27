package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.data.remote.NoticiasApiService
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv
import cl.duoc.corpaliv.data.remote.dto.NoticiaRequestDto

class RepositorioNoticias(
    private val api: NoticiasApiService = RetrofitCorpaliv.noticias
) {
    suspend fun obtenerNoticias(): List<Noticia> = api.listar()

    suspend fun crear(datos: NoticiaRequestDto): Noticia = api.crear(datos)

    suspend fun actualizar(id: Long, datos: NoticiaRequestDto): Noticia = api.actualizar(id, datos)

    suspend fun eliminar(id: Long) = api.eliminar(id)
}
