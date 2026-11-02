package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaGestionUsuarios(
    claseAnchoVentana: WindowWidthSizeClass,
    listaUsuarios: List<Usuario>,
    onCambiarRol: (String, RolUsuario) -> Unit,
    onCrearUsuario: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaGestionUsuariosCompacta(
                listaUsuarios = listaUsuarios,
                onCambiarRol = onCambiarRol,
                onCrearUsuario = onCrearUsuario
            )
        }
    )
}