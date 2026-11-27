package cl.duoc.corpaliv.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.ui.utils.formatoDolares
import cl.duoc.corpaliv.ui.utils.formatoUf
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores

// Interruptor para mostrar u ocultar la conversión de montos
@Composable
fun InterruptorConversion(
    texto: String,
    indicadores: EstadoIndicadores,
    onCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = indicadores.mostrarConversion && indicadores.disponible,
            onCheckedChange = onCambiar,
            enabled = indicadores.disponible
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (indicadores.disponible) texto else "Conversión no disponible (sin conexión)",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// Equivalencia en dólares de un monto en pesos (para socios)
@Composable
fun TextoEnDolares(
    montoPesos: Double,
    indicadores: EstadoIndicadores,
    modifier: Modifier = Modifier
) {
    val valorDolar = indicadores.valorDolar
    AnimatedVisibility(
        visible = indicadores.mostrarConversion && valorDolar != null && montoPesos > 0,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Text(
            text = "≈ ${formatoDolares(montoPesos, valorDolar ?: 1.0)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Equivalencia en dólares y UF de un total en pesos (para el panel del trabajador)
@Composable
fun TextoEnDolaresYUf(
    montoPesos: Double,
    indicadores: EstadoIndicadores,
    modifier: Modifier = Modifier
) {
    val valorDolar = indicadores.valorDolar
    val valorUf = indicadores.valorUf
    AnimatedVisibility(
        visible = indicadores.mostrarConversion && valorDolar != null && valorUf != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Text(
            text = "≈ ${formatoDolares(montoPesos, valorDolar ?: 1.0)} · ${formatoUf(montoPesos, valorUf ?: 1.0)}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
