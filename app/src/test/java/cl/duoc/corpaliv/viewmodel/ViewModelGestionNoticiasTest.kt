package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.data.remote.dto.NoticiaRequestDto
import cl.duoc.corpaliv.repository.RepositorioNoticias
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.io.IOException

class ViewModelGestionNoticiasTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repositorio: RepositorioNoticias

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repositorio = mockk()
        coEvery { repositorio.obtenerNoticias() } returns listOf(Datos.noticia)
    }

    afterTest { Dispatchers.resetMain() }

    "carga la lista de noticias" {
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.cargar()
            advanceUntilIdle()

            vm.estado.value.lista shouldBe listOf(Datos.noticia)
        }
    }

    "si falla la carga muestra el error" {
        coEvery { repositorio.obtenerNoticias() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.cargar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "No se pudo conectar con el servidor"
        }
    }

    "sin título o resumen no se envía" {
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.onTituloChange("Campaña 2026")
            vm.guardar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Completa el título y el resumen"
            coVerify(exactly = 0) { repositorio.crear(any()) }
        }
    }

    "una noticia nueva se crea y el formulario se limpia" {
        val datos = NoticiaRequestDto("Campaña 2026", "Gran colecta anual", null)
        coEvery { repositorio.crear(datos) } returns Datos.noticia
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.onTituloChange("Campaña 2026")
            vm.onResumenChange("Gran colecta anual")

            vm.guardar()
            runCurrent()
            vm.estado.value.guardadoExitoso shouldBe true
            vm.estado.value.titulo shouldBe ""

            advanceUntilIdle()
            vm.estado.value.guardadoExitoso shouldBe false
            coVerify { repositorio.crear(datos) }
        }
    }

    "editar carga la noticia y guardar la actualiza" {
        coEvery { repositorio.actualizar(1L, any()) } returns Datos.noticia
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.editar(Datos.noticia)
            vm.onUrlImagenChange("https://imagen.cl/n.png")
            vm.guardar()
            advanceUntilIdle()

            coVerify {
                repositorio.actualizar(1L, NoticiaRequestDto("Campaña 2026", "Gran colecta anual", "https://imagen.cl/n.png"))
            }
        }
    }

    "cancelar la edición limpia el formulario" {
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.editar(Datos.noticia)
            vm.cancelarEdicion()

            vm.estado.value.idEditando.shouldBeNull()
            vm.estado.value.titulo shouldBe ""
        }
    }

    "si el servidor rechaza el guardado muestra su mensaje" {
        coEvery { repositorio.crear(any()) } throws Datos.errorHttp(401, "Debe iniciar sesión")
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.onTituloChange("Campaña 2026")
            vm.onResumenChange("Gran colecta anual")
            vm.guardar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Debe iniciar sesión"
        }
    }

    "eliminar quita la noticia, y si falla muestra el error" {
        coEvery { repositorio.eliminar(1L) } just Runs
        coEvery { repositorio.eliminar(2L) } throws Datos.errorHttp(404, "Noticia no encontrada")
        coEvery { repositorio.obtenerNoticias() } returns emptyList()
        runTest(dispatcher) {
            val vm = ViewModelGestionNoticias(repositorio)
            vm.eliminar(1L)
            advanceUntilIdle()
            vm.estado.value.lista shouldBe emptyList()

            vm.eliminar(2L)
            advanceUntilIdle()
            vm.estado.value.error shouldBe "Noticia no encontrada"
        }
    }
})
