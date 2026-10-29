package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.viewmodel.ViewModelInicio

@Composable
fun ResumenDonacionScreen(
    viewModel: ViewModelInicio,
    alVolver: () -> Unit
) {
    // Observamos el estado del formulario compartido desde el ViewModel (Guía 11)[cite: 87]
    val formState by viewModel.formState.collectAsState()

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Resumen de la Donación",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre del Donante: ${formState.nombre}")
        Text(text = "Correo Registrado: ${formState.correo}")
        Text(text = "Monto Aportado: $${formState.monto}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = alVolver) {
            Text("Volver al Inicio")
        }
    }
}