package cl.duoc.corpaliv.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import cl.duoc.corpaliv.ui.screens.PantallaLoginCompacta
import cl.duoc.corpaliv.viewmodel.EstadoLogin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PantallaLoginTest {

    @get:Rule
    val reglaCompose = createComposeRule()

    @Test
    fun escribirElCorreoYPresionarIngresarAvisaAlViewModel() {
        var correoEscrito = ""
        var presionoIngresar = false
        reglaCompose.setContent {
            PantallaLoginCompacta(
                estado = EstadoLogin(),
                onCorreoChange = { correoEscrito = it },
                onContrasenaChange = {},
                onIniciarSesion = { presionoIngresar = true },
                onCrearCuenta = {}
            )
        }

        reglaCompose.onNodeWithText("Correo Electrónico").performTextInput("admin@corpaliv.cl")
        reglaCompose.onNodeWithText("Iniciar Sesión").performClick()

        assertEquals("admin@corpaliv.cl", correoEscrito)
        assertTrue(presionoIngresar)
    }

    @Test
    fun muestraElErrorRecibidoEnElEstado() {
        reglaCompose.setContent {
            PantallaLoginCompacta(
                estado = EstadoLogin(error = "Correo o contraseña incorrectos"),
                onCorreoChange = {}, onContrasenaChange = {}, onIniciarSesion = {}, onCrearCuenta = {}
            )
        }

        reglaCompose.onNodeWithText("Correo o contraseña incorrectos").assertIsDisplayed()
    }

    @Test
    fun trasIniciarSesionMuestraElAvisoYDesactivaElBoton() {
        reglaCompose.setContent {
            PantallaLoginCompacta(
                estado = EstadoLogin(loginExitoso = true),
                onCorreoChange = {}, onContrasenaChange = {}, onIniciarSesion = {}, onCrearCuenta = {}
            )
        }

        reglaCompose.onNodeWithText("¡Sesión iniciada con éxito!").assertIsDisplayed()
        reglaCompose.onNodeWithText("Iniciar Sesión").assertIsNotEnabled()
    }

    @Test
    fun elEnlaceDeRegistroAvisaAlViewModel() {
        var irARegistro = false
        reglaCompose.setContent {
            PantallaLoginCompacta(
                estado = EstadoLogin(),
                onCorreoChange = {}, onContrasenaChange = {}, onIniciarSesion = {},
                onCrearCuenta = { irARegistro = true }
            )
        }

        reglaCompose.onNodeWithText("¿No tienes cuenta? Regístrate").performClick()

        assertTrue(irARegistro)
    }
}
