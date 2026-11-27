package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.ui.components.AvisoExito
import cl.duoc.corpaliv.ui.components.TarjetaElementoGestion
import cl.duoc.corpaliv.viewmodel.EstadoGestionNoticias

@Composable
fun PantallaGestionNoticiasCompacta(
    estado: EstadoGestionNoticias,
    onTituloChange: (String) -> Unit,
    onResumenChange: (String) -> Unit,
    onUrlImagenChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelarEdicion: () -> Unit,
    onEditar: (Noticia) -> Unit,
    onEliminar: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Formulario: crea una noticia nueva o edita la seleccionada
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (estado.idEditando == null) "Nueva noticia" else "Editar noticia",
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedTextField(
                        value = estado.titulo,
                        onValueChange = onTituloChange,
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.resumen,
                        onValueChange = onResumenChange,
                        label = { Text("Resumen") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
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

                    AvisoExito(visible = estado.guardadoExitoso, mensaje = "¡Noticia guardada con éxito!")
                }
            }
        }

        items(estado.lista, key = { it.id }) { noticia ->
            TarjetaElementoGestion(
                titulo = noticia.titulo,
                detalle = noticia.fecha,
                onEditar = { onEditar(noticia) },
                onEliminar = { onEliminar(noticia.id) },
                modifier = Modifier.animateItem()
            )
        }
    }
}
