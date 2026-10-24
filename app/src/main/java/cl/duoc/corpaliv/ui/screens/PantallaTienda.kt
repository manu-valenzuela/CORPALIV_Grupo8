package cl.duoc.corpaliv.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.model.Producto
import cl.duoc.corpaliv.ui.utils.SeleccionarPantallaAdaptativa

@Composable
fun PantallaTienda(
    claseAnchoVentana: WindowWidthSizeClass,
    listaProductos: List<Producto>,
    alAgregarAlCarrito: (Producto) -> Unit
) {
    SeleccionarPantallaAdaptativa(
        claseAnchoVentana = claseAnchoVentana,
        pantallaCompacta = {
            PantallaTiendaCompacta(
                listaProductos = listaProductos,
                alAgregarAlCarrito = alAgregarAlCarrito
            )
        },
        pantallaMediana = {
            PantallaTiendaMediana(
                listaProductos = listaProductos,
                alAgregarAlCarrito = alAgregarAlCarrito
            )
        },
        pantallaExpandida = {
            PantallaTiendaExpandida(
                listaProductos = listaProductos,
                alAgregarAlCarrito = alAgregarAlCarrito
            )
        }
    )
}