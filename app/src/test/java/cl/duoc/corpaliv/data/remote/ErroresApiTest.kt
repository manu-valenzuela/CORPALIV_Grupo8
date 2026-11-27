package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.Datos
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErroresApiTest : StringSpec({

    "un error HTTP muestra el mensaje que envía el microservicio" {
        mensajeDeError(Datos.errorHttp(409, "El correo ya está registrado")) shouldBe "El correo ya está registrado"
    }

    "un error HTTP sin cuerpo JSON muestra el código" {
        val error = HttpException(Response.error<Any>(500, "".toResponseBody("text/plain".toMediaType())))
        mensajeDeError(error) shouldBe "Error del servidor (500)"
    }

    "sin conexión avisa que no se pudo conectar" {
        mensajeDeError(IOException("timeout")) shouldBe "No se pudo conectar con el servidor"
    }

    "cualquier otro error muestra un mensaje genérico" {
        mensajeDeError(IllegalStateException()) shouldBe "Ocurrió un error inesperado"
    }
})
