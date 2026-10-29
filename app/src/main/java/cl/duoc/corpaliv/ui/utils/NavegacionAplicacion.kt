package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import cl.duoc.corpaliv.ui.screens.AndamiajeAplicacion
import cl.duoc.corpaliv.ui.screens.PantallaInicio
import cl.duoc.corpaliv.ui.screens.PantallaTienda
import cl.duoc.corpaliv.ui.screens.ResumenDonacionScreen
import cl.duoc.corpaliv.viewmodel.ViewModelInicio
import cl.duoc.corpaliv.viewmodel.ViewModelTienda

@Composable
fun NavegacionAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    controladorNavegacion: NavHostController,
    viewModelInicio: ViewModelInicio = viewModel(),
    viewModelTienda: ViewModelTienda = viewModel()
) {
    val entradaNavegacionActual by controladorNavegacion.currentBackStackEntryAsState()
    val rutaActual = entradaNavegacionActual?.destination?.route ?: RutaAplicacion.Inicio.ruta

    AndamiajeAplicacion(
        claseAnchoVentana = claseAnchoVentana,
        rutaActual = rutaActual,
        alSeleccionarRuta = { nuevaRuta ->
            controladorNavegacion.navigate(nuevaRuta) {
                popUpTo(RutaAplicacion.Inicio.ruta) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
        NavHost(
            navController = controladorNavegacion,
            startDestination = RutaAplicacion.Inicio.ruta
        ) {
            // Pantalla Principal de Inicio
            composable(RutaAplicacion.Inicio.ruta) {
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
                            controladorNavegacion.navigate("resumen")[cite: 86]
                        }
                    }
                )
            }

            // Pantalla de Resumen de Donación (Guía 11)[cite: 87, 88]
            composable("resumen") {
                ResumenDonacionScreen(
                    viewModel = viewModelInicio,
                    alVolver = { controladorNavegacion.popBackStack() }
                )
            }

            // Pantalla de Tienda Solidaria
            composable(RutaAplicacion.Tienda.ruta) {
                val estadoTienda by viewModelTienda.estado.collectAsState()

                PantallaTienda(
                    claseAnchoVentana = claseAnchoVentana,
                    listaProductos = estadoTienda.listaProductos,
                    alAgregarAlCarrito = { producto ->
                        // Acción al presionar comprar
                    }
                )
            }
        }
    }
}