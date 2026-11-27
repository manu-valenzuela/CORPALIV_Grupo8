package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.data.remote.dto.ProductoRequestDto
import cl.duoc.corpaliv.repository.RepositorioTienda
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

class ViewModelGestionProductosTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repositorio: RepositorioTienda

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repositorio = mockk()
        coEvery { repositorio.listarProductos() } returns listOf(Datos.agenda)
    }

    afterTest { Dispatchers.resetMain() }

    fun ViewModelGestionProductos.llenar() {
        onNombreChange("Agenda")
        onDescripcionChange("Agenda institucional")
        onPrecioChange("12500")
        onStockChange("3")
    }

    "carga la lista de productos" {
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.cargar()
            advanceUntilIdle()

            vm.estado.value.lista shouldBe listOf(Datos.agenda)
        }
    }

    "si falla la carga muestra el error" {
        coEvery { repositorio.listarProductos() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.cargar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "No se pudo conectar con el servidor"
        }
    }

    "precio y stock solo aceptan dígitos" {
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.onPrecioChange("12.5a00")
            vm.onStockChange("-3")

            vm.estado.value.precio shouldBe "12500"
            vm.estado.value.stock shouldBe "3"
        }
    }

    "un formulario incompleto no se envía" {
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.onNombreChange("Agenda")
            vm.guardar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Completa nombre, descripción, precio (mayor a 0) y stock"
            coVerify(exactly = 0) { repositorio.crearProducto(any()) }
        }
    }

    "un producto nuevo se crea y el formulario se limpia" {
        val datos = ProductoRequestDto("Agenda", "Agenda institucional", 12500, 3, null)
        coEvery { repositorio.crearProducto(datos) } returns Datos.agenda
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.llenar()

            vm.guardar()
            runCurrent()
            vm.estado.value.guardadoExitoso shouldBe true
            vm.estado.value.nombre shouldBe ""

            advanceUntilIdle()
            vm.estado.value.guardadoExitoso shouldBe false
            coVerify { repositorio.crearProducto(datos) }
        }
    }

    "editar carga el producto y guardar lo actualiza" {
        coEvery { repositorio.actualizarProducto(1L, any()) } returns Datos.agenda
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.editar(Datos.agenda)
            vm.estado.value.idEditando shouldBe 1L
            vm.estado.value.precio shouldBe "12500"

            vm.onUrlImagenChange("https://imagen.cl/agenda.png")
            vm.guardar()
            advanceUntilIdle()

            coVerify {
                repositorio.actualizarProducto(
                    1L, ProductoRequestDto("Agenda", "Agenda institucional", 12500, 3, "https://imagen.cl/agenda.png")
                )
            }
        }
    }

    "cancelar la edición limpia el formulario" {
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.editar(Datos.agenda)
            vm.cancelarEdicion()

            vm.estado.value.idEditando.shouldBeNull()
            vm.estado.value.nombre shouldBe ""
        }
    }

    "si el servidor rechaza el guardado muestra su mensaje" {
        coEvery { repositorio.crearProducto(any()) } throws Datos.errorHttp(403, "No tiene permiso para esta acción")
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.llenar()
            vm.guardar()
            advanceUntilIdle()

            vm.estado.value.error shouldBe "No tiene permiso para esta acción"
        }
    }

    "eliminar quita el producto y recarga la lista" {
        coEvery { repositorio.eliminarProducto(1L) } just Runs
        coEvery { repositorio.listarProductos() } returns emptyList()
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.eliminar(1L)
            advanceUntilIdle()

            vm.estado.value.lista shouldBe emptyList()
        }
    }

    "si falla la eliminación muestra el error" {
        coEvery { repositorio.eliminarProducto(1L) } throws Datos.errorHttp(404, "Producto no encontrado")
        runTest(dispatcher) {
            val vm = ViewModelGestionProductos(repositorio)
            vm.eliminar(1L)
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Producto no encontrado"
        }
    }
})
