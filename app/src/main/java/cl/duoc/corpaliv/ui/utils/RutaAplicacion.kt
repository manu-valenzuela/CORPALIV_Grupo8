package cl.duoc.corpaliv.ui.utils

sealed class RutaAplicacion(val ruta: String) {
    data object SeleccionRol : RutaAplicacion("seleccion_rol")
    data object Inicio : RutaAplicacion("inicio")
    data object Tienda : RutaAplicacion("tienda")
    data object Intranet : RutaAplicacion("intranet")
}