package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.corpaliv.ui.screens.*
import cl.duoc.corpaliv.viewmodel.*

@Composable
fun NavegacionAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    controladorNavegacion: NavHostController,
    viewModelInicio: ViewModelInicio = viewModel(),
    viewModelTienda: ViewModelTienda = viewModel(),
    viewModelUsuario: ViewModelUsuario = viewModel()
) {
    val entradaNavegacionActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = entradaNavegacionActual?.destination?.route ?: RutaAplicacion.SeleccionRol.ruta

    var esTrabajador by remember { mutableStateOf(false) }

    AndamiajeAplicacion(
        claseAnchoVentana = claseAnchoVentana,
        rutaActual = rutaActual,
        esTrabajador = esTrabajador,
        alSeleccionarRuta = { nuevaRuta ->
            if (nuevaRuta == RutaAplicacion.SeleccionRol.ruta) {
                controladorNavegacion.navigate(RutaAplicacion.SeleccionRol.ruta) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                controladorNavegacion.navigate(nuevaRuta) {
                    popUpTo(RutaAplicacion.SeleccionRol.ruta) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    ) {
        NavHost(
            navController = controladorNavegacion,
            startDestination = RutaAplicacion.SeleccionRol.ruta
        ) {
            composable(RutaAplicacion.SeleccionRol.ruta) {
                PantallaSeleccionRol(
                    claseAnchoVentana = claseAnchoVentana,
                    alSeleccionarSocioInvitado = {
                        esTrabajador = false
                        controladorNavegacion.navigate(RutaAplicacion.PortalSocio.ruta)
                    },
                    alSeleccionarFuncionario = {
                        esTrabajador = true
                        controladorNavegacion.navigate(RutaAplicacion.GestionUsuarios.ruta)
                    }
                )
            }

            composable(RutaAplicacion.PortalSocio.ruta) {
                val estadoInicio by viewModelInicio.estado.collectAsState()
                val formState by viewModelInicio.formState.collectAsState()
                val mostrarMensaje by viewModelInicio.mostrarMensaje.collectAsState()

                PantallaInicio(
                    claseAnchoVentana = claseAnchoVentana,
                    cargando = estadoInicio.cargando,
                    listaNoticias = estadoInicio.listaNoticias,
                    proximoEvento = estadoInicio.proximoEvento,
                    formState = formState,
                    mostrarMensajeAnimado = mostrarMensaje,
                    mensajePersistido = estadoInicio.mensajePersistido,
                    onNombreChange = viewModelInicio::onNombreChange,
                    onCorreoChange = viewModelInicio::onCorreoChange,
                    onMontoChange = viewModelInicio::onMontoChange,
                    onConfirmarDonacion = {
                        viewModelInicio.realizarDonacion {
                            controladorNavegacion.navigate("resumen")
                        }
                    }
                )
            }

            composable("resumen") {
                ResumenDonacionScreen(
                    viewModel = viewModelInicio,
                    alVolver = { controladorNavegacion.popBackStack() }
                )
            }

            composable(RutaAplicacion.Tienda.ruta) {
                val estadoTienda by viewModelTienda.estado.collectAsState()

                if (esTrabajador) {
                    PantallaVentas(
                        claseAnchoVentana = claseAnchoVentana,
                        listaVentas = estadoTienda.listaVentas
                    )
                } else {
                    PantallaTienda(
                        claseAnchoVentana = claseAnchoVentana,
                        listaProductos = estadoTienda.listaProductos,
                        mensajeConfirmacion = estadoTienda.mensajeConfirmacion,
                        onConfirmarCompra = viewModelTienda::realizarCompraDirecta,
                        onLimpiarMensaje = viewModelTienda::limpiarMensaje
                    )
                }
            }

            composable(RutaAplicacion.RegistroSocio.ruta) {
                PantallaRegistroSocio(
                    viewModelUsuario = viewModelUsuario,
                    onRegistroExitoso = {
                        val destino = if (esTrabajador) RutaAplicacion.GestionUsuarios.ruta else RutaAplicacion.PortalSocio.ruta
                        controladorNavegacion.navigate(destino) {
                            popUpTo(destino) { inclusive = true }
                        }
                    }
                )
            }

            composable(RutaAplicacion.GestionUsuarios.ruta) {
                val listaUsuarios by viewModelUsuario.listaUsuarios.collectAsState()

                PantallaGestionUsuarios(
                    claseAnchoVentana = claseAnchoVentana,
                    listaUsuarios = listaUsuarios,
                    onCambiarRol = viewModelUsuario::cambiarRolUsuario
                )
            }
        }
    }
}