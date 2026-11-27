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
import cl.duoc.corpaliv.viewmodel.EstadoPantallaTienda

@Composable
fun PantallaTiendaCompacta(
    estado: EstadoPantallaTienda,
    onSumarCantidad: (String) -> Unit,
    onRestarCantidad: (String) -> Unit,
    onConfirmarCompra: (Producto) -> Unit,
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
            visible = estado.compraExitosa,
            mensaje = "¡Compra realizada con éxito!"
        )

        estado.listaProductos.forEach { producto ->
            TarjetaProducto(
                producto = producto,
                cantidad = estado.cantidadDe(producto.id),
                onSumar = { onSumarCantidad(producto.id) },
                onRestar = { onRestarCantidad(producto.id) },
                onConfirmarCompra = { onConfirmarCompra(producto) }
            )
        }
    }
}
