package cl.duoc.corpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.corpaliv.repository.RepositorioIndicadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModelIndicadores(
    private val repositorio: RepositorioIndicadores = RepositorioIndicadores()
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoIndicadores())
    val estado: StateFlow<EstadoIndicadores> = _estado.asStateFlow()

    init {
        cargarIndicadores()
    }

    fun cargarIndicadores() {
        viewModelScope.launch {
            try {
                val respuesta = repositorio.obtenerIndicadores()
                _estado.update {
                    it.copy(valorDolar = respuesta.dolar.valor, valorUf = respuesta.uf.valor)
                }
            } catch (e: Exception) {
                // Sin conexión o API caída: la conversión queda no disponible
                _estado.update { it.copy(valorDolar = null, valorUf = null) }
            }
        }
    }

    fun cambiarMostrarConversion(mostrar: Boolean) {
        _estado.update { it.copy(mostrarConversion = mostrar) }
    }
}
