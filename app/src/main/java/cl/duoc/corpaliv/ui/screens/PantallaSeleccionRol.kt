package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaSeleccionRol(
    claseAnchoVentana: WindowWidthSizeClass,
    alSeleccionarSocioInvitado: () -> Unit,
    alSeleccionarFuncionario: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaSeleccionRolCompacta(
                alSeleccionarSocioInvitado = alSeleccionarSocioInvitado,
                alSeleccionarFuncionario = alSeleccionarFuncionario
            )
        }
    )
}