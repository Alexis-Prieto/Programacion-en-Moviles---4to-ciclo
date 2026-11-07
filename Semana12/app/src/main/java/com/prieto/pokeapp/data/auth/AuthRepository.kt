package com.prieto.pokeapp.data.auth

import com.prieto.pokeapp.domain.model.Sesion

class ErrorConexionAuthException : Exception()
class ErrorRolException : Exception()
class TiempoAgotadoAuthException : Exception()

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun sesionActual(): Sesion?
    fun cerrarSesion()
}