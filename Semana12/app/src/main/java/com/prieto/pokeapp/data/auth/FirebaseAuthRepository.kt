package com.prieto.pokeapp.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prieto.pokeapp.domain.model.Rol
import com.prieto.pokeapp.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        auth.signOut()
        val authResult = auth.signInWithEmailAndPassword(correo, clave).await()
        val user = authResult.user ?: throw Exception("Usuario no encontrado")
        Result.success(leerSesion(user.uid, correo))
    } catch (e: Exception) {
        Result.failure(e)
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
        val rol = try {
            val doc = db.collection("usuarios").document(uid).get().await()
            val rolStr = doc.getString("rol")?.lowercase()
            if (rolStr == "admin") Rol.ADMIN else Rol.USUARIO
        } catch (e: Exception) {
            Rol.USUARIO
        }
        return Sesion(uid, correo, rol)
    }
}
