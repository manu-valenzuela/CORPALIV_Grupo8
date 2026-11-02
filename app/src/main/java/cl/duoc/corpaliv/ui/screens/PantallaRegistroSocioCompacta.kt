package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.RegistroUsuarioUiState

@Composable
fun PantallaRegistroSocioCompacta(
    formState: RegistroUsuarioUiState,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Nuevo Socio",
            style = MaterialTheme.typography.headlineSmall
        )

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

        Button(
            onClick = onRegistrar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Completar Registro de Socio")
        }
    }
}