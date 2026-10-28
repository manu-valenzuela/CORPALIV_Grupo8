package cl.duoc.corpaliv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definición de colores institucionales
val AzulPrincipal = Color(0xFF0052CC)
val AzulSecundario = Color(0xFF0066FF)
val FondoClaro = Color(0xFFF8F9FA)
val SuperficieClara = Color(0xFFFFFFFF)

val AzulOscuroPrincipal = Color(0xFF4C9AFF)
val AzulOscuroSecundario = Color(0xFF82B1FF)
val FondoOscuro = Color(0xFF121212)
val SuperficieOscura = Color(0xFF1E1E1E)

private val EsquemaColoresClaro = lightColorScheme(
    primary = AzulPrincipal,
    secondary = AzulSecundario,
    background = FondoClaro,
    surface = SuperficieClara,
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val EsquemaColoresOscuro = darkColorScheme(
    primary = AzulOscuroPrincipal,
    secondary = AzulOscuroSecundario,
    background = FondoOscuro,
    surface = SuperficieOscura,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

@Composable
fun TemaCorpaliv(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    contenido: @Composable () -> Unit
) {
    val colores = if (modoOscuro) EsquemaColoresOscuro else EsquemaColoresClaro

    MaterialTheme(
        colorScheme = colores,
        content = contenido
    )
}