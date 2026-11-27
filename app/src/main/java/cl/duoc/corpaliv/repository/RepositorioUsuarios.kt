package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Usuario
import cl.duoc.corpaliv.data.remote.AuthApiService
import cl.duoc.corpaliv.data.remote.RetrofitCorpaliv
import cl.duoc.corpaliv.data.remote.UsuariosApiService
import cl.duoc.corpaliv.data.remote.dto.CambioRolDto
import cl.duoc.corpaliv.data.remote.dto.UsuarioDto

// El perfil viene del microservicio usuarios y el rol del microservicio auth (se unen por correo)
class RepositorioUsuarios(
    private val usuariosApi: UsuariosApiService = RetrofitCorpaliv.usuarios,
    private val authApi: AuthApiService = RetrofitCorpaliv.auth
) {
    suspend fun listar(): List<Usuario> {
        val cuentas = authApi.listarCuentas()
        return usuariosApi.listar().map { dto ->
            val codigoRol = cuentas.find { it.correo == dto.correo }?.rol ?: RolUsuario.Socio.codigo
            Usuario(
                id = dto.id ?: 0L,
                nombre = dto.nombre,
                apellido = dto.apellido,
                rut = dto.rut,
                fechaNacimiento = dto.fechaNacimiento,
                direccion = dto.direccion,
                latitud = dto.latitud,
                longitud = dto.longitud,
                donacionMensual = OpcionDonacionMensual.desdeMonto(dto.montoDonacionMensual),
                correo = dto.correo,
                rol = RolUsuario.desdeCodigo(codigoRol)
            )
        }
    }

    suspend fun crear(datos: UsuarioDto): UsuarioDto = usuariosApi.crear(datos)

    suspend fun cambiarRol(correo: String, rol: RolUsuario) {
        val cuenta = authApi.listarCuentas().find { it.correo == correo }
            ?: throw IllegalStateException("El usuario no tiene cuenta")
        authApi.cambiarRol(cuenta.id, CambioRolDto(rol.codigo))
    }
}
