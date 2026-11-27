package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.DonacionUiState

@Composable
fun PantallaResumenDonacion(
    claseAnchoVentana: WindowWidthSizeClass,
    formState: DonacionUiState,
    alVolver: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaResumenDonacionCompacta(
                formState = formState,
                alVolver = alVolver
            )
        }
    )
}
