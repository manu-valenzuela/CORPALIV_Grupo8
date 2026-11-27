package cl.duoc.corpaliv.data.remote

import cl.duoc.corpaliv.data.remote.dto.ErrorDto
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

// Convierte un error de red o HTTP en un mensaje para mostrar en pantalla
fun mensajeDeError(e: Exception): String = when (e) {
    is HttpException -> {
        val cuerpo = e.response()?.errorBody()?.string()
        val error = try {
            Gson().fromJson(cuerpo, ErrorDto::class.java)
        } catch (ex: Exception) {
            null
        }
        error?.mensaje ?: "Error del servidor (${e.code()})"
    }
    is IOException -> "No se pudo conectar con el servidor"
    else -> "Ocurrió un error inesperado"
}
