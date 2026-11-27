package cl.duoc.corpaliv.ui.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FormatosTest : StringSpec({

    "convierte pesos a dólares con formato chileno" {
        formatoDolares(10000.0, 1000.0) shouldBe "US$ 10,00"
    }

    "convierte pesos a UF con formato chileno" {
        formatoUf(80000.0, 40000.0) shouldBe "2,00 UF"
    }

    "muestra la fecha del servidor sin la T ni los segundos" {
        formatoFecha("2026-11-27T10:15:30.123") shouldBe "2026-11-27 10:15"
    }
})
