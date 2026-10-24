package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia

@Composable
fun PantallaInicioMediana(
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    alRealizarDonacion: (DonacionExpress) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 24.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Contenedor con ancho máximo centrado para mantener legibilidad en 1 columna
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Sección Donación Exprés
            TarjetaDonacionExpress(
                alConfirmarDonacion = alRealizarDonacion
            )

            // Sección Próximo Evento
            proximoEvento?.let { evento ->
                TarjetaEvento(
                    evento = evento,
                    alVerDetallesEvento = { /* Navegar al evento */ }
                )
            }

            // Sección Noticias
            Text(
                text = "Últimas Noticias",
                style = MaterialTheme.typography.titleLarge
            )

            listaNoticias.forEach { noticia ->
                TarjetaNoticia(
                    noticia = noticia,
                    alSeleccionarNoticia = { /* Navegar a la noticia */ }
                )
            }
        }
    }
}

@Preview(
    name = "Medium",
    widthDp = 720,
    heightDp = 1280,
    showBackground = true
)
@Composable
fun PantallaInicioMedianaPreview() {
        PantallaInicioMediana(
            listaNoticias = emptyList(),
            proximoEvento = null,
            alRealizarDonacion = {}
        )
}