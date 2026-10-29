package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.*
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
fun PantallaInicioCompacta(
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
    // Muestra del Loader según la Guía 12[cite: 76]
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                TarjetaEvento(evento = evento, alVerDetallesEvento = {})
            }

            Text(
                text = "Últimas Noticias",
                style = MaterialTheme.typography.titleLarge
            )

            listaNoticias.forEach { noticia ->
                TarjetaNoticia(noticia = noticia, alSeleccionarNoticia = {})
            }
        }
    }
}