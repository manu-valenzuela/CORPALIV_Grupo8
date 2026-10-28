package cl.duoc.corpaliv.ui.utils

sealed class RutaAplicacion(val ruta: String) {
    object Inicio : RutaAplicacion("inicio")
    object Tienda : RutaAplicacion("tienda")
    //object Escuela : RutaAplicacion("escuela")
    //object Empresas : RutaAplicacion("empresas")
    //object IniciarSesion : RutaAplicacion("iniciar_sesion")
    //object Intranet : RutaAplicacion("intranet")
    //object IntranetPersonal : RutaAplicacion("intranet/personal")
}