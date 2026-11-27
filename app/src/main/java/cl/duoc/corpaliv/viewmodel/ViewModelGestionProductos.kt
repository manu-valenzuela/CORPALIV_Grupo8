package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.data.model.Producto
import cl.duoc.corpaliv.data.remote.dto.ProductoRequestDto
import cl.duoc.corpaliv.data.remote.mensajeDeError
import cl.duoc.corpaliv.repository.RepositorioTienda
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelGestionProductos(
    private val repositorio: RepositorioTienda = RepositorioTienda()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoGestionProductos())
    val estado: StateFlow<EstadoGestionProductos> = _estado.asStateFlow()

    fun cargar() {
        viewModelScope.launch {
            try {
                _estado.update { it.copy(lista = repositorio.listarProductos()) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    fun onNombreChange(valor: String) = _estado.update { it.copy(nombre = valor, error = null) }

    fun onDescripcionChange(valor: String) = _estado.update { it.copy(descripcion = valor, error = null) }

    fun onPrecioChange(valor: String) = _estado.update { it.copy(precio = valor.filter { c -> c.isDigit() }, error = null) }

    fun onStockChange(valor: String) = _estado.update { it.copy(stock = valor.filter { c -> c.isDigit() }, error = null) }

    fun onUrlImagenChange(valor: String) = _estado.update { it.copy(urlImagen = valor, error = null) }

    // Carga el producto en el formulario para editarlo
    fun editar(producto: Producto) {
        _estado.update {
            it.copy(
                idEditando = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio.toInt().toString(),
                stock = producto.stock.toString(),
                urlImagen = producto.urlImagen ?: "",
                error = null
            )
        }
    }

    fun cancelarEdicion() {
        _estado.update { EstadoGestionProductos(lista = it.lista) }
    }

    fun guardar() {
        val datos = _estado.value
        val precio = datos.precio.toIntOrNull()
        val stock = datos.stock.toIntOrNull()
        if (datos.nombre.isBlank() || datos.descripcion.isBlank() || precio == null || precio <= 0 || stock == null) {
            _estado.update { it.copy(error = "Completa nombre, descripción, precio (mayor a 0) y stock") }
            return
        }

        viewModelScope.launch {
            try {
                val producto = ProductoRequestDto(
                    nombre = datos.nombre.trim(),
                    descripcion = datos.descripcion.trim(),
                    precio = precio,
                    stock = stock,
                    urlImagen = datos.urlImagen.trim().ifBlank { null }
                )
                val id = datos.idEditando
                if (id == null) repositorio.crearProducto(producto) else repositorio.actualizarProducto(id, producto)

                _estado.value = EstadoGestionProductos(lista = repositorio.listarProductos(), guardadoExitoso = true)
                delay(2000)
                _estado.update { it.copy(guardadoExitoso = false) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            try {
                repositorio.eliminarProducto(id)
                _estado.update { it.copy(lista = repositorio.listarProductos()) }
            } catch (e: Exception) {
                _estado.update { it.copy(error = mensajeDeError(e)) }
            }
        }
    }
}
