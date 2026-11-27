package cl.duoc.corpaliv.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import cl.duoc.corpaliv.ui.components.AvisoExito
import org.junit.Rule
import org.junit.Test

class AvisoExitoTest {

    @get:Rule
    val reglaCompose = createComposeRule()

    @Test
    fun muestraElMensajeCuandoEsVisible() {
        reglaCompose.setContent {
            AvisoExito(visible = true, mensaje = "¡Compra realizada con éxito!")
        }

        reglaCompose.onNodeWithText("¡Compra realizada con éxito!").assertIsDisplayed()
    }

    @Test
    fun noMuestraNadaCuandoNoEsVisible() {
        reglaCompose.setContent {
            AvisoExito(visible = false, mensaje = "¡Compra realizada con éxito!")
        }

        reglaCompose.onNodeWithText("¡Compra realizada con éxito!").assertDoesNotExist()
    }
}
