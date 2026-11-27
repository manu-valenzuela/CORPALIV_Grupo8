package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.viewmodel.DonacionUiState
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaInicio(
    claseAnchoVentana: WindowWidthSizeClass,
    cargando: Boolean,
    listaNoticias: List<Noticia>,
    formState: DonacionUiState,
    errorNoticias: String?,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onConfirmarDonacion: () -> Unit,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaInicioCompacta(
                cargando = cargando,
                listaNoticias = listaNoticias,
                formState = formState,
                errorNoticias = errorNoticias,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onMontoChange = onMontoChange,
                onConfirmarDonacion = onConfirmarDonacion,
                indicadores = indicadores,
                onCambiarConversion = onCambiarConversion
            )
        }
    )
}