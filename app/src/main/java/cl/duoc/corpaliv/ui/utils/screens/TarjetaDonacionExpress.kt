package cl.duoc.corpaliv.ui.utils.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.MetodoPago

@Composable
fun TarjetaDonacionExpres(
    alConfirmarDonacion: (DonacionExpress) -> Unit,
    modifier: Modifier = Modifier
) {
    var textoMonto by remember { mutableStateOf("") }
    var metodoSeleccionado by remember { mutableStateOf(MetodoPago.TARJETA_CREDITO) }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Haz tu aportación exprés",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = textoMonto,
                onValueChange = { textoMonto = it },
                label = { Text("Monto a donar ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Método de pago:",
                style = MaterialTheme.typography.bodySmall
            )

            MetodoPago.values().forEach { metodo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (metodo == metodoSeleccionado),
                        onClick = { metodoSeleccionado = metodo }
                    )
                    Text(
                        text = when (metodo) {
                            MetodoPago.TARJETA_CREDITO -> "Tarjeta de Crédito"
                            MetodoPago.TARJETA_DEBITO -> "Tarjeta de Débito"
                            MetodoPago.TRANSFERENCIA_BANCARIA -> "Transferencia Bancaria"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    val montoNumerico = textoMonto.toDoubleOrNull() ?: 0.0
                    if (montoNumerico > 0.0) {
                        alConfirmarDonacion(DonacionExpress(montoNumerico, metodoSeleccionado))
                    }
                },
                enabled = textoMonto.isNotBlank() && (textoMonto.toDoubleOrNull() ?: 0.0) > 0.0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Donar Ahora")
            }
        }
    }
}