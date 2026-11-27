package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.EstadoLogin

@Composable
fun PantallaLogin(
    claseAnchoVentana: WindowWidthSizeClass,
    estado: EstadoLogin,
    onCorreoChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onCrearCuenta: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaLoginCompacta(
                estado = estado,
                onCorreoChange = onCorreoChange,
                onContrasenaChange = onContrasenaChange,
                onIniciarSesion = onIniciarSesion,
                onCrearCuenta = onCrearCuenta
            )
        }
    )
}
