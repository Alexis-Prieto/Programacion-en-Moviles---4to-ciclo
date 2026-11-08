package com.prieto.pokeapp.ui.lista

import com.prieto.pokeapp.data.repository.ErrorServidorException
import com.prieto.pokeapp.data.repository.ItemRepository
import com.prieto.pokeapp.domain.model.DetalleItem
import com.prieto.pokeapp.domain.model.Item
import com.prieto.pokeapp.notification.Notificador
import com.prieto.pokeapp.ui.UiState
import java.io.IOException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val notificador = NotificadorFalso()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun crearViewModel(repo: RepositorioFalso): ListaViewModel =
        ListaViewModel(repo, notificador)

    private fun resultado(vm: ListaViewModel): ResultadoBusqueda =
        (vm.uiState.value as UiState.Exito).datos

    @Test
    fun cacheConDatosSeMuestraSinSincronizarNiNotificar() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(6, "charizard"), Item(4, "charmander")))
        val vm = crearViewModel(repo)
        advanceUntilIdle()

        assertEquals(listOf(4, 6), resultado(vm).items.map { it.id })
        assertEquals(0, repo.llamadasRefresco)
        assertTrue(notificador.mensajes.isEmpty())
    }

    @Test
    fun primeraCargaEsperaElContenidoRealDelRepositorio() = runTest(dispatcher) {
        val repo = RepositorioFalso(emptyList(), emitirInicialmente = false)
        val vm = crearViewModel(repo)
        runCurrent()
        assertEquals(UiState.Cargando, vm.uiState.value)
        assertEquals(0, repo.llamadasRefresco)

        repo.emitir(listOf(Item(4, "charmander")))
        advanceUntilIdle()
        assertEquals(listOf(4), resultado(vm).items.map { it.id })
        assertEquals(0, repo.llamadasRefresco)
    }

    @Test
    fun cacheVaciaCargaDesdeRepositorioYNotifica() = runTest(dispatcher) {
        val repo = RepositorioFalso(emptyList())
        repo.descarga = listOf(Item(4, "charmander"), Item(5, "charmeleon"))
        val vm = crearViewModel(repo)
        advanceUntilIdle()

        assertEquals(listOf(4, 5), resultado(vm).items.map { it.id })
        assertEquals(1, repo.llamadasRefresco)
        assertEquals(listOf("Datos actualizados" to "Se guardaron 2 elementos"), notificador.mensajes)
    }

    @Test
    fun errorDeConexionSinDatosMuestraErrorYPermiteReintentar() = runTest(dispatcher) {
        val repo = RepositorioFalso(emptyList()).apply { fallo = IOException() }
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        assertEquals(UiState.Error("No hay conexión a internet."), vm.uiState.value)
        assertTrue(notificador.mensajes.isEmpty())

        repo.fallo = null
        repo.descarga = listOf(Item(4, "charmander"))
        vm.refrescar()
        advanceUntilIdle()
        assertEquals(listOf(4), resultado(vm).items.map { it.id })
        assertEquals(2, repo.llamadasRefresco)
        assertEquals(1, notificador.mensajes.size)
    }

    @Test
    fun falloDeSincronizacionConDatosMantieneCacheYAviso() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(4, "charmander")))
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        repo.fallo = IOException()
        vm.sincronizar()
        advanceUntilIdle()

        assertEquals(listOf(4), resultado(vm).items.map { it.id })
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
        assertTrue(notificador.mensajes.isEmpty())
    }

    @Test
    fun errorHttp404SinDatosMuestraMensajeEspecifico() = runTest(dispatcher) {
        val repo = RepositorioFalso(emptyList()).apply { fallo = ErrorServidorException(404) }
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        assertEquals(UiState.Error("No se encontró la información del Pokémon."), vm.uiState.value)
    }

    @Test
    fun sincronizacionManualActualizaListaConservaFavoritoYNotifica() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(4, "charmander", esFavorito = true)))
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        repo.descarga = listOf(Item(4, "charmander"), Item(6, "charizard"))
        vm.sincronizar()
        advanceUntilIdle()

        assertEquals(listOf(4, 6), resultado(vm).items.map { it.id })
        assertTrue(resultado(vm).items.first().esFavorito)
        assertEquals(1, repo.llamadasRefresco)
        assertEquals(listOf("Datos actualizados" to "Se guardaron 2 elementos"), notificador.mensajes)
    }

    @Test
    fun alternarFavoritoActualizaListaYSeleccionDeFavoritos() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(4, "charmander"), Item(6, "charizard")))
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        vm.alternarFavorito(6)
        advanceUntilIdle()
        assertTrue(resultado(vm).items.first { it.id == 6 }.esFavorito)
        vm.seleccionarTab(1)
        advanceUntilIdle()
        assertEquals(listOf(6), resultado(vm).items.map { it.id })
        vm.alternarFavorito(6)
        advanceUntilIdle()
        assertTrue(resultado(vm).items.isEmpty())
        assertTrue(resultado(vm).listaOriginalVacia)
    }

    @Test
    fun busquedaPorPrefijoNombreYNumeroDistingueListaVacia() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(4, "charmander"), Item(5, "charmeleon"), Item(6, "charizard"), Item(37, "vulpix")))
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        vm.cambiarBusqueda("CHAR")
        advanceUntilIdle()
        assertEquals(listOf(4, 5, 6), resultado(vm).items.map { it.id })
        vm.cambiarBusqueda("#00")
        advanceUntilIdle()
        assertEquals(listOf(4, 5, 6), resultado(vm).items.map { it.id })
        vm.cambiarBusqueda("xyz")
        advanceUntilIdle()
        assertTrue(resultado(vm).items.isEmpty())
        assertFalse(resultado(vm).listaOriginalVacia)
        vm.cambiarBusqueda("")
        advanceUntilIdle()
        assertEquals(4, resultado(vm).items.size)
        assertEquals(0, repo.llamadasRefresco)
    }

    @Test
    fun ordenSeAplicaALosResultadosFiltrados() = runTest(dispatcher) {
        val repo = RepositorioFalso(listOf(Item(4, "charmander"), Item(5, "charmeleon"), Item(6, "charizard")))
        val vm = crearViewModel(repo)
        advanceUntilIdle()
        vm.cambiarBusqueda("char")
        vm.cambiarOrden(OrdenLista.NOMBRE)
        advanceUntilIdle()
        assertEquals(listOf(6, 4, 5), resultado(vm).items.map { it.id })
        vm.cambiarBusqueda("c")
        repo.emitir(listOf(Item(6, "charizard"), Item(5, "charmeleon"), Item(4, "charmander")))
        advanceUntilIdle()
        assertEquals(listOf(6, 4, 5), resultado(vm).items.map { it.id })
        vm.cambiarOrden(OrdenLista.NUMERO)
        advanceUntilIdle()
        assertEquals(listOf(4, 5, 6), resultado(vm).items.map { it.id })
    }
}

