package cl.duoc.corpaliv.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.data.model.Producto
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TarjetaProducto(
    producto: Producto,
    cantidad: Int,
    onSumar: () -> Unit,
    onRestar: () -> Unit,
    onConfirmarCompra: () -> Unit,
    indicadores: EstadoIndicadores,
    modifier: Modifier = Modifier
) {
    val precioTotal = (producto.precio * cantidad).toInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Imagen del producto: aparece con un fundido al terminar de cargar
        if (!producto.urlImagen.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(producto.urlImagen)
                    .crossfade(true)
                    .build(),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Precio unitario: $ ${producto.precio.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextoEnDolares(montoPesos = producto.precio, indicadores = indicadores)
            Text(
                text = if (producto.stock > 0) "Quedan ${producto.stock} unidades" else "Agotado",
                style = MaterialTheme.typography.bodySmall,
                color = if (producto.stock > 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de Cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cantidad:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedIconButton(
                        onClick = onRestar,
                        enabled = cantidad > 1,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Restar")
                    }

                    // El número se desliza hacia arriba al sumar y hacia abajo al restar
                    AnimatedContent(
                        targetState = cantidad,
                        transitionSpec = { transicionNumero() },
                        label = "cantidad"
                    ) { valor ->
                        Text(
                            text = "$valor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedIconButton(
                        onClick = onSumar,
                        enabled = cantidad < producto.stock,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Sumar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Precio Total y Botón
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleSmall
                )
                AnimatedContent(
                    targetState = precioTotal,
                    transitionSpec = { transicionNumero() },
                    label = "total"
                ) { valor ->
                    Text(
                        text = "$ $valor",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            TextoEnDolares(
                montoPesos = producto.precio * cantidad,
                indicadores = indicadores,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onConfirmarCompra,
                enabled = producto.stock > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (producto.stock > 0) "Confirmar Compra ($ $precioTotal)" else "Agotado")
            }
        }
    }
}

// Si el número sube, entra desde abajo; si baja, entra desde arriba
private fun AnimatedContentTransitionScope<Int>.transicionNumero(): ContentTransform =
    if (targetState > initialState) {
        (slideInVertically { altura -> altura } + fadeIn()) togetherWith
                (slideOutVertically { altura -> -altura } + fadeOut())
    } else {
        (slideInVertically { altura -> -altura } + fadeIn()) togetherWith
                (slideOutVertically { altura -> altura } + fadeOut())
    }
