package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.repository.RepositorioDonaciones
import cl.duoc.corpaliv.repository.RepositorioNoticias
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
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

class ViewModelInicioTest : StringSpec({

    lateinit var dispatcher: TestDispatcher
    lateinit var repoNoticias: RepositorioNoticias
    lateinit var repoDonaciones: RepositorioDonaciones

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repoNoticias = mockk()
        repoDonaciones = mockk()
        coEvery { repoNoticias.obtenerNoticias() } returns listOf(Datos.noticia)
    }

    afterTest { Dispatchers.resetMain() }

    fun crearViewModel() = ViewModelInicio(repoNoticias, repoDonaciones)

    fun ViewModelInicio.llenarFormulario(monto: String = "5000") {
        onNombreChange("Ana")
        onCorreoChange("ana@correo.cl")
        onMontoChange(monto)
    }

    "carga las noticias al iniciar" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            advanceUntilIdle()

            vm.estado.value.listaNoticias shouldBe listOf(Datos.noticia)
            vm.estado.value.cargando shouldBe false
        }
    }

    "muestra un error si no puede cargar las noticias" {
        coEvery { repoNoticias.obtenerNoticias() } throws IOException()
        runTest(dispatcher) {
            val vm = crearViewModel()
            advanceUntilIdle()

            vm.estado.value.errorNoticias shouldBe "No se pudo conectar con el servidor"
        }
    }

    "el formulario vacío marca todos los errores" {
        runTest(dispatcher) {
            val vm = crearViewModel()

            vm.validarFormulario() shouldBe false
            vm.formState.value.errores.nombre.shouldNotBeNull()
            vm.formState.value.errores.correo.shouldNotBeNull()
            vm.formState.value.errores.monto shouldBe "Campo obligatorio"
        }
    }

    "rechaza montos de cero o mayores a un millón" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario(monto = "0")
            vm.validarFormulario() shouldBe false
            vm.formState.value.errores.monto shouldBe "El monto debe ser mayor a $0"

            vm.onMontoChange("2000000")
            vm.validarFormulario() shouldBe false
            vm.formState.value.errores.monto shouldBe "El monto máximo permitido es $1.000.000"
        }
    }

    "escribir en un campo borra su error" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.validarFormulario()
            vm.onNombreChange("Ana")

            vm.formState.value.errores.nombre.shouldBeNull()
        }
    }

    "una donación válida se registra, muestra el aviso y luego navega" {
        coEvery { repoDonaciones.registrar("Ana", "ana@correo.cl", 5000) } returns Datos.donacion
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()
            var navego = false

            vm.realizarDonacion { navego = true }
            runCurrent()
            vm.formState.value.donacionExitosa shouldBe true

            advanceUntilIdle()
            vm.formState.value.donacionExitosa shouldBe false
            navego shouldBe true
        }
    }

    "una donación inválida no llama al servidor" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.realizarDonacion { }
            advanceUntilIdle()

            coVerify(exactly = 0) { repoDonaciones.registrar(any(), any(), any()) }
        }
    }

    "si el servidor rechaza la donación muestra su mensaje" {
        coEvery { repoDonaciones.registrar(any(), any(), any()) } throws Datos.errorHttp(400, "El correo no es válido")
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()
            var navego = false

            vm.realizarDonacion { navego = true }
            advanceUntilIdle()

            vm.formState.value.errorEnvio shouldBe "El correo no es válido"
            navego shouldBe false
        }
    }

    "el funcionario carga y elimina donaciones" {
        coEvery { repoDonaciones.listar() } returnsMany listOf(listOf(Datos.donacion), emptyList())
        coEvery { repoDonaciones.eliminar(5L) } just Runs
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarDonaciones()
            advanceUntilIdle()
            vm.listaDonaciones.value shouldBe listOf(Datos.donacion)

            vm.eliminarDonacion(5L)
            advanceUntilIdle()
            vm.listaDonaciones.value.shouldBeEmpty()
        }
    }

    "si falla la carga de donaciones la lista queda vacía" {
        coEvery { repoDonaciones.listar() } throws IOException()
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarDonaciones()
            advanceUntilIdle()

            vm.listaDonaciones.value.shouldBeEmpty()
        }
    }

    "si falla la eliminación la lista no cambia" {
        coEvery { repoDonaciones.listar() } returns listOf(Datos.donacion)
        coEvery { repoDonaciones.eliminar(5L) } throws Datos.errorHttp(403, "No tiene permiso para esta acción")
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarDonaciones()
            advanceUntilIdle()

            vm.eliminarDonacion(5L)
            advanceUntilIdle()
            vm.listaDonaciones.value shouldBe listOf(Datos.donacion)
        }
    }
})
