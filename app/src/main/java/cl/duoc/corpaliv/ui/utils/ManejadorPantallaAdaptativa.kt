package cl.duoc.corpaliv.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun SeleccionarPantallaAdaptativa(
    claseAnchoVentana: WindowWidthSizeClass,
    pantallaCompacta: @Composable () -> Unit,
    pantallaMediana: (@Composable () -> Unit)? = null,
    pantallaExpandida: (@Composable () -> Unit)? = null
) {
    when (claseAnchoVentana) {
        WindowWidthSizeClass.Compact -> pantallaCompacta()
        WindowWidthSizeClass.Medium -> (pantallaMediana ?: pantallaCompacta)()
        WindowWidthSizeClass.Expanded -> (pantallaExpandida ?: pantallaCompacta)()
        else -> pantallaCompacta()
    }
}