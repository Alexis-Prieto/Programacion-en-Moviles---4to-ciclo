package com.prieto.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {
    companion object {
        private val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo")
        private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
    }
    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: ""
    }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false
    }
    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[RECIBIR_PEDIDOS] ?: true
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
    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = ""
        }
    }
}