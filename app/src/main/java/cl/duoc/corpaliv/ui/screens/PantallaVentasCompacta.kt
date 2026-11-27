package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.ui.components.TextoEnDolaresYUf
import cl.duoc.corpaliv.ui.components.InterruptorConversion
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.ui.components.TarjetaVenta
import cl.duoc.corpaliv.data.model.Venta

@Composable
fun PantallaVentasCompacta(
    listaVentas: List<Venta>,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit,
    onAdministrarProductos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val recaudacionTotal = listaVentas.sumOf { it.precioTotal }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedButton(
            onClick = onAdministrarProductos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Administrar productos")
        }

        Spacer(modifier = Modifier.height(12.dp))

        InterruptorConversion(
            texto = "Ver recaudación en dólares y UF",
            indicadores = indicadores,
            onCambiar = onCambiarConversion
        )

        // Tarjeta resumen
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Ventas Totales: ${listaVentas.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Recaudado: $ ${recaudacionTotal.toInt()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    TextoEnDolaresYUf(montoPesos = recaudacionTotal, indicadores = indicadores)
                }
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (listaVentas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay ventas registradas en el sistema.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listaVentas, key = { it.id }) { venta ->
                    TarjetaVenta(venta = venta)
                }
            }
        }
    }
}
