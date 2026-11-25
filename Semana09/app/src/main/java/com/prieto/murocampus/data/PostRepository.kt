package com.prieto.murocampus.data

import com.prieto.murocampus.data.remote.PostApi
import com.prieto.murocampus.model.Post
import com.prieto.murocampus.model.Comentario
import com.prieto.murocampus.model.NuevoPost
import com.prieto.murocampus.model.EditarPost
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

interface PostRepository {

    suspend fun listar(): Result<List<Post>>

    suspend fun detalle(id: Int): Result<Post>

    suspend fun comentarios(id: Int): Result<List<Comentario>>

    suspend fun porUsuario(userId: Int): Result<List<Post>>

    suspend fun crear(nuevo: NuevoPost): Result<Post>

    suspend fun eliminar(id: Int): Result<Unit>

    suspend fun editar(id: Int, cambios: EditarPost): Result<Post>
}

class PostRepositoryRemote(
    private val api: PostApi
) : PostRepository {

    override suspend fun listar(): Result<List<Post>> =
        llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> =
        llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> =
        llamar { api.crear(nuevo) }

    override suspend fun eliminar(id: Int): Result<Unit> =
        llamar {
            val respuesta = api.eliminar(id)
            if (!respuesta.isSuccessful) throw HttpException(respuesta)
            Unit
        }

    override suspend fun editar(id: Int, cambios: EditarPost): Result<Post> =
        llamar { api.editar(id, cambios) }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> {
        return try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}