package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.DonacionUiState
import cl.duoc.corpaliv.model.Evento
import cl.duoc.corpaliv.model.Noticia
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaInicio(
    claseAnchoVentana: WindowWidthSizeClass,
    cargando: Boolean,
    listaNoticias: List<Noticia>,
    proximoEvento: Evento?,
    formState: DonacionUiState,
    mostrarMensajeAnimado: Boolean,
    mensajePersistido: String,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onMontoChange: (String) -> Unit,
    onConfirmarDonacion: () -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaInicioCompacta(
                cargando = cargando,
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                formState = formState,
                mostrarMensajeAnimado = mostrarMensajeAnimado,
                mensajePersistido = mensajePersistido,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onMontoChange = onMontoChange,
                onConfirmarDonacion = onConfirmarDonacion
            )
        },
        pantallaMediana = {
            PantallaInicioMediana(
                cargando = cargando,
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                formState = formState,
                mostrarMensajeAnimado = mostrarMensajeAnimado,
                mensajePersistido = mensajePersistido,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onMontoChange = onMontoChange,
                onConfirmarDonacion = onConfirmarDonacion
            )
        },
        pantallaExpandida = {
            PantallaInicioExpandida(
                cargando = cargando,
                listaNoticias = listaNoticias,
                proximoEvento = proximoEvento,
                formState = formState,
                mostrarMensajeAnimado = mostrarMensajeAnimado,
                mensajePersistido = mensajePersistido,
                onNombreChange = onNombreChange,
                onCorreoChange = onCorreoChange,
                onMontoChange = onMontoChange,
                onConfirmarDonacion = onConfirmarDonacion
            )
        }
    )
}