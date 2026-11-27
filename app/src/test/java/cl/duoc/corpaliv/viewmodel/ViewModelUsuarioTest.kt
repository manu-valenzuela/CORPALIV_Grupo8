package cl.duoc.corpaliv.viewmodel

import android.app.Application
import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.data.ManejadorUbicacion
import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Sesion
import cl.duoc.corpaliv.data.remote.dto.UsuarioDto
import cl.duoc.corpaliv.repository.RepositorioAuth
import cl.duoc.corpaliv.repository.RepositorioUsuarios
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.io.IOException

class ViewModelUsuarioTest : StringSpec({

    val sesionSocio = Sesion("token-socio", "ana@correo.cl", RolUsuario.Socio)
    val sesionFuncionario = Sesion("token-admin", "admin@corpaliv.cl", RolUsuario.Funcionario)

    lateinit var dispatcher: TestDispatcher
    lateinit var repoUsuarios: RepositorioUsuarios
    lateinit var repoAuth: RepositorioAuth
    lateinit var manejadorUbicacion: ManejadorUbicacion
    lateinit var sesionActual: MutableStateFlow<Sesion?>

    beforeTest {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        repoUsuarios = mockk()
        repoAuth = mockk()
        manejadorUbicacion = mockk()
        sesionActual = MutableStateFlow(null)
        every { repoAuth.sesionActual } returns sesionActual
        every { repoAuth.guardarSesion(any()) } just Runs
    }

    afterTest { Dispatchers.resetMain() }

    fun crearViewModel() =
        ViewModelUsuario(mockk<Application>(relaxed = true), repoUsuarios, repoAuth, manejadorUbicacion)

    fun ViewModelUsuario.llenarFormulario() {
        onNombreChange("Ana")
        onApellidoChange("Pérez")
        onRutChange("12.345.678-5")
        onFechaNacimientoChange("31/05/2000")
        onDireccionChange("Calle 1")
        onCorreoChange("ana@correo.cl")
        onContrasenaChange("socio123")
    }

    "un formulario completo y correcto es válido" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()

            vm.validarFormulario() shouldBe true
        }
    }

    "rechaza RUT, fecha, correo y contraseña inválidos" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()
            vm.onRutChange("12.345.678-9")
            vm.onFechaNacimientoChange("2000-05-31")
            vm.onCorreoChange("ana@")
            vm.onContrasenaChange("123")

            vm.validarFormulario() shouldBe false
            val errores = vm.formState.value.errores
            errores.rut shouldBe "RUT inválido"
            errores.fechaNacimiento shouldBe "Usa el formato DD/MM/AAAA"
            errores.correo shouldBe "Correo no válido"
            errores.contrasena shouldBe "Mínimo 6 caracteres"
        }
    }

    "el monto personalizado solo acepta dígitos y exige al menos $1.000" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()

            vm.onMontoPersonalizadoChange("5a00")
            vm.formState.value.donacionMensual shouldBe OpcionDonacionMensual.Personalizado(500)
            vm.validarFormulario() shouldBe false
            vm.formState.value.errores.donacionMensual shouldBe "El monto mínimo es $1.000"

            vm.onDonacionChange(OpcionDonacionMensual.QuinceMil)
            vm.validarFormulario() shouldBe true
        }
    }

    "alterna la visibilidad de la contraseña" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.onAlternarContrasena()
            vm.formState.value.contrasenaVisible shouldBe true
        }
    }

    "quien se registra crea su cuenta y perfil, y queda con la sesión iniciada" {
        val perfil = slot<UsuarioDto>()
        coEvery { repoAuth.registrar("ana@correo.cl", "socio123") } returns sesionSocio
        coEvery { repoUsuarios.crear(capture(perfil)) } answers { perfil.captured }
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()
            var navego = false

            vm.registrarSocio { navego = true }
            advanceUntilIdle()

            verify { repoAuth.guardarSesion(sesionSocio) }
            perfil.captured.fechaNacimiento shouldBe "2000-05-31"
            perfil.captured.montoDonacionMensual shouldBe 5000
            navego shouldBe true
            vm.formState.value.nombre shouldBe "" // el formulario se limpia
        }
    }

    "un funcionario que crea un usuario conserva su propia sesión" {
        sesionActual.value = sesionFuncionario
        coEvery { repoAuth.registrar(any(), any()) } returns sesionSocio
        coEvery { repoUsuarios.crear(any()) } answers { firstArg() }
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()

            vm.registrarSocio { }
            advanceUntilIdle()

            verify(exactly = 0) { repoAuth.guardarSesion(any()) }
        }
    }

    "si el correo ya existe muestra el mensaje del servidor" {
        coEvery { repoAuth.registrar(any(), any()) } throws Datos.errorHttp(409, "El correo ya está registrado")
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.llenarFormulario()
            var navego = false

            vm.registrarSocio { navego = true }
            advanceUntilIdle()

            vm.formState.value.errorEnvio shouldBe "El correo ya está registrado"
            navego shouldBe false
            coVerify(exactly = 0) { repoUsuarios.crear(any()) }
        }
    }

    "un formulario inválido no llama a los servicios" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.registrarSocio { }
            advanceUntilIdle()

            coVerify(exactly = 0) { repoAuth.registrar(any(), any()) }
        }
    }

    "el funcionario carga usuarios y cambia un rol" {
        val actualizado = Datos.usuario.copy(rol = RolUsuario.Funcionario)
        coEvery { repoUsuarios.listar() } returnsMany listOf(listOf(Datos.usuario), listOf(actualizado))
        coEvery { repoUsuarios.cambiarRol("ana@correo.cl", RolUsuario.Funcionario) } just Runs
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarUsuarios()
            advanceUntilIdle()
            vm.listaUsuarios.value shouldBe listOf(Datos.usuario)

            vm.cambiarRolUsuario("ana@correo.cl", RolUsuario.Funcionario)
            advanceUntilIdle()
            vm.listaUsuarios.value shouldBe listOf(actualizado)
        }
    }

    "si falla la carga de usuarios la lista queda vacía" {
        coEvery { repoUsuarios.listar() } throws IOException()
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarUsuarios()
            advanceUntilIdle()

            vm.listaUsuarios.value.shouldBeEmpty()
        }
    }

    "si falla el cambio de rol la lista no cambia" {
        coEvery { repoUsuarios.listar() } returns listOf(Datos.usuario)
        coEvery { repoUsuarios.cambiarRol(any(), any()) } throws IllegalStateException()
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.cargarUsuarios()
            advanceUntilIdle()

            vm.cambiarRolUsuario("ana@correo.cl", RolUsuario.Funcionario)
            advanceUntilIdle()
            vm.listaUsuarios.value shouldBe listOf(Datos.usuario)
        }
    }

    "la ubicación del GPS completa la dirección" {
        every { manejadorUbicacion.obtenerUbicacionActual(any(), any()) } answers {
            firstArg<(String, Double, Double) -> Unit>().invoke("Av. Siempre Viva 123", -33.4, -70.6)
        }
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.obtenerUbicacion()

            vm.formState.value.direccion shouldBe "Av. Siempre Viva 123"
            vm.formState.value.latitud shouldBe -33.4
            vm.formState.value.cargandoUbicacion shouldBe false
        }
    }

    "un error del GPS se muestra bajo la dirección" {
        every { manejadorUbicacion.obtenerUbicacionActual(any(), any()) } answers {
            secondArg<(String) -> Unit>().invoke("No se pudo obtener la ubicación actual")
        }
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.obtenerUbicacion()

            vm.formState.value.errorUbicacion shouldBe "No se pudo obtener la ubicación actual"
            vm.formState.value.cargandoUbicacion shouldBe false
        }
    }

    "permiso denegado y escritura manual de la dirección" {
        runTest(dispatcher) {
            val vm = crearViewModel()
            vm.onPermisoUbicacionDenegado()
            vm.formState.value.errorUbicacion shouldBe "Permiso de ubicación denegado"

            vm.onDireccionChange("Calle 2")
            vm.formState.value.errorUbicacion.shouldBeNull()
            vm.formState.value.direccion shouldBe "Calle 2"
        }
    }
})
