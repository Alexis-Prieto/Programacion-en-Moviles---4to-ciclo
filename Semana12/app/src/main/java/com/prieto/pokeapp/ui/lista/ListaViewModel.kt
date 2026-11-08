package com.prieto.pokeapp.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.prieto.pokeapp.data.repository.ErrorServidorException
import java.io.IOException

data class ResultadoBusqueda(val items: List<Item>, val listaOriginalVacia: Boolean)
enum class OrdenLista { NUMERO, NOMBRE }

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _tabSeleccionado = MutableStateFlow(0)
    val tabSeleccionado: StateFlow<Int> = _tabSeleccionado.asStateFlow()

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()
    private val _orden = MutableStateFlow(OrdenLista.NUMERO)
    val orden: StateFlow<OrdenLista> = _orden.asStateFlow()
    val uiState: StateFlow<UiState<ResultadoBusqueda>> = combine(_estado, _busqueda, _orden) { estado, texto, orden ->
        when (estado) {
            is UiState.Cargando -> UiState.Cargando
            is UiState.Error -> estado
            is UiState.Exito -> {
                val consulta = texto.trim().removePrefix("#")
                val filtrados = if (consulta.isEmpty()) estado.datos else estado.datos.filter { item ->
                    item.nombre.startsWith(consulta, ignoreCase = true) ||
                        item.id.toString().startsWith(consulta) ||
                        item.id.toString().padStart(3, '0').startsWith(consulta)
                }
                val ordenados = when (orden) {
                    OrdenLista.NUMERO -> filtrados.sortedBy { it.id }
                    OrdenLista.NOMBRE -> filtrados.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.nombre })
                }
                UiState.Exito(ResultadoBusqueda(ordenados, estado.datos.isEmpty()))
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Cargando)

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()
    private var refrescando = false
    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            combine(repo.items, repo.favoritos, _tabSeleccionado) { todos, favoritos, tab ->
                if (tab == 0) todos else favoritos
            }.collect { lista ->
                if ((refrescoTerminado || lista.isNotEmpty()) && (_estado.value !is UiState.Error || lista.isNotEmpty())) {
                    _estado.value = UiState.Exito(lista)
                }
            }
        }
        viewModelScope.launch {
            val guardados = repo.items.first()
            if (guardados.isEmpty()) {
                refrescar()
            } else {
                refrescoTerminado = true
            }
        }
    }

    fun seleccionarTab(index: Int) {
        _tabSeleccionado.value = index
    }

    fun cambiarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun cambiarOrden(orden: OrdenLista) {
        _orden.value = orden
    }

    fun refrescar() {
        if (refrescando) return
        refrescando = true
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    _aviso.value = null
                    _estado.value = UiState.Exito(if (_tabSeleccionado.value == 0) repo.items.first() else repo.favoritos.first())
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) {
                        _estado.value = UiState.Error(mensajeDe(e))
                    } else {
                        _estado.value = UiState.Exito(if (_tabSeleccionado.value == 0) repo.items.first() else repo.favoritos.first())
                        _aviso.value = if (e is IOException) "Sin conexión: mostrando datos guardados" else "No se pudo sincronizar: mostrando datos guardados"
                    }
                }
            refrescando = false
        }
    }

    fun sincronizar() = refrescar()

    fun cambiarFavorito(id: Int) = viewModelScope.launch {
        repo.alternarFavorito(id)
    }

    fun alternarFavorito(id: Int) = cambiarFavorito(id)

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet."
        is ErrorServidorException -> if (e.codigo == 404) "No se encontró la información del Pokémon." else "El servidor no responde. Intenta más tarde."
        else -> "Ocurrió un error inesperado."
    }
}
