package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.RegistroUsuarioUiState
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaRegistroSocio(
    claseAnchoVentana: WindowWidthSizeClass,
    formState: RegistroUsuarioUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onRegistrar: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaRegistroSocioCompacta(
                formState = formState,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onRegistrar = onRegistrar
            )
        }
    )
}