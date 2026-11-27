package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.ui.components.AvisoExito
import cl.duoc.corpaliv.ui.components.TarjetaProducto
import cl.duoc.corpaliv.data.model.Producto

@Composable
fun PantallaTiendaCompacta(
    listaProductos: List<Producto>,
    compraExitosa: Boolean,
    onConfirmarCompra: (Producto, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AvisoExito(
            visible = compraExitosa,
            mensaje = "¡Compra realizada con éxito!"
        )

        listaProductos.forEach { producto ->
            TarjetaProducto(
                producto = producto,
                onConfirmarCompra = onConfirmarCompra
            )
        }
    }
}
