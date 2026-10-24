package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cl.duoc.corpaliv.ui.utils.RutaAplicacion

data class ElementoNavegacion(
    val titulo: String,
    val ruta: String,
    val icono: ImageVector
)

val elementosNavegacion = listOf(
    ElementoNavegacion("Inicio", RutaAplicacion.Inicio.ruta, Icons.Default.Home),
    ElementoNavegacion("Tienda", RutaAplicacion.Tienda.ruta, Icons.Default.ShoppingBag),
    //ElementoNavegacion("Escuela", RutaAplicacion.Escuela.ruta, Icons.Default.School),
    //ElementoNavegacion("Empresas", RutaAplicacion.Empresas.ruta, Icons.Default.Business)
)

@Composable
fun AndamiajeAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    when (claseAnchoVentana) {
        WindowWidthSizeClass.Compact -> {
            Scaffold(
                bottomBar = {
                    BarraNavegacionInferior(
                        rutaActual = rutaActual,
                        alSeleccionarRuta = alSeleccionarRuta
                    )
                }
            ) { padding ->
                Row(modifier = Modifier.padding(padding)) {
                    contenido()
                }
            }
        }
        WindowWidthSizeClass.Medium -> {
            Scaffold { padding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    RielNavegacionLateral(
                        rutaActual = rutaActual,
                        alSeleccionarRuta = alSeleccionarRuta
                    )
                    contenido()
                }
            }
        }
        WindowWidthSizeClass.Expanded -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet {
                        elementosNavegacion.forEach { elemento ->
                            NavigationDrawerItem(
                                label = { Text(elemento.titulo) },
                                selected = rutaActual == elemento.ruta,
                                onClick = { alSeleccionarRuta(elemento.ruta) },
                                icon = { Icon(elemento.icono, contentDescription = elemento.titulo) }
                            )
                        }
                    }
                }
            ) {
                contenido()
            }
        }
        else -> {
            Scaffold { padding ->
                Row(modifier = Modifier.padding(padding)) {
                    contenido()
                }
            }
        }
    }
}

@Composable
private fun BarraNavegacionInferior(
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit
) {
    NavigationBar {
        elementosNavegacion.forEach { elemento ->
            NavigationBarItem(
                selected = rutaActual == elemento.ruta,
                onClick = { alSeleccionarRuta(elemento.ruta) },
                icon = { Icon(elemento.icono, contentDescription = elemento.titulo) },
                label = { Text(elemento.titulo) }
            )
        }
    }
}

@Composable
private fun RielNavegacionLateral(
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit
) {
    NavigationRail {
        elementosNavegacion.forEach { elemento ->
            NavigationRailItem(
                selected = rutaActual == elemento.ruta,
                onClick = { alSeleccionarRuta(elemento.ruta) },
                icon = { Icon(elemento.icono, contentDescription = elemento.titulo) },
                label = { Text(elemento.titulo) }
            )
        }
    }
}