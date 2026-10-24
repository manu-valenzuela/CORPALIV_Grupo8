package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun SeleccionarPantallaAdaptativa(
    claseAnchoVentana: WindowWidthSizeClass,
    pantallaCompacta: @Composable () -> Unit,
    pantallaMediana: @Composable () -> Unit,
    pantallaExpandida: @Composable () -> Unit
) {
    when (claseAnchoVentana) {
        WindowWidthSizeClass.Compact -> pantallaCompacta()
        WindowWidthSizeClass.Medium -> pantallaMediana()
        WindowWidthSizeClass.Expanded -> pantallaExpandida()
        else -> pantallaCompacta()
    }
}