package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.EstadoPantallaTienda

@Composable
fun PantallaTienda(
    claseAnchoVentana: WindowWidthSizeClass,
    estado: EstadoPantallaTienda,
    onSumarCantidad: (Long) -> Unit,
    onRestarCantidad: (Long) -> Unit,
    onConfirmarCompra: (Producto) -> Unit,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaTiendaCompacta(
                estado = estado,
                onSumarCantidad = onSumarCantidad,
                onRestarCantidad = onRestarCantidad,
                onConfirmarCompra = onConfirmarCompra,
                indicadores = indicadores,
                onCambiarConversion = onCambiarConversion
            )
        }
    )
}