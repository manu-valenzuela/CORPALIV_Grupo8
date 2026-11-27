package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.data.model.Indicador
import cl.duoc.corpaliv.data.model.RespuestaIndicadores
import cl.duoc.corpaliv.data.remote.DonacionesApiService
import cl.duoc.corpaliv.data.remote.IndicadoresApiService
import cl.duoc.corpaliv.data.remote.NoticiasApiService
import cl.duoc.corpaliv.data.remote.VentasApiService
import cl.duoc.corpaliv.data.remote.dto.DonacionRequestDto
import cl.duoc.corpaliv.data.remote.dto.NoticiaRequestDto
import cl.duoc.corpaliv.data.remote.dto.ProductoRequestDto
import cl.duoc.corpaliv.data.remote.dto.VentaRequestDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs

// Repositorios que solo traducen sus llamadas a la API correspondiente
class RepositoriosTest : StringSpec({

    "RepositorioTienda llama a los endpoints de productos y ventas" {
        val api = mockk<VentasApiService>()
        val repositorio = RepositorioTienda(api)
        val datos = ProductoRequestDto("Agenda", "Agenda institucional", 12500, 3, null)
        coEvery { api.listarProductos() } returns listOf(Datos.agenda)
        coEvery { api.crearProducto(datos) } returns Datos.agenda
        coEvery { api.actualizarProducto(1L, datos) } returns Datos.agenda
        coEvery { api.eliminarProducto(1L) } just Runs
        coEvery { api.listarVentas() } returns listOf(Datos.venta)
        coEvery { api.registrarVenta(VentaRequestDto(1L, 2)) } returns Datos.venta

        repositorio.listarProductos() shouldBe listOf(Datos.agenda)
        repositorio.crearProducto(datos) shouldBe Datos.agenda
        repositorio.actualizarProducto(1L, datos) shouldBe Datos.agenda
        repositorio.eliminarProducto(1L)
        repositorio.listarVentas() shouldBe listOf(Datos.venta)
        repositorio.registrarVenta(1L, 2) shouldBe Datos.venta
        coVerify { api.eliminarProducto(1L) }
    }

    "RepositorioNoticias llama a los endpoints de noticias" {
        val api = mockk<NoticiasApiService>()
        val repositorio = RepositorioNoticias(api)
        val datos = NoticiaRequestDto("Campaña 2026", "Gran colecta anual", null)
        coEvery { api.listar() } returns listOf(Datos.noticia)
        coEvery { api.crear(datos) } returns Datos.noticia
        coEvery { api.actualizar(1L, datos) } returns Datos.noticia
        coEvery { api.eliminar(1L) } just Runs

        repositorio.obtenerNoticias() shouldBe listOf(Datos.noticia)
        repositorio.crear(datos) shouldBe Datos.noticia
        repositorio.actualizar(1L, datos) shouldBe Datos.noticia
        repositorio.eliminar(1L)
        coVerify { api.eliminar(1L) }
    }

    "RepositorioDonaciones arma la petición con nombre, correo y monto" {
        val api = mockk<DonacionesApiService>()
        val repositorio = RepositorioDonaciones(api)
        coEvery { api.registrar(DonacionRequestDto("Ana", "ana@correo.cl", 5000)) } returns Datos.donacion
        coEvery { api.listar() } returns listOf(Datos.donacion)
        coEvery { api.eliminar(5L) } just Runs

        repositorio.registrar("Ana", "ana@correo.cl", 5000) shouldBe Datos.donacion
        repositorio.listar() shouldBe listOf(Datos.donacion)
        repositorio.eliminar(5L)
        coVerify { api.eliminar(5L) }
    }

    "RepositorioIndicadores obtiene los indicadores de la API externa" {
        val api = mockk<IndicadoresApiService>()
        val respuesta = RespuestaIndicadores(uf = Indicador(39000.0), dolar = Indicador(950.0))
        coEvery { api.obtenerIndicadores() } returns respuesta

        RepositorioIndicadores(api).obtenerIndicadores() shouldBe respuesta
    }
})
