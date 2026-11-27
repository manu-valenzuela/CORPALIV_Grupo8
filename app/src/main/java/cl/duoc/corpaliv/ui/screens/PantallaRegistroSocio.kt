package cl.duoc.corpaliv.ui.screens

import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.viewmodel.RegistroUsuarioUiState

@Composable
fun PantallaRegistroSocio(
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
    PantallaRegistroSocioCompacta(
        estado = estado,
        onNombreChange = onNombreChange,
        onApellidoChange = onApellidoChange,
        onRutChange = onRutChange,
        onFechaNacimientoChange = onFechaNacimientoChange,
        onDireccionChange = onDireccionChange,
        onDonacionChange = onDonacionChange,
        onMontoPersonalizadoChange = onMontoPersonalizadoChange,
        onCorreoChange = onCorreoChange,
        onContrasenaChange = onContrasenaChange,
        onAlternarContrasena = onAlternarContrasena,
        onObtenerUbicacion = onObtenerUbicacion,
        onPermisoUbicacionDenegado = onPermisoUbicacionDenegado,
        onRegistrar = onRegistrar
    )
}
