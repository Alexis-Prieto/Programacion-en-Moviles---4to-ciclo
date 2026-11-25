package com.prieto.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.murocampus.data.PostRepository
import com.prieto.murocampus.data.mensajeDe
import com.prieto.murocampus.model.Post
import com.prieto.murocampus.model.Comentario
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

    private var trabajoDetalle: Job? = null

    init {
        cargar()
    }
    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando

            repo.listar()
                .onSuccess {
                    _lista.value = UiState.Exito(it)
                }
                .onFailure {
                    _lista.value = UiState.Error(mensajeDe(it))
                }
        }
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
    }
}