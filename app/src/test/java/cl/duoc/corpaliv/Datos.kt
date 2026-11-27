package cl.duoc.corpaliv

import cl.duoc.corpaliv.data.model.DonacionExpress
import cl.duoc.corpaliv.data.model.Noticia
import cl.duoc.corpaliv.data.model.OpcionDonacionMensual
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.model.RolUsuario
import cl.duoc.corpaliv.data.model.Usuario
import cl.duoc.corpaliv.data.model.Venta
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

// Datos de ejemplo y utilidades compartidas por las pruebas
object Datos {

    val noticia = Noticia(1L, "Campaña 2026", "Gran colecta anual", null, "2026-10-15")

    val agenda = Producto(1L, "Agenda", "Agenda institucional", 12500.0, 3, null)
    val tazon = Producto(2L, "Tazón", "Tazón solidario", 6500.0, 0, null)

    val venta = Venta(10L, 1L, "Agenda", 2, 12500.0, "2026-11-27T10:15:00")

    val donacion = DonacionExpress(5L, "Ana", "ana@correo.cl", 5000.0, "2026-11-27T10:15:00")

    val usuario = Usuario(
        id = 1L, nombre = "Ana", apellido = "Pérez", rut = "12.345.678-5",
        fechaNacimiento = "2000-05-31", direccion = "Calle 1",
        donacionMensual = OpcionDonacionMensual.CincoMil, correo = "ana@correo.cl",
        rol = RolUsuario.Socio
    )

    // Simula la respuesta de error de un microservicio: {"estado": ..., "mensaje": ...}
    fun errorHttp(codigo: Int, mensaje: String): HttpException {
        val cuerpo = """{"estado":$codigo,"mensaje":"$mensaje"}"""
            .toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(codigo, cuerpo))
    }
}
