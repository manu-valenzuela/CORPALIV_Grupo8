package cl.duoc.corpaliv.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.Producto

@Composable
fun PantallaTiendaCompacta(
    listaProductos: List<Producto>,
    mensajeConfirmacion: String?,
    onConfirmarCompra: (Producto, Int) -> Unit,
    onLimpiarMensaje: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(mensajeConfirmacion) {
        mensajeConfirmacion?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            onLimpiarMensaje()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tienda Solidaria",
            style = MaterialTheme.typography.headlineSmall
        )

        listaProductos.forEach { producto ->
            TarjetaProducto(
                producto = producto,
                onConfirmarCompra = onConfirmarCompra
            )
        }
    }
}