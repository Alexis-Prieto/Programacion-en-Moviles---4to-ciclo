package com.prieto.pokeapp.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import android.os.SystemClock
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.prieto.pokeapp.domain.model.Rol
import com.prieto.pokeapp.domain.model.Sesion
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val inicio = SystemClock.elapsedRealtime()
        val authResult = try {
            withTimeoutOrNull(15_000) { auth.signInWithEmailAndPassword(correo, clave).await() }
                ?: throw TiempoAgotadoAuthException()
        } finally {
            Log.d("LoginTiming", "auth_ms=${SystemClock.elapsedRealtime() - inicio}")
        }
        val user = authResult.user ?: throw Exception("Usuario no encontrado")
        Result.success(leerSesion(user.uid, correo))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        auth.signOut()
        Result.failure(if (e is FirebaseNetworkException) ErrorConexionAuthException() else e)
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null
        return leerSesion(u.uid, u.email ?: "")
    }

    override fun cerrarSesion() {
        try {
            auth.signOut()
        } catch (e: Exception) {
        }
    }

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val inicio = SystemClock.elapsedRealtime()
        val rol = try {
            val doc = withTimeoutOrNull(15_000) { db.collection("usuarios").document(uid).get().await() }
                ?: throw ErrorRolException()
            when (doc.getString("rol")?.lowercase()) {
                "admin" -> Rol.ADMIN
                "usuario" -> Rol.USUARIO
                else -> throw ErrorRolException()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw ErrorRolException()
        } finally {
            Log.d("LoginTiming", "firestore_ms=${SystemClock.elapsedRealtime() - inicio}")
        }
        return Sesion(uid, correo, rol)
    }
}
