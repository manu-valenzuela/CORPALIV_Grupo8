package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.data.model.Venta
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaVentas(
    claseAnchoVentana: WindowWidthSizeClass,
    listaVentas: List<Venta>,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaVentasCompacta(
                listaVentas = listaVentas,
                indicadores = indicadores,
                onCambiarConversion = onCambiarConversion
            )
        }
    )
}