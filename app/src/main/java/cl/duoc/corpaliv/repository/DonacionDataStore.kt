package cl.duoc.corpaliv.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.duoc.corpaliv.model.DonacionExpress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "preferencias_donacion")

class DonacionDataStore(private val context: Context) {

    private val KEY_DONACIONES = stringPreferencesKey("lista_donaciones_express_v1")
    private val ULTIMO_DONANTE = stringPreferencesKey("ultimo_donante")
    private val ULTIMO_MONTO = stringPreferencesKey("ultimo_monto")

    fun obtenerDonaciones(): Flow<List<DonacionExpress>> {
        return context.dataStore.data.map { prefs ->
            val rawData = prefs[KEY_DONACIONES] ?: ""
            if (rawData.isBlank()) {
                emptyList()
            } else {
                rawData.split("||").mapNotNull { registro ->
                    val partes = registro.split(";")
                    if (partes.size >= 5) {
                        DonacionExpress(
                            id = partes[0],
                            nombre = partes[1],
                            correo = partes[2],
                            monto = partes[3].toDoubleOrNull() ?: 0.0,
                            fecha = partes[4]
                        )
                    } else null
                }
            }
        }
    }

    suspend fun guardarDonacion(donacion: DonacionExpress) {
        context.dataStore.edit { prefs ->
            val listaActual = (prefs[KEY_DONACIONES] ?: "").split("||").filter { it.isNotBlank() }.toMutableList()
            val nuevoRegistro = "${donacion.id};${donacion.nombre};${donacion.correo};${donacion.monto};${donacion.fecha}"
            listaActual.add(0, nuevoRegistro)
            prefs[KEY_DONACIONES] = listaActual.joinToString("||")
            prefs[ULTIMO_DONANTE] = donacion.nombre
            prefs[ULTIMO_MONTO] = donacion.monto.toInt().toString()
        }
    }

    fun obtenerUltimaDonacion(): Flow<String> {
        return context.dataStore.data.map { preferencias ->
            val nombre = preferencias[ULTIMO_DONANTE] ?: ""
            val monto = preferencias[ULTIMO_MONTO] ?: ""
            if (nombre.isNotBlank()) "Último aporte registrado: $$monto ($nombre)" else ""
        }
    }
}