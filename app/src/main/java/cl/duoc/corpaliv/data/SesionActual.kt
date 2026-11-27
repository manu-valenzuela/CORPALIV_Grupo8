package cl.duoc.corpaliv.data

import cl.duoc.corpaliv.data.model.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sesión en memoria: la usan los ViewModels y Retrofit (para enviar el token)
object SesionActual {

    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()

    fun iniciar(sesion: Sesion) {
        _sesion.value = sesion
    }

    fun cerrar() {
        _sesion.value = null
    }
}
