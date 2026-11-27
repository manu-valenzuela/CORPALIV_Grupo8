package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.data.remote.dto.NoticiaRequestDto
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioNoticias
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelGestionNoticias(
    private val repositorio: RepositorioNoticias = RepositorioNoticias()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoGestionNoticias())
    val estado: StateFlow<EstadoGestionNoticias> = _estado.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            try {
                _estado.update { it.copy(lista = repositorio.obtenerNoticias()) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    fun onTituloChange(valor: String) = _estado.update { it.copy(titulo = valor, error = null) }

    fun onResumenChange(valor: String) = _estado.update { it.copy(resumen = valor, error = null) }

    fun onUrlImagenChange(valor: String) = _estado.update { it.copy(urlImagen = valor, error = null) }

    // Carga la noticia en el formulario para editarla
    fun editar(noticia: Noticia) {
        _estado.update {
            it.copy(
                idEditando = noticia.id,
                titulo = noticia.titulo,
                resumen = noticia.resumen,
                urlImagen = noticia.urlImagen ?: "",
                error = null
            )
        }
    }

    fun cancelarEdicion() {
        _estado.update { EstadoGestionNoticias(lista = it.lista) }
    }

    fun guardar() {
        val datos = _estado.value
        if (datos.titulo.isBlank() || datos.resumen.isBlank()) {
            _estado.update { it.copy(error = "Completa el título y el resumen") }
            return
        }

        viewModelScope.launch {
            try {
                val noticia = NoticiaRequestDto(
                    titulo = datos.titulo.trim(),
                    resumen = datos.resumen.trim(),
                    urlImagen = datos.urlImagen.trim().ifBlank { null }
                )
                val id = datos.idEditando
                if (id == null) repositorio.crear(noticia) else repositorio.actualizar(id, noticia)

                _estado.value = EstadoGestionNoticias(lista = repositorio.obtenerNoticias(), guardadoExitoso = true)
                delay(2000)
                _estado.update { it.copy(guardadoExitoso = false) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            try {
                repositorio.eliminar(id)
                _estado.update { it.copy(lista = repositorio.obtenerNoticias()) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }
}
