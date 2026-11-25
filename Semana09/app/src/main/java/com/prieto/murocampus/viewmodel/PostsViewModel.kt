package com.prieto.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.murocampus.data.PostRepository
import com.prieto.murocampus.data.mensajeDe
import com.prieto.murocampus.model.Post
import com.prieto.murocampus.model.Comentario
import com.prieto.murocampus.model.NuevoPost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista =
        MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    val lista = _lista.asStateFlow()

    private val _detalle =
        MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)

    val detalle = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)

    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _busqueda = MutableStateFlow("")

    val busqueda = _busqueda.asStateFlow()

    private var publicacionesOriginales: List<Post> = emptyList()

    private val _publicacion =
        MutableStateFlow<UiState<Post>?>(null)

    val publicacion = _publicacion.asStateFlow()

    private val _eliminacion = MutableStateFlow<UiState<Unit>?>(null)

    val eliminacion = _eliminacion.asStateFlow()

    private var trabajoLista: Job? = null

    private var trabajoDetalle: Job? = null

    private var trabajoPublicacion: Job? = null

    private var trabajoEliminacion: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        trabajoLista?.cancel()

        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando

            val userId = _usuarioFiltro.value

            val resultado = if (userId == null) {
                repo.listar()
            } else {
                repo.porUsuario(userId)
            }

            resultado
                .onSuccess {
                    publicacionesOriginales = it
                    aplicarBusqueda()
                }
                .onFailure {
                    _lista.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
        if (_lista.value is UiState.Exito) {
            aplicarBusqueda()
        }
    }

    private fun aplicarBusqueda() {
        val texto = _busqueda.value
        val publicaciones = publicacionesOriginales.filter {
            it.title.contains(texto, ignoreCase = true)
        }
        _lista.value = UiState.Exito(publicaciones)
    }

    fun abrir(id: Int) {
        trabajoDetalle?.cancel()

        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando

            val post = repo.detalle(id).getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }

            val comentarios = repo.comentarios(id).getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }

            _detalle.value = UiState.Exito(
                DetalleUi(post, comentarios)
            )
        }
    }

    fun cerrarDetalle() {
        trabajoDetalle?.cancel()
        trabajoEliminacion?.cancel()
        _eliminacion.value = null
    }

    fun eliminar(id: Int) {
        if (_eliminacion.value == UiState.Cargando || _eliminacion.value is UiState.Exito) return

        _eliminacion.value = UiState.Cargando
        trabajoEliminacion = viewModelScope.launch {
            repo.eliminar(id)
                .onSuccess {
                    publicacionesOriginales = publicacionesOriginales.filterNot { it.id == id }
                    if (_lista.value is UiState.Exito) aplicarBusqueda()
                    _eliminacion.value = UiState.Exito(Unit)
                }
                .onFailure {
                    _eliminacion.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun publicar(titulo: String, texto: String) {

        if (titulo.isBlank() || texto.isBlank()) return

        if (_publicacion.value == UiState.Cargando) return

        trabajoPublicacion = viewModelScope.launch {
            _publicacion.value = UiState.Cargando

            val nuevo = NuevoPost(
                userId = 1,
                title = titulo,
                body = texto
            )

            repo.crear(nuevo)
                .onSuccess {
                    _publicacion.value = UiState.Exito(it)
                }
                .onFailure {
                    _publicacion.value = UiState.Error(mensajeDe(it))
                }
        }
    }

    fun limpiarPublicacion() {
        trabajoPublicacion?.cancel()
        _publicacion.value = null
    }
}