package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa
import cl.duoc.corpaliv.viewmodel.EstadoGestionNoticias

@Composable
fun PantallaGestionNoticias(
    claseAnchoVentana: WindowWidthSizeClass,
    estado: EstadoGestionNoticias,
    onTituloChange: (String) -> Unit,
    onResumenChange: (String) -> Unit,
    onUrlImagenChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelarEdicion: () -> Unit,
    onEditar: (Noticia) -> Unit,
    onEliminar: (Long) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaGestionNoticiasCompacta(
                estado = estado,
                onTituloChange = onTituloChange,
                onResumenChange = onResumenChange,
                onUrlImagenChange = onUrlImagenChange,
                onGuardar = onGuardar,
                onCancelarEdicion = onCancelarEdicion,
                onEditar = onEditar,
                onEliminar = onEliminar
            )
        }
    )
}
