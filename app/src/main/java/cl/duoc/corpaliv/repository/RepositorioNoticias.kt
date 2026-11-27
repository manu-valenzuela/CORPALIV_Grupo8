package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.data.remote.NoticiasApiService
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv

class RepositorioNoticias(
    private val api: NoticiasApiService = RetrofitCorpaliv.noticias
) {
    suspend fun obtenerNoticias(): List<Noticia> = api.listar()
}
