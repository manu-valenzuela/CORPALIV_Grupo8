package cl.duoc.corpaliv.repository

import cl.duoc.corpaliv.model.Producto
import cl.duoc.corpaliv.model.Venta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RepositorioTienda {

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    fun obtenerProductos(): List<Producto> {
        return listOf(
            Producto(
                id = "p1",
                nombre = "Agenda Institucional 2026",
                descripcion = "Agenda encuadernada con ilustraciones creadas por los estudiantes.",
                precio = 12500.0,
                urlImagen = ""
            ),
            Producto(
                id = "p2",
                nombre = "Tazón Solidario CORPALIV",
                descripcion = "Tazón cerámico de 350 ml con diseño exclusivo.",
                precio = 6500.0,
                urlImagen = ""
            ),
            Producto(
                id = "p3",
                nombre = "Bolsa Ecológica de Tela",
                descripcion = "Bolsa de algodón orgánico reutilizable.",
                precio = 4500.0,
                urlImagen = ""
            )
        )
    }

    fun registrarVenta(producto: Producto, cantidad: Int): Venta {
        val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val precioTotal = producto.precio * cantidad

        // Genera un ID para venta con la hora actual
        val idVenta = "V-${System.currentTimeMillis()}"

        val nuevaVenta = Venta(
            id = idVenta,
            productoId = producto.id,
            nombreProducto = producto.nombre,
            cantidad = cantidad,
            precioUnitario = producto.precio,
            precioTotal = precioTotal,
            fecha = fechaActual
        )

        _ventas.value = listOf(nuevaVenta) + _ventas.value
        return nuevaVenta
    }
}