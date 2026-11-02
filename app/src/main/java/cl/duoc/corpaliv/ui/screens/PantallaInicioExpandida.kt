package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.DonacionUiState
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia

@Composable
fun PantallaInicioExpandida(
    cargando: Boolean,
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    formState: DonacionUiState,
    mostrarMensajeAnimado: Boolean,
    mensajePersistido: String,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onConfirmarDonacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (cargando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TarjetaDonacionExpress(
                    formState = formState,
                    mostrarMensajeAnimado = mostrarMensajeAnimado,
                    mensajePersistido = mensajePersistido,
                    onNombreChange = onNombreChange,
                    onCorreoChange = onCorreoChange,
                    onMontoChange = onMontoChange,
                    onConfirmarDonacion = onConfirmarDonacion
                )

                proximoEvento?.let { evento ->
                    TarjetaEvento(
                        evento = evento,
                        alVerDetallesEvento = { }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Noticias Destacadas",
                    style = MaterialTheme.typography.headlineSmall
                )

                listaNoticias.forEach { noticia ->
                    TarjetaNoticia(
                        noticia = noticia,
                        alSeleccionarNoticia = { }
                    )
                }
            }
        }
    }
}