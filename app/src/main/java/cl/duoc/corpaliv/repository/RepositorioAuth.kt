package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.SesionActual
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Sesion
import cl.duoc.corpaliv.data.remote.AuthApiService
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv
import cl.duoc.corpaliv.data.remote.dto.CredencialesDto
import cl.duoc.corpaliv.data.remote.dto.TokenDto
import kotlinx.coroutines.flow.StateFlow

class RepositorioAuth(
    private val api: AuthApiService = RetrofitCorpaliv.auth
) {
    val sesionActual: StateFlow<Sesion?> = SesionActual.sesion

    // Inicia sesión y la deja guardada
    suspend fun iniciarSesion(correo: String, contrasena: String): Sesion {
        val sesion = aSesion(api.iniciarSesion(CredencialesDto(correo, contrasena)))
        SesionActual.iniciar(sesion)
        return sesion
    }

    // Crea la cuenta (siempre como SOCIO); quien llama decide si guarda la sesión
    suspend fun registrar(correo: String, contrasena: String): Sesion =
        aSesion(api.registrar(CredencialesDto(correo, contrasena)))

    fun guardarSesion(sesion: Sesion) = SesionActual.iniciar(sesion)

    fun cerrarSesion() = SesionActual.cerrar()

    private fun aSesion(dto: TokenDto) = Sesion(dto.token, dto.correo, RolUsuario.desdeCodigo(dto.rol))
}
