package cl.duoc.corpaliv.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.ui.screens.PantallaGestionNoticiasCompacta
import cl.duoc.corpaliv.viewmodel.EstadoGestionNoticias
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PantallaGestionNoticiasTest {

    @get:Rule
    val reglaCompose = createComposeRule()

    private val noticia = Noticia(1L, "Campaña 2026", "Gran colecta anual", null, "2026-10-15")

    @Test
    fun muestraLaListaYPermiteEditarYEliminar() {
        var editada: Noticia? = null
        var eliminada: Long? = null
        reglaCompose.setContent {
            PantallaGestionNoticiasCompacta(
                estado = EstadoGestionNoticias(lista = listOf(noticia)),
                onTituloChange = {}, onResumenChange = {}, onUrlImagenChange = {},
                onGuardar = {}, onCancelarEdicion = {},
                onEditar = { editada = it },
                onEliminar = { eliminada = it }
            )
        }

        reglaCompose.onNodeWithText("Nueva noticia").assertIsDisplayed()
        reglaCompose.onNodeWithText("Campaña 2026").assertIsDisplayed()
        reglaCompose.onAllNodesWithContentDescription("Editar")[0].performClick()
        reglaCompose.onAllNodesWithContentDescription("Eliminar")[0].performClick()

        assertEquals(noticia, editada)
        assertEquals(1L, eliminada)
    }

    @Test
    fun alEditarMuestraElBotonCancelar() {
        var cancelo = false
        reglaCompose.setContent {
            PantallaGestionNoticiasCompacta(
                estado = EstadoGestionNoticias(idEditando = 1L, titulo = "Campaña 2026"),
                onTituloChange = {}, onResumenChange = {}, onUrlImagenChange = {},
                onGuardar = {}, onCancelarEdicion = { cancelo = true },
                onEditar = {}, onEliminar = {}
            )
        }

        reglaCompose.onNodeWithText("Editar noticia").assertIsDisplayed()
        reglaCompose.onNodeWithText("Cancelar").performClick()

        assertEquals(true, cancelo)
    }
}
