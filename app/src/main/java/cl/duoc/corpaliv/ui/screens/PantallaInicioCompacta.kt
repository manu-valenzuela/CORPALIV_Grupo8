package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.ui.components.TarjetaDonacionExpress
import cl.duoc.corpaliv.ui.components.TarjetaNoticia
import cl.duoc.corpaliv.viewmodel.DonacionUiState
import cl.duoc.corpaliv.data.model.Noticia

@Composable
fun PantallaInicioCompacta(
    cargando: Boolean,
    listaNoticias: List<Noticia>,
    formState: DonacionUiState,
    errorNoticias: String?,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onConfirmarDonacion: () -> Unit,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )

        {
            Text(
                text = "Últimas Noticias",
                style = MaterialTheme.typography.titleLarge
            )

            errorNoticias?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            listaNoticias.forEach { noticia ->
                TarjetaNoticia(noticia = noticia, alSeleccionarNoticia = {})
            }
                Text(
                    text = "Donación Express",
                    style = MaterialTheme.typography.titleLarge
                )

                TarjetaDonacionExpress(
                    formState = formState,
                    onNombreChange = onNombreChange,
                    onCorreoChange = onCorreoChange,
                    onMontoChange = onMontoChange,
                    onConfirmarDonacion = onConfirmarDonacion,
                    indicadores = indicadores,
                    onCambiarConversion = onCambiarConversion
                )


        }
    }
}
