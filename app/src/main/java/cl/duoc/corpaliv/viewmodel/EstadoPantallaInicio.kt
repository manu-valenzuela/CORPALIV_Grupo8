package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Evento
import cl.duoc.corpaliv.data.model.Noticia

data class EstadoPantallaInicio(
    val cargando: Boolean = true,
    val listaNoticias: List<Noticia> = emptyList(),
    val proximoEvento: Evento? = null,
    val mensajePersistido: String = ""
)