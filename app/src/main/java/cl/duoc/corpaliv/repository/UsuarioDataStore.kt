package cl.duoc.corpaliv.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.corpaliv.model.OpcionDonacionMensual
import cl.duoc.corpaliv.model.RolUsuario
import cl.duoc.corpaliv.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsuarioDataStore(private val context: Context) {

    private val KEY_USUARIOS = stringPreferencesKey("lista_usuarios_v2")

    fun obtenerUsuarios(): Flow<List<Usuario>> {
        return context.dataStore.data.map { prefs ->
            val rawData = prefs[KEY_USUARIOS] ?: ""
            if (rawData.isBlank()) {
                emptyList()
            } else {
                rawData.split("||").mapNotNull { registro ->
                    val partes = registro.split(";")
                    if (partes.size >= 10) {
                        Usuario(
                            id = partes[0],
                            nombre = partes[1],
                            apellido = partes[2],
                            rut = partes[3],
                            fechaNacimiento = partes[4],
                            direccion = partes[5],
                            latitud = partes[6].toDoubleOrNull(),
                            longitud = partes[7].toDoubleOrNull(),
                            donacionMensual = parseDonacion(partes[8]),
                            correo = partes[9],
                            rol = parseRol(partes.getOrNull(10))
                        )
                    } else null
                }
            }
        }
    }

    private fun sanear(texto: String): String {
        return texto.replace(";", ",").replace("||", " ")
    }

    suspend fun guardarUsuario(usuario: Usuario) {
        context.dataStore.edit { prefs ->
            val listaActual = (prefs[KEY_USUARIOS] ?: "").split("||").filter { it.isNotBlank() }.toMutableList()
            val donacionStr = when (val d = usuario.donacionMensual) {
                is OpcionDonacionMensual.CincoMil -> "5000"
                is OpcionDonacionMensual.QuinceMil -> "15000"
                is OpcionDonacionMensual.VeinticincoMil -> "25000"
                is OpcionDonacionMensual.Personalizado -> "CUSTOM_${d.montoLibre}"
            }
            val rolStr = when (usuario.rol) {
                is RolUsuario.Socio -> "SOCIO"
                is RolUsuario.Voluntario -> "VOLUNTARIO"
                is RolUsuario.Funcionario -> "FUNCIONARIO"
            }
            val nuevoRegistro = "${sanear(usuario.id)};${sanear(usuario.nombre)};${sanear(usuario.apellido)};${sanear(usuario.rut)};${sanear(usuario.fechaNacimiento)};${sanear(usuario.direccion)};${usuario.latitud ?: ""};${usuario.longitud ?: ""};$donacionStr;${sanear(usuario.correo)};$rolStr"
            listaActual.add(nuevoRegistro)
            prefs[KEY_USUARIOS] = listaActual.joinToString("||")
        }
    }

    suspend fun actualizarRolUsuario(idUsuario: String, nuevoRol: RolUsuario) {
        context.dataStore.edit { prefs ->
            val listaRaw = prefs[KEY_USUARIOS] ?: ""
            if (listaRaw.isNotBlank()) {
                val rolStr = when (nuevoRol) {
                    is RolUsuario.Socio -> "SOCIO"
                    is RolUsuario.Voluntario -> "VOLUNTARIO"
                    is RolUsuario.Funcionario -> "FUNCIONARIO"
                }
                val registros = listaRaw.split("||").mapNotNull { registro ->
                    val partes = registro.split(";").toMutableList()
                    if (partes.size >= 10 && partes[0] == idUsuario) {
                        if (partes.size == 10) partes.add(rolStr) else partes[10] = rolStr
                        partes.joinToString(";")
                    } else {
                        registro
                    }
                }
                prefs[KEY_USUARIOS] = registros.joinToString("||")
            }
        }
    }

    private fun parseDonacion(str: String): OpcionDonacionMensual {
        return when {
            str == "5000" -> OpcionDonacionMensual.CincoMil
            str == "15000" -> OpcionDonacionMensual.QuinceMil
            str == "25000" -> OpcionDonacionMensual.VeinticincoMil
            str.startsWith("CUSTOM_") -> {
                val monto = str.removePrefix("CUSTOM_").toIntOrNull() ?: 5000
                OpcionDonacionMensual.Personalizado(monto)
            }
            else -> OpcionDonacionMensual.CincoMil
        }
    }

    private fun parseRol(str: String?): RolUsuario {
        return when (str) {
            "VOLUNTARIO" -> RolUsuario.Voluntario()
            "FUNCIONARIO" -> RolUsuario.Funcionario()
            else -> RolUsuario.Socio
        }
    }
}