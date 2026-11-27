package cl.duoc.corpaliv.ui.utils

// "2026-11-27T10:15:00" -> "2026-11-27 10:15"
fun formatoFecha(fecha: String): String = fecha.take(16).replace('T', ' ')
