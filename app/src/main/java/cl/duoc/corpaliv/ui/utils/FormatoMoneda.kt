package cl.duoc.corpaliv.ui.utils

import java.util.Locale

private val LOCALE_CHILE: Locale = Locale.forLanguageTag("es-CL")

fun formatoDolares(montoPesos: Double, valorDolar: Double): String =
    String.format(LOCALE_CHILE, "US$ %,.2f", montoPesos / valorDolar)

fun formatoUf(montoPesos: Double, valorUf: Double): String =
    String.format(LOCALE_CHILE, "%,.2f UF", montoPesos / valorUf)
