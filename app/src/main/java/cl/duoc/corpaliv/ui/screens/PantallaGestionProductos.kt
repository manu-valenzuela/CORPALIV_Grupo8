package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.EstadoGestionProductos

@Composable
fun PantallaGestionProductos(
    claseAnchoVentana: WindowWidthSizeClass,
    estado: EstadoGestionProductos,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onUrlImagenChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelarEdicion: () -> Unit,
    onEditar: (Producto) -> Unit,
    onEliminar: (Long) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaGestionProductosCompacta(
                estado = estado,
                onNombreChange = onNombreChange,
                onDescripcionChange = onDescripcionChange,
                onPrecioChange = onPrecioChange,
                onStockChange = onStockChange,
                onUrlImagenChange = onUrlImagenChange,
                onGuardar = onGuardar,
                onCancelarEdicion = onCancelarEdicion,
                onEditar = onEditar,
                onEliminar = onEliminar
            )
        }
    )
}
