package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Sesion
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelSesion(
    private val repositorioAuth: RepositorioAuth = RepositorioAuth()
) : ViewModel() {

    // null = invitado
    val sesion: StateFlow<Sesion?> = repositorioAuth.sesionActual

    private val _estado = MutableStateFlow(EstadoLogin())
    val estado: StateFlow<EstadoLogin> = _estado.asStateFlow()

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, error = null) }
    }

    fun onContrasenaChange(valor: String) {
        _estado.update { it.copy(contrasena = valor, error = null) }
    }

    fun iniciarSesion(onExito: (RolUsuario) -> Unit) {
        val datos = _estado.value
        if (datos.correo.isBlank() || datos.contrasena.isBlank()) {
            _estado.update { it.copy(error = "Ingresa tu correo y contraseña") }
            return
        }

        viewModelScope.launch {
            try {
                val sesion = repositorioAuth.iniciarSesion(datos.correo.trim(), datos.contrasena)
                _estado.update { it.copy(loginExitoso = true, error = null) }
                delay(1500)
                _estado.value = EstadoLogin()
                onExito(sesion.rol)
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    fun cerrarSesion() {
        repositorioAuth.cerrarSesion()
    }
}
