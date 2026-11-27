package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.repository.RepositorioTienda
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
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

class ViewModelTiendaTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repositorio: RepositorioTienda

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repositorio = mockk()
        coEvery { repositorio.listarProductos() } returns listOf(Datos.agenda, Datos.tazon)
    }

    afterTest { Dispatchers.resetMain() }

    "carga los productos al iniciar" {
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()

            vm.estado.value.listaProductos shouldBe listOf(Datos.agenda, Datos.tazon)
        }
    }

    "muestra un error si no puede cargar los productos" {
        coEvery { repositorio.listarProductos() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()

            vm.estado.value.error shouldBe "No se pudo conectar con el servidor"
        }
    }

    "no permite elegir más unidades que el stock" {
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()

            repeat(5) { vm.sumarCantidad(1L) } // la agenda tiene stock 3
            vm.estado.value.cantidadDe(1L) shouldBe 3

            vm.sumarCantidad(2L) // el tazón está agotado
            vm.estado.value.cantidadDe(2L) shouldBe 1
        }
    }

    "la cantidad no baja de 1" {
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()
            vm.sumarCantidad(1L)

            vm.restarCantidad(1L)
            vm.restarCantidad(1L)
            vm.estado.value.cantidadDe(1L) shouldBe 1
        }
    }

    "una compra registra la venta, recarga el stock y vuelve la cantidad a 1" {
        val agendaActualizada = Datos.agenda.copy(stock = 1)
        coEvery { repositorio.registrarVenta(1L, 2) } returns Datos.venta
        coEvery { repositorio.listarProductos() } returnsMany listOf(
            listOf(Datos.agenda, Datos.tazon),
            listOf(agendaActualizada, Datos.tazon)
        )
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()
            vm.sumarCantidad(1L)

            vm.realizarCompraDirecta(Datos.agenda)
            runCurrent()
            vm.estado.value.compraExitosa shouldBe true
            vm.estado.value.cantidadDe(1L) shouldBe 1
            vm.estado.value.listaProductos.first().stock shouldBe 1

            advanceUntilIdle()
            vm.estado.value.compraExitosa shouldBe false
            coVerify { repositorio.registrarVenta(1L, 2) }
        }
    }

    "si no hay stock suficiente muestra el mensaje del servidor" {
        coEvery { repositorio.registrarVenta(any(), any()) } throws Datos.errorHttp(409, "Stock insuficiente: quedan 0 unidades")
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            advanceUntilIdle()

            vm.realizarCompraDirecta(Datos.agenda)
            advanceUntilIdle()

            vm.estado.value.error shouldBe "Stock insuficiente: quedan 0 unidades"
            vm.estado.value.compraExitosa shouldBe false
        }
    }

    "el funcionario carga las ventas" {
        coEvery { repositorio.listarVentas() } returns listOf(Datos.venta)
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            vm.cargarVentas()
            advanceUntilIdle()

            vm.estado.value.listaVentas shouldBe listOf(Datos.venta)
        }
    }

    "si falla la carga de ventas la lista queda vacía" {
        coEvery { repositorio.listarVentas() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelTienda(repositorio)
            vm.cargarVentas()
            advanceUntilIdle()

            vm.estado.value.listaVentas.shouldBeEmpty()
        }
    }
})
