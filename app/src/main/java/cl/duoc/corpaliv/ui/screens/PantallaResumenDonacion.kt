package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.ViewModelInicio

@Composable
fun PantallaResumenDonacion(
    claseAnchoVentana: WindowWidthSizeClass,
    viewModel: ViewModelInicio,
    alVolver: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()

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