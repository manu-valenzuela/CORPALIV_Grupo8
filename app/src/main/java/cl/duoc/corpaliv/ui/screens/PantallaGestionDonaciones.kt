package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Usuario
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaGestionDonaciones(
    claseAnchoVentana: WindowWidthSizeClass,
    listaDonaciones: List<DonacionExpress>,
    listaUsuarios: List<Usuario>
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaGestionDonacionesCompacta(
                listaDonaciones = listaDonaciones,
                listaUsuarios = listaUsuarios
            )
        }
    )
}