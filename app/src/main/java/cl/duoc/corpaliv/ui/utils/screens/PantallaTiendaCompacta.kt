package cl.duoc.corpaliv.ui.utils.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.Producto

@Composable
fun PantallaTiendaCompacta(
    listaProductos: List<Producto>,
    alAgregarAlCarrito: (Producto) -> Unit,
    modifier: Modifier = Modifier
) {
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
                alAgregarAlCarrito = alAgregarAlCarrito
            )
        }
    }
}