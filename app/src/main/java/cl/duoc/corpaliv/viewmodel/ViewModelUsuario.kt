package cl.duoc.corpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.model.RegistroUsuarioErrores
import cl.duoc.corpaliv.model.RegistroUsuarioUiState
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario
import cl.duoc.corpaliv.repository.UsuarioDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ViewModelUsuario(application: Application) : AndroidViewModel(application) {

    private val usuarioDataStore = UsuarioDataStore(application)

    private val _formState = MutableStateFlow(RegistroUsuarioUiState())
    val formState: StateFlow<RegistroUsuarioUiState> = _formState

    private val _listaUsuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val listaUsuarios: StateFlow<List<Usuario>> = _listaUsuarios

    init {
        observarUsuarios()
    }

    private fun observarUsuarios() {
        viewModelScope.launch {
            usuarioDataStore.obtenerUsuarios().collect { usuarios ->
                _listaUsuarios.value = usuarios
            }
        }
    }

    fun onNombreChange(valor: String) {
        _formState.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _formState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun validarFormulario(): Boolean {
        val estado = _formState.value
        val errores = RegistroUsuarioErrores(
            nombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (!estado.correo.contains("@")) "Ingresa un correo válido" else null
        )
        _formState.update { it.copy(errores = errores) }
        return errores.nombre == null && errores.correo == null
    }

    fun registrarSocio(onExito: () -> Unit) {
        if (validarFormulario()) {
            viewModelScope.launch {
                val nuevoSocio = Usuario(
                    id = UUID.randomUUID().toString(),
                    nombre = _formState.value.nombre,
                    correo = _formState.value.correo,
                    rol = RolUsuario.SOCIO
                )
                usuarioDataStore.guardarUsuario(nuevoSocio)
                _formState.value = RegistroUsuarioUiState()
                onExito()
            }
        }
    }

    fun cambiarRolUsuario(idUsuario: String, nuevoRol: RolUsuario) {
        viewModelScope.launch {
            usuarioDataStore.actualizarRolUsuario(idUsuario, nuevoRol)
        }
    }
}