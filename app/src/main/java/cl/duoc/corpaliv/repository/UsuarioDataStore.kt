package cl.duoc.corpaliv.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsuarioDataStore(private val context: Context) {

    private val KEY_USUARIOS = stringPreferencesKey("lista_usuarios_v1")

    // Retorna la lista de usuarios estructurados desde DataStore Preferences
    fun obtenerUsuarios(): Flow<List<cl.duoc.corpaliv.model.Usuario>> {
        return context.dataStore.data.map { prefs ->
            val rawData = prefs[KEY_USUARIOS] ?: ""
            if (rawData.isBlank()) {
                emptyList()
            } else {
                rawData.split("||").mapNotNull { registro ->
                    val partes = registro.split(";")
                    if (partes.size == 4) {
                        cl.duoc.corpaliv.model.Usuario(
                            id = partes[0],
                            nombre = partes[1],
                            correo = partes[2],
                            rol = cl.duoc.corpaliv.model.RolUsuario.valueOf(partes[3])
                        )
                    } else null
                }
            }
        }
    }

    // Serializa y guarda un nuevo usuario
    suspend fun guardarUsuario(usuario: cl.duoc.corpaliv.model.Usuario) {
        context.dataStore.edit { prefs ->
            val listaActual = (prefs[KEY_USUARIOS] ?: "").split("||").filter { it.isNotBlank() }.toMutableList()
            val nuevoRegistro = "${usuario.id};${usuario.nombre};${usuario.correo};${usuario.rol.name}"
            listaActual.add(nuevoRegistro)
            prefs[KEY_USUARIOS] = listaActual.joinToString("||")
        }
    }

    // Actualiza el rol de un usuario existente por su ID
    suspend fun actualizarRolUsuario(idUsuario: String, nuevoRol: cl.duoc.corpaliv.model.RolUsuario) {
        context.dataStore.edit { prefs ->
            val listaRaw = prefs[KEY_USUARIOS] ?: ""
            if (listaRaw.isNotBlank()) {
                val registros = listaRaw.split("||").mapNotNull { registro ->
                    val partes = registro.split(";")
                    if (partes.size == 4) {
                        if (partes[0] == idUsuario) {
                            "${partes[0]};${partes[1]};${partes[2]};${nuevoRol.name}"
                        } else {
                            registro
                        }
                    } else null
                }
                prefs[KEY_USUARIOS] = registros.joinToString("||")
            }
        }
    }
}