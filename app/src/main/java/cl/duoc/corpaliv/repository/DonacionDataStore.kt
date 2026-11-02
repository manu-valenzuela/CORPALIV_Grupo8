package cl.duoc.corpaliv.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Declaramos el DataStore con nombre "preferencias_donacion" (Guía 12)
val Context.dataStore by preferencesDataStore(name = "preferencias_donacion")

class DonacionDataStore(private val context: Context) {

    // Clave usada para guardar la información (Guía 12)[cite: 75]
    private val ULTIMO_DONANTE = stringPreferencesKey("ultimo_donante")
    private val ULTIMO_MONTO = stringPreferencesKey("ultimo_monto")

    // Función para guardar en DataStore (Guía 12)[cite: 75]
    suspend fun guardarDonacion(nombre: String, monto: String) {
        context.dataStore.edit { preferencias ->
            preferencias[ULTIMO_DONANTE] = nombre
            preferencias[ULTIMO_MONTO] = monto
        }
    }

    // Función para obtener los datos como Flow reactivo (Guía 12)[cite: 75]
    fun obtenerUltimaDonacion(): Flow<String> {
        return context.dataStore.data.map { preferencias ->
            val nombre = preferencias[ULTIMO_DONANTE] ?: ""
            val monto = preferencias[ULTIMO_MONTO] ?: ""
            if (nombre.isNotBlank()) "Último aporte registrado: $$monto ($nombre)" else ""
        }
    }
}