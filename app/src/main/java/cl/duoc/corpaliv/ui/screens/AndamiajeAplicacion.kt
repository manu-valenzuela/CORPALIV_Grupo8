package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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

// Pestañas para Socio / Invitado
val elementosSocio = listOf(
    ElementoNavegacion("Inicio", RutaAplicacion.SeleccionRol.ruta, Icons.Default.Home),
    ElementoNavegacion("Portal", RutaAplicacion.PortalSocio.ruta, Icons.Default.Info),
    ElementoNavegacion("Tienda", RutaAplicacion.Tienda.ruta, Icons.Default.ShoppingBag),
    ElementoNavegacion("Registrarse", RutaAplicacion.RegistroSocio.ruta, Icons.Default.Person)
)

// Pestañas para Trabajador / Funcionario
val elementosTrabajador = listOf(
    ElementoNavegacion("Inicio", RutaAplicacion.SeleccionRol.ruta, Icons.Default.Home),
    ElementoNavegacion("Usuarios", RutaAplicacion.GestionUsuarios.ruta, Icons.Default.AccountCircle),
    ElementoNavegacion("Ventas", RutaAplicacion.Tienda.ruta, Icons.Default.ShoppingBag),
    ElementoNavegacion("Crear Usuario", RutaAplicacion.RegistroSocio.ruta, Icons.Default.PersonAdd)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndamiajeAplicacion(
    claseAnchoVentana: WindowWidthSizeClass,
    rutaActual: String,
    esTrabajador: Boolean = false,
    alSeleccionarRuta: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    // Si la pantalla actual es la de Selección de Rol, se muestra limpia (Sin TopBar ni Barra Inferior)
    if (rutaActual == RutaAplicacion.SeleccionRol.ruta) {
        Box(modifier = Modifier.fillMaxSize()) {
            contenido()
        }
        return
    }

    val elementosNavegacion = if (esTrabajador) elementosTrabajador else elementosSocio

    val tituloPantalla = when (rutaActual) {
        RutaAplicacion.PortalSocio.ruta -> "Portal Informativo Socio"
        RutaAplicacion.Tienda.ruta -> if (esTrabajador) "Gestión de Ventas" else "Tienda Solidaria"
        RutaAplicacion.RegistroSocio.ruta -> if (esTrabajador) "Crear Nuevo Usuario" else "Registro de Socio"
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
                        elementos = elementosNavegacion,
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
                        elementos = elementosNavegacion,
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
                        elementos = elementosNavegacion,
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
    elementos: List<ElementoNavegacion>,
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit
) {
    NavigationBar {
        elementos.forEach { elemento ->
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
    elementos: List<ElementoNavegacion>,
    rutaActual: String,
    alSeleccionarRuta: (String) -> Unit
) {
    NavigationRail {
        elementos.forEach { elemento ->
            NavigationRailItem(
                selected = rutaActual == elemento.ruta,
                onClick = { alSeleccionarRuta(elemento.ruta) },
                icon = { Icon(elemento.icono, contentDescription = elemento.titulo) },
                label = { Text(elemento.titulo) }
            )
        }
    }
}