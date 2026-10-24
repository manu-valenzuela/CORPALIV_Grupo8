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
import cl.duoc.corpaliv.ui.utils.screens.AndamiajeAplicacion
import cl.duoc.corpaliv.ui.utils.screens.PantallaInicio
import cl.duoc.corpaliv.ui.utils.screens.PantallaTienda
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
            composable(RutaAplicacion.Inicio.ruta) {
                val estadoInicio by viewModelInicio.estado.collectAsState()

                PantallaInicio(
                    claseAnchoVentana = claseAnchoVentana,
                    listaNoticias = estadoInicio.listaNoticias,
                    proximoEvento = estadoInicio.proximoEvento,
                    alRealizarDonacion = { donacion ->
                        viewModelInicio.procesarDonacionExpres(donacion)
                    }
                )
            }

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