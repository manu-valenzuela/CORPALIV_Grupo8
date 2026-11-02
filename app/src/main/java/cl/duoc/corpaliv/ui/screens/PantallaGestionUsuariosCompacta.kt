package cl.duoc.corpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario

@Composable
fun PantallaGestionUsuariosCompacta(
    listaUsuarios: List<Usuario>,
    onCambiarRol: (String, RolUsuario) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestión de Usuarios (Panel Funcionario)",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (listaUsuarios.isEmpty()) {
            Text(
                text = "No hay usuarios registrados en el sistema.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listaUsuarios, key = { it.id }) { usuario ->
                    TarjetaUsuarioGestion(
                        usuario = usuario,
                        onCambiarRol = { nuevoRol -> onCambiarRol(usuario.id, nuevoRol) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaUsuarioGestion(
    usuario: Usuario,
    onCambiarRol: (RolUsuario) -> Unit
) {
    val rolesDisponibles = listOf(
        RolUsuario.Socio,
        RolUsuario.Voluntario(),
        RolUsuario.Funcionario()
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = usuario.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = usuario.correo, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Rol actual: ${usuario.rol.etiqueta}", style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.height(8.dp))
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