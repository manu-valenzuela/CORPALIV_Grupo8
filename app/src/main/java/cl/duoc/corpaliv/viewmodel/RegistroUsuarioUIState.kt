package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.data.model.RolUsuario

data class RegistroUsuarioUiState(
    val nombre: String = "",
    val apellido: String = "",
    val rut: String = "",
    val fechaNacimiento: String = "",
    val direccion: String = "",
    val latitud: Double? = null,
    val longitud: Double? = null,
    val donacionMensual: OpcionDonacionMensual = OpcionDonacionMensual.CincoMil,
    val correo: String = "",
    val contrasena: String = "",
    val rol: RolUsuario = RolUsuario.Socio,
    val cargandoUbicacion: Boolean = false,
    val errores: RegistroUsuarioErrores = RegistroUsuarioErrores()
)

data class RegistroUsuarioErrores(
    val nombre: String? = null,
    val apellido: String? = null,
    val rut: String? = null,
    val fechaNacimiento: String? = null,
    val direccion: String? = null,
    val donacionMensual: String? = null,
    val correo: String? = null,
    val contrasena: String? = null
)