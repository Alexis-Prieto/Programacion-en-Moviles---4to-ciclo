package com.prieto.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "configuraciones_user")

class PreferenciasStore(private val context: Context) {

    companion object {
        private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
        private val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo")
        private val FOTO_PERFIL_KEY = stringPreferencesKey("foto_perfil_uri")
        // Claves para guardar los datos personales del usuario
        private val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
        private val CORREO_USUARIO = stringPreferencesKey("correo_usuario")
    }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false
    }
    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[RECIBIR_PEDIDOS] ?: true
    }
    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: ""
    }
    val fotoPerfilUri: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[FOTO_PERFIL_KEY]
    }
    val nombreUsuario: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[NOMBRE_USUARIO] ?: "Alexis Prieto"
    }

    val correoUsuario: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[CORREO_USUARIO] ?: "alexis@cafecampus.com"
    }
    suspend fun guardarPerfil(rol: String) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
        }
    }
    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = b
        }
    }
    suspend fun guardarRecibirPedidos(b: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[RECIBIR_PEDIDOS] = b
        }
    }
    suspend fun guardarFotoPerfilUri(uriString: String) {
        context.dataStore.edit { prefs ->
            prefs[FOTO_PERFIL_KEY] = uriString
        }
    }
    suspend fun guardarDatosUsuario(nombre: String, correo: String) {
        context.dataStore.edit { prefs ->
            prefs[NOMBRE_USUARIO] = nombre
            prefs[CORREO_USUARIO] = correo
        }
    }
    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}