package cl.duoc.corpaliv.ui.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia

@Composable
fun PantallaInicioCompacta(
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    alRealizarDonacion: (DonacionExpress) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

@Composable
fun TarjetaDonacionExpress(alConfirmarDonacion: (DonacionExpress) -> Unit) {
    TODO("Not yet implemented")
}

@Preview(
    name = "Small",
    widthDp = 360,
    heightDp = 480,
    showBackground = true
)
@Composable
fun PantallaCompactaPreview() {
        PantallaInicioCompacta(
            listaNoticias = emptyList(),
            proximoEvento = null,
            alRealizarDonacion = {}
        )
}