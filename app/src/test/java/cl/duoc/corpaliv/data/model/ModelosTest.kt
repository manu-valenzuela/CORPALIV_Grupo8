package cl.duoc.corpaliv.data.model

import cl.duoc.corpaliv.Datos
import cl.duoc.corpaliv.viewmodel.EstadoIndicadores
import cl.duoc.corpaliv.viewmodel.EstadoPantallaTienda
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ModelosTest : StringSpec({

    "RolUsuario.desdeCodigo convierte el código que envía auth" {
        RolUsuario.desdeCodigo("FUNCIONARIO") shouldBe RolUsuario.Funcionario
        RolUsuario.desdeCodigo("SOCIO") shouldBe RolUsuario.Socio
        RolUsuario.desdeCodigo("DESCONOCIDO") shouldBe RolUsuario.Socio
    }

    "OpcionDonacionMensual.desdeMonto reconoce los montos fijos y el personalizado" {
        OpcionDonacionMensual.desdeMonto(5000) shouldBe OpcionDonacionMensual.CincoMil
        OpcionDonacionMensual.desdeMonto(15000) shouldBe OpcionDonacionMensual.QuinceMil
        OpcionDonacionMensual.desdeMonto(25000) shouldBe OpcionDonacionMensual.VeinticincoMil
        OpcionDonacionMensual.desdeMonto(7000) shouldBe OpcionDonacionMensual.Personalizado(7000)
    }

    "la venta calcula su precio total" {
        Datos.venta.precioTotal shouldBe 25000.0
    }

    "la tienda usa cantidad 1 para un producto no modificado" {
        val estado = EstadoPantallaTienda(cantidades = mapOf(1L to 3))
        estado.cantidadDe(1L) shouldBe 3
        estado.cantidadDe(2L) shouldBe 1
    }

    "la conversión solo está disponible si hay valor del dólar y de la UF" {
        EstadoIndicadores(valorDolar = 950.0, valorUf = 39000.0).disponible shouldBe true
        EstadoIndicadores(valorDolar = 950.0).disponible shouldBe false
        EstadoIndicadores().disponible shouldBe false
    }
})
