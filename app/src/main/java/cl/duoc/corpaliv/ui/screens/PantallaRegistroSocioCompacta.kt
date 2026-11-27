package cl.duoc.corpaliv.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.corpaliv.ui.components.AvisoExito
import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.viewmodel.RegistroUsuarioUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroSocioCompacta(
    estado: RegistroUsuarioUiState,
    onNombreChange: (String) -> Unit,
    onApellidoChange: (String) -> Unit,
    onRutChange: (String) -> Unit,
    onFechaNacimientoChange: (String) -> Unit,
    onDireccionChange: (String) -> Unit,
    onDonacionChange: (OpcionDonacionMensual) -> Unit,
    onMontoPersonalizadoChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onAlternarContrasena: () -> Unit,
    onObtenerUbicacion: () -> Unit,
    onPermisoUbicacionDenegado: () -> Unit,
    onRegistrar: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // El permiso de ubicación debe pedirse desde la UI (requisito de Android)
    val lanzadorPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) onObtenerUbicacion() else onPermisoUbicacionDenegado()
    }

    fun solicitarUbicacion() {
        val permiso = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
            onObtenerUbicacion()
        } else {
            lanzadorPermiso.launch(permiso)
        }
    }

    // Datos derivados del estado (no son estado nuevo)
    val personalizado = estado.donacionMensual as? OpcionDonacionMensual.Personalizado
    val esPersonalizado = personalizado != null
    val textoMontoPersonalizado = personalizado?.montoLibre?.takeIf { it > 0 }?.toString() ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // --- Datos Personales ---
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = { estado.errores.nombre?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.apellido,
            onValueChange = onApellidoChange,
            label = { Text("Apellido") },
            isError = estado.errores.apellido != null,
            supportingText = { estado.errores.apellido?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.rut,
            onValueChange = onRutChange,
            label = { Text("RUT (ej: 12345678-K)") },
            isError = estado.errores.rut != null,
            supportingText = { estado.errores.rut?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.fechaNacimiento,
            onValueChange = onFechaNacimientoChange,
            label = { Text("Fecha Nacimiento (DD/MM/AAAA)") },
            isError = estado.errores.fechaNacimiento != null,
            supportingText = { estado.errores.fechaNacimiento?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Dirección con GPS ---
        val errorDireccion = estado.errores.direccion ?: estado.errorUbicacion
        OutlinedTextField(
            value = estado.direccion,
            onValueChange = onDireccionChange,
            label = { Text("Dirección de Residencia") },
            isError = errorDireccion != null,
            supportingText = { errorDireccion?.let { Text(it) } },
            trailingIcon = {
                IconButton(onClick = { solicitarUbicacion() }) {
                    if (estado.cargandoUbicacion) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Obtener Ubicación",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Selector de Donación Mensual ---
        Text(
            text = "Selecciona tu aporte mensual:",
            style = MaterialTheme.typography.titleMedium
        )

        val opcionesFijas = listOf(
            OpcionDonacionMensual.CincoMil,
            OpcionDonacionMensual.QuinceMil,
            OpcionDonacionMensual.VeinticincoMil
        )

        opcionesFijas.forEach { opcion ->
            val seleccionada = estado.donacionMensual == opcion
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDonacionChange(opcion) },
                colors = CardDefaults.cardColors(
                    containerColor = if (seleccionada) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (seleccionada) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = seleccionada,
                        onClick = { onDonacionChange(opcion) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = opcion.etiqueta,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = opcion.impacto,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // --- Opción de Monto Personalizado ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMontoPersonalizadoChange(textoMontoPersonalizado) },
            colors = CardDefaults.cardColors(
                containerColor = if (esPersonalizado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            ),
            border = if (esPersonalizado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = esPersonalizado,
                        onClick = { onMontoPersonalizadoChange(textoMontoPersonalizado) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Otro monto mensual",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (esPersonalizado) {
                    OutlinedTextField(
                        value = textoMontoPersonalizado,
                        onValueChange = onMontoPersonalizadoChange,
                        label = { Text("Monto voluntario ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }

        estado.errores.donacionMensual?.let { err ->
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // --- Credenciales ---
        OutlinedTextField(
            value = estado.correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            isError = estado.errores.correo != null,
            supportingText = { estado.errores.correo?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = onContrasenaChange,
            label = { Text("Contraseña") },
            visualTransformation = if (estado.contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = onAlternarContrasena) {
                    Icon(
                        imageVector = if (estado.contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar Contraseña"
                    )
                }
            },
            isError = estado.errores.contrasena != null,
            supportingText = { estado.errores.contrasena?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onRegistrar,
            enabled = !estado.registroExitoso,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Completar Registro")
        }

        AvisoExito(
            visible = estado.registroExitoso,
            mensaje = "¡Registro completado con éxito!"
        )

        // Resumen de errores debajo del botón
        val listaErrores = listOfNotNull(
            estado.errores.nombre,
            estado.errores.apellido,
            estado.errores.rut,
            estado.errores.fechaNacimiento,
            estado.errores.direccion,
            estado.errores.donacionMensual,
            estado.errores.correo,
            estado.errores.contrasena
        )

        if (listaErrores.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Por favor corrige los siguientes errores:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    listaErrores.forEach { error ->
                        Text(
                            text = "• $error",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}
