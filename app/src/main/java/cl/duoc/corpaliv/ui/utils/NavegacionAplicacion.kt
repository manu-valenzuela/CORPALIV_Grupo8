package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.corpaliv.data.model.RolUsuario
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
    viewModelIndicadores: ViewModelIndicadores = viewModel(),
    viewModelSesion: ViewModelSesion = viewModel()
) {
    val entradaNavegacionActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = entradaNavegacionActual?.destination?.route ?: RutaAplicacion.SeleccionRol.ruta

    // El rol sale de la sesión iniciada (null = invitado)
    val sesion by viewModelSesion.sesion.collectAsState()
    val esTrabajador = sesion?.rol == RolUsuario.Funcionario
    val indicadores by viewModelIndicadores.estado.collectAsState()

    AndamiajeAplicacion(
        claseAnchoVentana = claseAnchoVentana,
        rutaActual = rutaActual,
        esTrabajador = esTrabajador,
        sesionIniciada = sesion != null,
        alSeleccionarRuta = { nuevaRuta ->
            if (nuevaRuta == RutaAplicacion.SeleccionRol.ruta) {
                // Volver al inicio cierra la sesión
                viewModelSesion.cerrarSesion()
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
                    alIniciarSesion = {
                        controladorNavegacion.navigate(RutaAplicacion.Login.ruta)
                    },
                    alContinuarComoInvitado = {
                        controladorNavegacion.navigate(RutaAplicacion.PortalSocio.ruta)
                    }
                )
            }

            composable(RutaAplicacion.Login.ruta) {
                val estadoLogin by viewModelSesion.estado.collectAsState()

                PantallaLogin(
                    claseAnchoVentana = claseAnchoVentana,
                    estado = estadoLogin,
                    onCorreoChange = viewModelSesion::onCorreoChange,
                    onContrasenaChange = viewModelSesion::onContrasenaChange,
                    onIniciarSesion = {
                        viewModelSesion.iniciarSesion { rol ->
                            val destino = if (rol == RolUsuario.Funcionario) RutaAplicacion.GestionUsuarios.ruta
                            else RutaAplicacion.PortalSocio.ruta
                            controladorNavegacion.navigate(destino) {
                                popUpTo(RutaAplicacion.SeleccionRol.ruta)
                            }
                        }
                    },
                    onCrearCuenta = {
                        controladorNavegacion.navigate(RutaAplicacion.RegistroSocio.ruta)
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
                        onConfirmarCompra = { producto ->
                            // Comprar pide iniciar sesión
                            if (sesion == null) {
                                controladorNavegacion.navigate(RutaAplicacion.Login.ruta)
                            } else {
                                viewModelTienda.realizarCompraDirecta(producto)
                            }
                        },
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
                LaunchedEffect(Unit) { viewModelUsuario.cargarUsuarios() }
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
                LaunchedEffect(Unit) {
                    viewModelInicio.cargarDonaciones()
                    viewModelUsuario.cargarUsuarios()
                }
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
