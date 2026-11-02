package cl.duoc.corpaliv.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
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
import cl.duoc.corpaliv.model.OpcionDonacionMensual
import cl.duoc.corpaliv.ui.utils.ManejadorUbicacion
import cl.duoc.corpaliv.viewmodel.ViewModelUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroSocioCompacta(
    viewModelUsuario: ViewModelUsuario,
    onRegistroExitoso: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModelUsuario.formState.collectAsState()
    val scrollState = rememberScrollState()

    var passwordVisible by remember { mutableStateOf(false) }
    var montoPersonalizadoTexto by remember { mutableStateOf("") }
    var esPersonalizadoSeleccionado by remember { mutableStateOf(false) }

    val manejadorUbicacion = remember { ManejadorUbicacion(context) }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            viewModelUsuario.setCargandoUbicacion(true)
            manejadorUbicacion.obtenerUbicacionActual(
                onResultado = { dir, lat, lng ->
                    viewModelUsuario.onDireccionChange(dir, lat, lng)
                    viewModelUsuario.setCargandoUbicacion(false)
                },
                onError = { err ->
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                    viewModelUsuario.setCargandoUbicacion(false)
                }
            )
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun obtenerUbicacion() {
        val permiso = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
            viewModelUsuario.setCargandoUbicacion(true)
            manejadorUbicacion.obtenerUbicacionActual(
                onResultado = { dir, lat, lng ->
                    viewModelUsuario.onDireccionChange(dir, lat, lng)
                    viewModelUsuario.setCargandoUbicacion(false)
                },
                onError = { err ->
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                    viewModelUsuario.setCargandoUbicacion(false)
                }
            )
        } else {
            locationLauncher.launch(permiso)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // --- Datos Personales ---
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModelUsuario.onNombreChange(it) },
            label = { Text("Nombre") },
            isError = state.errores.nombre != null,
            supportingText = { state.errores.nombre?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.apellido,
            onValueChange = { viewModelUsuario.onApellidoChange(it) },
            label = { Text("Apellido") },
            isError = state.errores.apellido != null,
            supportingText = { state.errores.apellido?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.rut,
            onValueChange = { viewModelUsuario.onRutChange(it) },
            label = { Text("RUT (ej: 12345678-K)") },
            isError = state.errores.rut != null,
            supportingText = { state.errores.rut?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.fechaNacimiento,
            onValueChange = { viewModelUsuario.onFechaNacimientoChange(it) },
            label = { Text("Fecha Nacimiento (DD/MM/AAAA)") },
            isError = state.errores.fechaNacimiento != null,
            supportingText = { state.errores.fechaNacimiento?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Dirección con GPS ---
        OutlinedTextField(
            value = state.direccion,
            onValueChange = { viewModelUsuario.onDireccionChange(it) },
            label = { Text("Dirección de Residencia") },
            isError = state.errores.direccion != null,
            supportingText = { state.errores.direccion?.let { Text(it) } },
            trailingIcon = {
                IconButton(onClick = { obtenerUbicacion() }) {
                    if (state.cargandoUbicacion) {
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
            val seleccionada = !esPersonalizadoSeleccionado && state.donacionMensual.javaClass == opcion.javaClass
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        esPersonalizadoSeleccionado = false
                        viewModelUsuario.onDonacionChange(opcion)
                    },
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
                        onClick = {
                            esPersonalizadoSeleccionado = false
                            viewModelUsuario.onDonacionChange(opcion)
                        }
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
                .clickable {
                    esPersonalizadoSeleccionado = true
                    val montoInt = montoPersonalizadoTexto.toIntOrNull() ?: 0
                    viewModelUsuario.onDonacionChange(OpcionDonacionMensual.Personalizado(montoInt))
                },
            colors = CardDefaults.cardColors(
                containerColor = if (esPersonalizadoSeleccionado) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            ),
            border = if (esPersonalizadoSeleccionado) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = esPersonalizadoSeleccionado,
                        onClick = {
                            esPersonalizadoSeleccionado = true
                            val montoInt = montoPersonalizadoTexto.toIntOrNull() ?: 0
                            viewModelUsuario.onDonacionChange(OpcionDonacionMensual.Personalizado(montoInt))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Otro monto mensual",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (esPersonalizadoSeleccionado) {
                    OutlinedTextField(
                        value = montoPersonalizadoTexto,
                        onValueChange = { texto ->
                            val filtrado = texto.filter { it.isDigit() }
                            montoPersonalizadoTexto = filtrado
                            val montoInt = filtrado.toIntOrNull() ?: 0
                            viewModelUsuario.onDonacionChange(OpcionDonacionMensual.Personalizado(montoInt))
                        },
                        label = { Text("Monto voluntario ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }

        state.errores.donacionMensual?.let { err ->
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // --- Credenciales ---
        OutlinedTextField(
            value = state.correo,
            onValueChange = { viewModelUsuario.onCorreoChange(it) },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            isError = state.errores.correo != null,
            supportingText = { state.errores.correo?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.contrasena,
            onValueChange = { viewModelUsuario.onContrasenaChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar Contraseña"
                    )
                }
            },
            isError = state.errores.contrasena != null,
            supportingText = { state.errores.contrasena?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModelUsuario.registrarSocio(onRegistroExitoso) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text("Completar Registro")
        }
    }
}