package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Sesion
import cl.duoc.corpaliv.repository.RepositorioAuth
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class ViewModelSesionTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repoAuth: RepositorioAuth

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repoAuth = mockk()
        every { repoAuth.sesionActual } returns MutableStateFlow<Sesion?>(null)
    }

    afterTest { Dispatchers.resetMain() }

    "sin correo o contraseña muestra un error y no llama a auth" {
        runTest(dispatcher) {
            val vm = ViewModelSesion(repoAuth)
            vm.iniciarSesion { }
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Ingresa tu correo y contraseña"
            coVerify(exactly = 0) { repoAuth.iniciarSesion(any(), any()) }
        }
    }

    "un login correcto muestra el aviso y entrega el rol" {
        coEvery { repoAuth.iniciarSesion("admin@corpaliv.cl", "admin123") } returns
                Sesion("token", "admin@corpaliv.cl", RolUsuario.Funcionario)
        runTest(dispatcher) {
            val vm = ViewModelSesion(repoAuth)
            vm.onCorreoChange(" admin@corpaliv.cl ")
            vm.onContrasenaChange("admin123")
            var rolRecibido: RolUsuario? = null

            vm.iniciarSesion { rolRecibido = it }
            runCurrent()
            vm.estado.value.loginExitoso shouldBe true

            advanceUntilIdle()
            rolRecibido shouldBe RolUsuario.Funcionario
            vm.estado.value.correo shouldBe "" // el formulario se limpia
        }
    }

    "con credenciales incorrectas muestra el mensaje de auth" {
        coEvery { repoAuth.iniciarSesion(any(), any()) } throws Datos.errorHttp(401, "Correo o contraseña incorrectos")
        runTest(dispatcher) {
            val vm = ViewModelSesion(repoAuth)
            vm.onCorreoChange("admin@corpaliv.cl")
            vm.onContrasenaChange("mala123")

            vm.iniciarSesion { }
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Correo o contraseña incorrectos"
        }
    }

    "escribir borra el error y cerrar sesión llama al repositorio" {
        every { repoAuth.cerrarSesion() } just Runs
        runTest(dispatcher) {
            val vm = ViewModelSesion(repoAuth)
            vm.iniciarSesion { }
            vm.onContrasenaChange("x")
            vm.estado.value.error.shouldBeNull()

            vm.cerrarSesion()
            verify { repoAuth.cerrarSesion() }
        }
    }
})
