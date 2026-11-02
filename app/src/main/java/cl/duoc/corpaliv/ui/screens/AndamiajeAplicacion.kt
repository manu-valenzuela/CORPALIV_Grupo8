package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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

val elementosSocio = listOf(
    ElementoNavegacion("Inicio", RutaAplicacion.SeleccionRol.ruta, Icons.Default.Home),
    ElementoNavegacion("Portal", RutaAplicacion.PortalSocio.ruta, Icons.Default.Info),
    ElementoNavegacion("Tienda", RutaAplicacion.Tienda.ruta, Icons.Default.ShoppingBag),
    ElementoNavegacion("Registrarse", RutaAplicacion.RegistroSocio.ruta, Icons.Default.Person)
)

val elementosTrabajador = listOf(
    ElementoNavegacion("Inicio", RutaAplicacion.SeleccionRol.ruta, Icons.Default.Home),
    ElementoNavegacion("Usuarios", RutaAplicacion.GestionUsuarios.ruta, Icons.Default.AccountCircle),
    ElementoNavegacion("Donaciones", RutaAplicacion.GestionDonaciones.ruta, Icons.Default.VolunteerActivism),
    ElementoNavegacion("Ventas", RutaAplicacion.Tienda.ruta, Icons.Default.ShoppingBag)
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
    if (rutaActual == RutaAplicacion.SeleccionRol.ruta) {
        Box(modifier = Modifier.fillMaxSize()) {
            contenido()
        }
        return
    }

    val elementosNavegacion = if (esTrabajador) elementosTrabajador else elementosSocio

    val tituloPantalla = when (rutaActual) {
        RutaAplicacion.PortalSocio.ruta -> "Portal CORPALIV"
        RutaAplicacion.Tienda.ruta -> if (esTrabajador) "Gestión de Ventas" else "Tienda CORPALIV"
        RutaAplicacion.RegistroSocio.ruta -> if (esTrabajador) "Crear Usuario" else "Registro de Socio"
        RutaAplicacion.GestionUsuarios.ruta -> "Gestión de Usuarios"
        RutaAplicacion.GestionDonaciones.ruta -> "Gestión de Donaciones"
        "resumen" -> "Resumen de Aporte"
        else -> "CORPALIV"
    }

    val topBarComposable = @Composable {
        TopAppBar(
            title = { Text(text = tituloPantalla) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

    Scaffold(
        topBar = topBarComposable,
        bottomBar = {
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