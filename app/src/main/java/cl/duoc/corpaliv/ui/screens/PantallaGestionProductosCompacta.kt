package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.ui.components.AvisoExito
import cl.duoc.corpaliv.ui.components.TarjetaElementoGestion
import cl.duoc.corpaliv.viewmodel.EstadoGestionProductos

@Composable
fun PantallaGestionProductosCompacta(
    estado: EstadoGestionProductos,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onUrlImagenChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelarEdicion: () -> Unit,
    onEditar: (Producto) -> Unit,
    onEliminar: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Formulario: crea un producto nuevo o edita el seleccionado
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (estado.idEditando == null) "Nuevo producto" else "Editar producto",
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedTextField(
                        value = estado.nombre,
                        onValueChange = onNombreChange,
                        label = { Text("Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.descripcion,
                        onValueChange = onDescripcionChange,
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = estado.precio,
                            onValueChange = onPrecioChange,
                            label = { Text("Precio ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = estado.stock,
                            onValueChange = onStockChange,
                            label = { Text("Stock") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = estado.urlImagen,
                        onValueChange = onUrlImagenChange,
                        label = { Text("URL de la imagen (opcional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    estado.error?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onGuardar, enabled = !estado.guardadoExitoso) {
                            Text("Guardar")
                        }
                        if (estado.idEditando != null) {
                            OutlinedButton(onClick = onCancelarEdicion) {
                                Text("Cancelar")
                            }
                        }
                    }

                    AvisoExito(visible = estado.guardadoExitoso, mensaje = "¡Producto guardado con éxito!")
                }
            }
        }

        items(estado.lista, key = { it.id }) { producto ->
            TarjetaElementoGestion(
                titulo = producto.nombre,
                detalle = "$ ${producto.precio.toInt()} · Stock: ${producto.stock}",
                onEditar = { onEditar(producto) },
                onEliminar = { onEliminar(producto.id) },
                modifier = Modifier.animateItem()
            )
        }
    }
}
