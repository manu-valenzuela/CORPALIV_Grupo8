package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaTienda(
    claseAnchoVentana: WindowWidthSizeClass,
    listaProductos: List<Producto>,
    compraExitosa: Boolean,
    onConfirmarCompra: (Producto, Int) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaTiendaCompacta(
                listaProductos = listaProductos,
                compraExitosa = compraExitosa,
                onConfirmarCompra = onConfirmarCompra
            )
        }
    )
}