package cl.duoc.corpaliv.ui.utils.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.DonacionExpress
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaInicio(
    claseAnchoVentana: WindowWidthSizeClass,
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    alRealizarDonacion: (DonacionExpress) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaInicioCompacta(
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                alRealizarDonacion = alRealizarDonacion
            )
        },
        pantallaMediana = {
            PantallaInicioMediana(
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                alRealizarDonacion = alRealizarDonacion
            )
        },
        pantallaExpandida = {
            PantallaInicioExpandida(
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                alRealizarDonacion = alRealizarDonacion
            )
        }
    )
}