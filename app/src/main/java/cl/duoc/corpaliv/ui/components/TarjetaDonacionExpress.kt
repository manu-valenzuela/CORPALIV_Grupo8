package cl.duoc.corpaliv.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.viewmodel.DonacionUiState

@Composable
fun TarjetaDonacionExpress(
    formState: DonacionUiState,
    mensajePersistido: String,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onConfirmarDonacion: () -> Unit,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Animar color del botón usando valores del tema global (MaterialTheme.colorScheme)
    val colorBotonAnimado by animateColorAsState(
        targetValue = if (formState.nombre.isNotBlank() && formState.monto.isNotBlank()) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(durationMillis = 500),
        label = "colorBoton"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Haz tu Aporte a CORPALIV",
                style = MaterialTheme.typography.titleMedium
            )

            if (mensajePersistido.isNotBlank()) {
                Text(
                    text = mensajePersistido,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = formState.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre Completo") },
                isError = formState.errores.nombre != null,
                supportingText = {
                    formState.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = formState.correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo Electrónico") },
                isError = formState.errores.correo != null,
                supportingText = {
                    formState.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = formState.monto,
                onValueChange = onMontoChange,
                label = { Text("Monto a Donar ($)") },
                isError = formState.errores.monto != null,
                supportingText = {
                    formState.errores.monto?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            TextoEnDolares(
                montoPesos = formState.monto.toDoubleOrNull() ?: 0.0,
                indicadores = indicadores
            )

            InterruptorConversion(
                texto = "Ver monto en dólares",
                indicadores = indicadores,
                onCambiar = onCambiarConversion
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onConfirmarDonacion,
                enabled = !formState.donacionExitosa,
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonAnimado),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Donación")
            }

            AvisoExito(
                visible = formState.donacionExitosa,
                mensaje = "¡Gracias por tu donación!"
            )

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:222000000")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Llamar a CORPALIV")
            }
        }
    }
}