package cl.duoc.corpaliv.ui.screens

import androidx.compose.runtime.Composable
import cl.duoc.corpaliv.viewmodel.ViewModelUsuario

@Composable
fun PantallaRegistroSocio(
    viewModelUsuario: ViewModelUsuario,
    onRegistroExitoso: () -> Unit
) {
    PantallaRegistroSocioCompacta(
        viewModelUsuario = viewModelUsuario,
        onRegistroExitoso = onRegistroExitoso
    )
}