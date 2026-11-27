package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.corpaliv.ui.components.AndamiajeAplicacion
import cl.duoc.corpaliv.ui.screens.*
import cl.duoc.corpaliv.viewmodel.*

@Composable
fun NavegacionAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    controladorNavegacion: NavHostController,
    viewModelInicio: ViewModelInicio = viewModel(),
    viewModelTienda: ViewModelTienda = viewModel(),
    viewModelUsuario: ViewModelUsuario = viewModel(),
    viewModelIndicadores: ViewModelIndicadores = viewModel()
) {
    val entradaNavegacionActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = entradaNavegacionActual?.destination?.route ?: RutaAplicacion.SeleccionRol.ruta

    var esTrabajador by remember { mutableStateOf(false) }
    val indicadores by viewModelIndicadores.estado.collectAsState()

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

                PantallaInicio(
                    claseAnchoVentana = claseAnchoVentana,
                    cargando = estadoInicio.cargando,
                    listaNoticias = estadoInicio.listaNoticias,
                    formState = formState,
                    errorNoticias = estadoInicio.errorNoticias,
                    onNombreChange = viewModelInicio::onNombreChange,
                    onCorreoChange = viewModelInicio::onCorreoChange,
                    onMontoChange = viewModelInicio::onMontoChange,
                    onConfirmarDonacion = {
                        viewModelInicio.realizarDonacion {
                            controladorNavegacion.navigate(RutaAplicacion.ResumenDonacion.ruta)
                        }
                    },
                    indicadores = indicadores,
                    onCambiarConversion = viewModelIndicadores::cambiarMostrarConversion
                )
            }

            composable(RutaAplicacion.ResumenDonacion.ruta) {
                val formState by viewModelInicio.formState.collectAsState()

                PantallaResumenDonacion(
                    claseAnchoVentana = claseAnchoVentana,
                    formState = formState,
                    alVolver = { controladorNavegacion.popBackStack() }
                )
            }

            composable(RutaAplicacion.Tienda.ruta) {
                val estadoTienda by viewModelTienda.estado.collectAsState()

                if (esTrabajador) {
                    LaunchedEffect(Unit) { viewModelTienda.cargarVentas() }

                    PantallaVentas(
                        claseAnchoVentana = claseAnchoVentana,
                        listaVentas = estadoTienda.listaVentas,
                        indicadores = indicadores,
                        onCambiarConversion = viewModelIndicadores::cambiarMostrarConversion
                    )
                } else {
                    PantallaTienda(
                        claseAnchoVentana = claseAnchoVentana,
                        estado = estadoTienda,
                        onSumarCantidad = viewModelTienda::sumarCantidad,
                        onRestarCantidad = viewModelTienda::restarCantidad,
                        onConfirmarCompra = viewModelTienda::realizarCompraDirecta,
                        indicadores = indicadores,
                        onCambiarConversion = viewModelIndicadores::cambiarMostrarConversion
                    )
                }
            }

            composable(RutaAplicacion.RegistroSocio.ruta) {
                val estadoRegistro by viewModelUsuario.formState.collectAsState()

                PantallaRegistroSocio(
                    estado = estadoRegistro,
                    onNombreChange = viewModelUsuario::onNombreChange,
                    onApellidoChange = viewModelUsuario::onApellidoChange,
                    onRutChange = viewModelUsuario::onRutChange,
                    onFechaNacimientoChange = viewModelUsuario::onFechaNacimientoChange,
                    onDireccionChange = { viewModelUsuario.onDireccionChange(it) },
                    onDonacionChange = viewModelUsuario::onDonacionChange,
                    onMontoPersonalizadoChange = viewModelUsuario::onMontoPersonalizadoChange,
                    onCorreoChange = viewModelUsuario::onCorreoChange,
                    onContrasenaChange = viewModelUsuario::onContrasenaChange,
                    onAlternarContrasena = viewModelUsuario::onAlternarContrasena,
                    onObtenerUbicacion = viewModelUsuario::obtenerUbicacion,
                    onPermisoUbicacionDenegado = viewModelUsuario::onPermisoUbicacionDenegado,
                    onRegistrar = {
                        viewModelUsuario.registrarSocio {
                            val destino = if (esTrabajador) RutaAplicacion.GestionUsuarios.ruta else RutaAplicacion.PortalSocio.ruta
                            controladorNavegacion.navigate(destino) {
                                popUpTo(destino) { inclusive = true }
                            }
                        }
                    },
                    indicadores = indicadores,
                    onCambiarConversion = viewModelIndicadores::cambiarMostrarConversion
                )
            }

            composable(RutaAplicacion.GestionUsuarios.ruta) {
                val listaUsuarios by viewModelUsuario.listaUsuarios.collectAsState()

                PantallaGestionUsuarios(
                    claseAnchoVentana = claseAnchoVentana,
                    listaUsuarios = listaUsuarios,
                    onCambiarRol = viewModelUsuario::cambiarRolUsuario,
                    onCrearUsuario = {
                        controladorNavegacion.navigate(RutaAplicacion.RegistroSocio.ruta)
                    }
                )
            }

            composable(RutaAplicacion.GestionDonaciones.ruta) {
                LaunchedEffect(Unit) { viewModelInicio.cargarDonaciones() }
                val listaDonaciones by viewModelInicio.listaDonaciones.collectAsState()
                val listaUsuarios by viewModelUsuario.listaUsuarios.collectAsState()

                PantallaGestionDonaciones(
                    claseAnchoVentana = claseAnchoVentana,
                    listaDonaciones = listaDonaciones,
                    listaUsuarios = listaUsuarios,
                    indicadores = indicadores,
                    onCambiarConversion = viewModelIndicadores::cambiarMostrarConversion,
                    onEliminarDonacion = viewModelInicio::eliminarDonacion
                )
            }
        }
    }
}
