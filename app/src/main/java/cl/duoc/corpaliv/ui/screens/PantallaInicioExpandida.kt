package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia

@Composable
fun PantallaInicioExpandida(
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    alRealizarDonacion: (DonacionExpress) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Columna Izquierda: Accesos Rápidos (Donación y Próximo Evento)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TarjetaDonacionExpress(
                alConfirmarDonacion = alRealizarDonacion
            )

            proximoEvento?.let { evento ->
                TarjetaEvento(
                    evento = evento,
                    alVerDetallesEvento = { /* Navegar al evento */ }
                )
            }
        }

        // Columna Derecha: Feed Principal de Noticias
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
                    alSeleccionarNoticia = { /* Navegar a la noticia */ }
                )
            }
        }
    }
}

@Preview(
    name = "Large",
    widthDp = 1280,
    heightDp = 720,
    showBackground = true
)
@Composable
fun PantallaInicioExpandidaPreview() {
        PantallaInicioExpandida(
            listaNoticias = emptyList(),
            proximoEvento = null,
            alRealizarDonacion = {}
        )
}