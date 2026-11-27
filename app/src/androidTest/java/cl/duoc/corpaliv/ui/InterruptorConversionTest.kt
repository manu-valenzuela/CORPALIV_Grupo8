package cl.duoc.corpaliv.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cl.duoc.corpaliv.ui.components.InterruptorConversion
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class InterruptorConversionTest {

    @get:Rule
    val reglaCompose = createComposeRule()

    @Test
    fun sinConexionElInterruptorQuedaDesactivado() {
        reglaCompose.setContent {
            InterruptorConversion(texto = "Ver precios en dólares", indicadores = EstadoIndicadores(), onCambiar = {})
        }

        reglaCompose.onNodeWithText("Conversión no disponible (sin conexión)").assertIsDisplayed()
        reglaCompose.onNode(isToggleable()).assertIsNotEnabled()
    }

    @Test
    fun conIndicadoresActivarloAvisaAlViewModel() {
        var valor: Boolean? = null
        reglaCompose.setContent {
            InterruptorConversion(
                texto = "Ver precios en dólares",
                indicadores = EstadoIndicadores(valorDolar = 950.0, valorUf = 39000.0),
                onCambiar = { valor = it }
            )
        }

        reglaCompose.onNodeWithText("Ver precios en dólares").assertIsDisplayed()
        reglaCompose.onNode(isToggleable()).performClick()

        assertEquals(true, valor)
    }
}
