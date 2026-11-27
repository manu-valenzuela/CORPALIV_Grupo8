package cl.duoc.corpaliv.viewmodel

import cl.duoc.corpaliv.data.model.Producto

// Lista de productos + formulario para crear o editar uno (panel del funcionario)
data class EstadoGestionProductos(
    val lista: List<Producto> = emptyList(),
    val idEditando: Long? = null, // null = se está creando un producto nuevo
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stock: String = "",
    val urlImagen: String = "",
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)
