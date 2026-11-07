package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _tabSeleccionado = MutableStateFlow(0)
    val tabSeleccionado: StateFlow<Int> = _tabSeleccionado.asStateFlow()

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Exito(emptyList()))
    val uiState: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repo.items, repo.favoritos, _tabSeleccionado) { todos, favoritos, tab ->
                if (tab == 0) todos else favoritos
            }.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }

    fun seleccionarTab(index: Int) {
        _tabSeleccionado.value = index
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = repo.refrescar()
            resultado
                .onSuccess { n ->
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        _aviso.value = "Sin conexión: mostrando datos guardados"
                    }
                }
        }
    }

    fun sincronizar() = refrescar()

    fun cambiarFavorito(id: Int) = viewModelScope.launch {
        repo.alternarFavorito(id)
    }

    fun alternarFavorito(id: Int) = cambiarFavorito(id)

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet."
        is HttpException -> if (e.code() == 404) "No se encontró la información del Pokémon." else "El servidor no responde. Intenta más tarde."
        else -> "Ocurrió un error inesperado."
    }
}
