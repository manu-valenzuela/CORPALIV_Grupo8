package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Noticia

// Lista de noticias + formulario para crear o editar una (panel del funcionario)
data class EstadoGestionNoticias(
    val lista: List<Noticia> = emptyList(),
    val idEditando: Long? = null, // null = se está creando una noticia nueva
    val titulo: String = "",
    val resumen: String = "",
    val urlImagen: String = "",
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)