private class NotificadorFalso : Notificador {
    val mensajes = mutableListOf<Pair<String, String>>()
    override fun mostrarNotificacion(titulo: String, mensaje: String) {
        mensajes.add(titulo to mensaje)
    }
}

private class RepositorioFalso(iniciales: List<Item>, emitirInicialmente: Boolean = true) : ItemRepository {
    private val inicio = CompletableDeferred<Unit>().apply { if (emitirInicialmente) complete(Unit) }
    private val datos = MutableStateFlow(iniciales)
    private val favoritosActuales = MutableStateFlow(iniciales.filter { it.esFavorito })
    override val items: Flow<List<Item>> = kotlinx.coroutines.flow.flow {
        inicio.await()
        datos.collect { emit(it) }
    }
    override val favoritos: Flow<List<Item>> = favoritosActuales
    var descarga: List<Item> = iniciales
    var fallo: Throwable? = null
    var llamadasRefresco = 0

    fun emitir(items: List<Item>) {
        datos.value = items
        favoritosActuales.value = items.filter { it.esFavorito }
        inicio.complete(Unit)
    }

    override suspend fun refrescar(): Result<Int> {
        llamadasRefresco++
        fallo?.let { return Result.failure(it) }
        val idsFavoritos = datos.value.filter { it.esFavorito }.map { it.id }.toSet()
        emitir(descarga.map { it.copy(esFavorito = it.id in idsFavoritos) })
        return Result.success(descarga.size)
    }

    override suspend fun alternarFavorito(id: Int) {
        emitir(datos.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it })
    }

    override suspend fun cantidadGuardada(): Int = datos.value.size
    override suspend fun cantidadFavoritos(): Int = favoritosActuales.value.size
    override suspend fun limpiarCache() = emitir(emptyList())
    override suspend fun obtenerDetalle(nombreOId: String): Result<DetalleItem> =
        Result.failure(UnsupportedOperationException("Detalle no se usa en estas pruebas"))
}