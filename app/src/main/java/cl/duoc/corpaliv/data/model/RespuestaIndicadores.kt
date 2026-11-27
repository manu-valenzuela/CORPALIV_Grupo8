package cl.duoc.corpaliv.data.model

// Respuesta de https://mindicador.cl/api (solo los indicadores que usa la app)
data class RespuestaIndicadores(
    val uf: Indicador,
    val dolar: Indicador
)
