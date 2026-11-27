package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.data.SesionActual
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.remote.AuthApiService
import cl.duoc.corpaliv.data.remote.dto.CredencialesDto
import cl.duoc.corpaliv.data.remote.dto.TokenDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class RepositorioAuthTest : StringSpec({

    lateinit var api: AuthApiService
    lateinit var repositorio: RepositorioAuth

    beforeTest {
        SesionActual.cerrar()
        api = mockk()
        repositorio = RepositorioAuth(api)
    }

    "iniciar sesión guarda la sesión con el rol recibido" {
        coEvery { api.iniciarSesion(CredencialesDto("admin@corpaliv.cl", "admin123")) } returns
                TokenDto("token-1", "admin@corpaliv.cl", "FUNCIONARIO")

        val sesion = repositorio.iniciarSesion("admin@corpaliv.cl", "admin123")

        sesion.rol shouldBe RolUsuario.Funcionario
        repositorio.sesionActual.value shouldBe sesion
    }

    "registrar devuelve la sesión pero no la guarda" {
        coEvery { api.registrar(any()) } returns TokenDto("token-2", "socio@correo.cl", "SOCIO")

        val sesion = repositorio.registrar("socio@correo.cl", "socio123")

        sesion.rol shouldBe RolUsuario.Socio
        repositorio.sesionActual.value.shouldBeNull()
    }

    "guardar y cerrar sesión" {
        coEvery { api.registrar(any()) } returns TokenDto("token-2", "socio@correo.cl", "SOCIO")
        val sesion = repositorio.registrar("socio@correo.cl", "socio123")

        repositorio.guardarSesion(sesion)
        repositorio.sesionActual.value shouldBe sesion

        repositorio.cerrarSesion()
        repositorio.sesionActual.value.shouldBeNull()
    }
})
