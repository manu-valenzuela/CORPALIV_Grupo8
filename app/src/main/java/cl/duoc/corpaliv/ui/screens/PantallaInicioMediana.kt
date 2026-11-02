package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
fun PantallaInicioMediana(
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
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

                Text(
                    text = "Últimas Noticias",
                    style = MaterialTheme.typography.titleLarge
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