package cl.duoc.corpaliv.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.ui.components.TarjetaProducto
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TarjetaProductoTest {

    @get:Rule
    val reglaCompose = createComposeRule()

    private val agenda = Producto(1L, "Agenda", "Agenda institucional", 12500.0, 3, null)

    @Test
    fun muestraElStockYPermiteComprar() {
        var compras = 0
        reglaCompose.setContent {
            TarjetaProducto(
                producto = agenda, cantidad = 2,
                onSumar = {}, onRestar = {}, onConfirmarCompra = { compras++ },
                indicadores = EstadoIndicadores()
            )
        }

        reglaCompose.onNodeWithText("Quedan 3 unidades").assertIsDisplayed()
        reglaCompose.onNodeWithText("Confirmar Compra ($ 25000)").performClick()

        assertEquals(1, compras)
    }

    @Test
    fun noPermiteSumarMasDelStock() {
        reglaCompose.setContent {
            TarjetaProducto(
                producto = agenda, cantidad = 3,
                onSumar = {}, onRestar = {}, onConfirmarCompra = {},
                indicadores = EstadoIndicadores()
            )
        }

        reglaCompose.onNodeWithContentDescription("Sumar").assertIsNotEnabled()
        reglaCompose.onNodeWithContentDescription("Restar").assertIsEnabled()
    }

    @Test
    fun unProductoSinStockApareceAgotado() {
        reglaCompose.setContent {
            TarjetaProducto(
                producto = agenda.copy(stock = 0), cantidad = 1,
                onSumar = {}, onRestar = {}, onConfirmarCompra = {},
                indicadores = EstadoIndicadores()
            )
        }

        // "Agotado" aparece en el texto de stock y en el botón desactivado
        reglaCompose.onNodeWithText("Confirmar Compra ($ 12500)").assertDoesNotExist()
        reglaCompose.onNodeWithContentDescription("Sumar").assertIsNotEnabled()
    }

    @Test
    fun conLaConversionActivaMuestraElPrecioEnDolares() {
        reglaCompose.setContent {
            TarjetaProducto(
                producto = agenda, cantidad = 2,
                onSumar = {}, onRestar = {}, onConfirmarCompra = {},
                indicadores = EstadoIndicadores(valorDolar = 1000.0, valorUf = 40000.0, mostrarConversion = true)
            )
        }

        reglaCompose.onNodeWithText("≈ US$ 12,50", useUnmergedTree = true).assertExists() // precio unitario
        reglaCompose.onNodeWithText("≈ US$ 25,00", useUnmergedTree = true).assertExists() // total de 2 unidades
    }
}
