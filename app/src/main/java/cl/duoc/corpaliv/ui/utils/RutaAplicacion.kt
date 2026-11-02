package cl.duoc.corpaliv.ui.utils

sealed class RutaAplicacion(val ruta: String) {
    data object SeleccionRol : RutaAplicacion("seleccion_rol")
    data object PortalSocio : RutaAplicacion("portal_socio")
    data object Tienda : RutaAplicacion("tienda")
    data object RegistroSocio : RutaAplicacion("registro_socio")
    data object GestionUsuarios : RutaAplicacion("gestion_usuarios")
}