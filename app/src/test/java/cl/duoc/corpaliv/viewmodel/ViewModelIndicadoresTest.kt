package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Indicador
import cl.duoc.corpaliv.data.model.RespuestaIndicadores
import cl.duoc.corpaliv.repository.RepositorioIndicadores
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.io.IOException

class ViewModelIndicadoresTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repositorio: RepositorioIndicadores

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repositorio = mockk()
    }

    afterTest { Dispatchers.resetMain() }

    "carga el dólar y la UF desde la API externa" {
        coEvery { repositorio.obtenerIndicadores() } returns
                RespuestaIndicadores(uf = Indicador(39000.0), dolar = Indicador(950.0))
        runTest(dispatcher) {
            val vm = ViewModelIndicadores(repositorio)
            advanceUntilIdle()

            vm.estado.value.valorDolar shouldBe 950.0
            vm.estado.value.valorUf shouldBe 39000.0
            vm.estado.value.disponible shouldBe true
        }
    }

    "sin conexión la conversión queda no disponible" {
        coEvery { repositorio.obtenerIndicadores() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelIndicadores(repositorio)
            advanceUntilIdle()

            vm.estado.value.disponible shouldBe false
        }
    }

    "el interruptor cambia la opción de mostrar la conversión" {
        coEvery { repositorio.obtenerIndicadores() } throws IOException()
        runTest(dispatcher) {
            val vm = ViewModelIndicadores(repositorio)
            vm.cambiarMostrarConversion(true)

            vm.estado.value.mostrarConversion shouldBe true
        }
    }
})
