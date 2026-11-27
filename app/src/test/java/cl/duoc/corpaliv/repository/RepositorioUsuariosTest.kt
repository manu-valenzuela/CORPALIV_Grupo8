package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.remote.AuthApiService
import cl.duoc.corpaliv.data.remote.UsuariosApiService
import cl.duoc.corpaliv.data.remote.dto.CambioRolDto
import cl.duoc.corpaliv.data.remote.dto.CuentaDto
import cl.duoc.corpaliv.data.remote.dto.UsuarioDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class RepositorioUsuariosTest : StringSpec({

    val perfil = UsuarioDto(1L, "Ana", "Pérez", "12.345.678-5", "2000-05-31", "Calle 1", null, null, 15000, "ana@correo.cl")
    val perfilSinCuenta = perfil.copy(id = 2L, correo = "otro@correo.cl", montoDonacionMensual = 7000)

    lateinit var usuariosApi: UsuariosApiService
    lateinit var authApi: AuthApiService
    lateinit var repositorio: RepositorioUsuarios

    beforeTest {
        usuariosApi = mockk()
        authApi = mockk()
        repositorio = RepositorioUsuarios(usuariosApi, authApi)
    }

    "une el perfil (usuarios) con el rol (auth) por correo" {
        coEvery { usuariosApi.listar() } returns listOf(perfil, perfilSinCuenta)
        coEvery { authApi.listarCuentas() } returns listOf(CuentaDto(7L, "ana@correo.cl", "FUNCIONARIO"))

        val usuarios = repositorio.listar()

        usuarios shouldHaveSize 2
        usuarios[0].rol shouldBe RolUsuario.Funcionario
        usuarios[0].donacionMensual shouldBe OpcionDonacionMensual.QuinceMil
        // Sin cuenta en auth queda como socio
        usuarios[1].rol shouldBe RolUsuario.Socio
        usuarios[1].donacionMensual shouldBe OpcionDonacionMensual.Personalizado(7000)
    }

    "cambia el rol buscando la cuenta por correo" {
        coEvery { authApi.listarCuentas() } returns listOf(CuentaDto(7L, "ana@correo.cl", "SOCIO"))
        coEvery { authApi.cambiarRol(7L, any()) } returns CuentaDto(7L, "ana@correo.cl", "FUNCIONARIO")

        repositorio.cambiarRol("ana@correo.cl", RolUsuario.Funcionario)

        coVerify { authApi.cambiarRol(7L, CambioRolDto("FUNCIONARIO")) }
    }

    "falla si el usuario no tiene cuenta" {
        coEvery { authApi.listarCuentas() } returns emptyList()

        shouldThrow<IllegalStateException> {
            repositorio.cambiarRol("nadie@correo.cl", RolUsuario.Funcionario)
        }
    }

    "crea el perfil en el microservicio" {
        coEvery { usuariosApi.crear(perfil) } returns perfil

        repositorio.crear(perfil) shouldBe perfil
    }
})
