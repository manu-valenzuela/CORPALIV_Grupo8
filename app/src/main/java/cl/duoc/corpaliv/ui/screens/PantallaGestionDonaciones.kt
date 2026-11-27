package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.data.model.DonacionExpress
import cl.duoc.corpaliv.data.model.Usuario
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaGestionDonaciones(
    claseAnchoVentana: WindowWidthSizeClass,
    listaDonaciones: List<DonacionExpress>,
    listaUsuarios: List<Usuario>,
    indicadores: EstadoIndicadores,
    onCambiarConversion: (Boolean) -> Unit,
    onEliminarDonacion: (Long) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaGestionDonacionesCompacta(
                listaDonaciones = listaDonaciones,
                listaUsuarios = listaUsuarios,
                indicadores = indicadores,
                onCambiarConversion = onCambiarConversion,
                onEliminarDonacion = onEliminarDonacion
            )
        }
    )
}