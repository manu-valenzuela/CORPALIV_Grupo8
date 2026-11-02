package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario

@Composable
fun TarjetaUsuarioGestion(
    usuario: Usuario,
    onCambiarRol: (RolUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    val rolesDisponibles = listOf(
        RolUsuario.Socio,
        RolUsuario.Voluntario(),
        RolUsuario.Funcionario()
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre y Apellido
            Text(
                text = "${usuario.nombre} ${usuario.apellido}".trim().ifEmpty { "Usuario sin nombre" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Correo Electrónico
            Text(
                text = usuario.correo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Aporte / Donación Mensual
            Text(
                text = "Aporte mensual: ${usuario.donacionMensual.etiqueta}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rol Actual
            Text(
                text = "Rol actual: ${usuario.rol.etiqueta}",
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Selector de Roles
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rolesDisponibles.forEach { rol ->
                    val esSeleccionado = usuario.rol::class == rol::class
                    FilterChip(
                        selected = esSeleccionado,
                        onClick = { onCambiarRol(rol) },
                        label = { Text(rol.etiqueta) }
                    )
                }
            }
        }
    }
}