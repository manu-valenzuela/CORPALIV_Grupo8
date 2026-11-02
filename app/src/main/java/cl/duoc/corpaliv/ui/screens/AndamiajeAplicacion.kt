package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
    ElementoNavegacion("Hazte Socio", RutaAplicacion.RegistroSocio.ruta, Icons.Default.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndamiajeAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    if (rutaActual == RutaAplicacion.SeleccionRol.ruta) {
        Box(modifier = Modifier.fillMaxSize()) {
            contenido()
        }
        return
    }

    val tituloPantalla = when (rutaActual) {
        RutaAplicacion.Inicio.ruta -> "CORPALIV - Inicio"
        RutaAplicacion.Tienda.ruta -> "Tienda Solidaria"
        RutaAplicacion.RegistroSocio.ruta -> "Registro de Socio"
        RutaAplicacion.GestionUsuarios.ruta -> "Gestión de Usuarios"
        "resumen" -> "Resumen de Aporte"
        else -> "CORPALIV"
    }

    val topBarComposable = @Composable {
        TopAppBar(
            title = { Text(text = tituloPantalla) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
    }

    when (claseAnchoVentana) {
        WindowWidthSizeClass.Compact -> {
            Scaffold(
                topBar = topBarComposable,
                bottomBar = {
                    BarraNavegacionInferior(
                        rutaActual = rutaActual,
                        alSeleccionarRuta = alSeleccionarRuta
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    contenido()
                }
            }
        }
        WindowWidthSizeClass.Medium -> {
            Scaffold(
                topBar = topBarComposable
            ) { padding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    RielNavegacionLateral(
                        rutaActual = rutaActual,
                        alSeleccionarRuta = alSeleccionarRuta
                    )
                    Box(modifier = Modifier.fillMaxSize()) {
                        contenido()
                    }
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
                Scaffold(
                    topBar = topBarComposable
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        contenido()
                    }
                }
            }
        }
        else -> {
            Scaffold(
                topBar = topBarComposable,
                bottomBar = {
                    BarraNavegacionInferior(
                        rutaActual = rutaActual,
                        alSeleccionarRuta = alSeleccionarRuta
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
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