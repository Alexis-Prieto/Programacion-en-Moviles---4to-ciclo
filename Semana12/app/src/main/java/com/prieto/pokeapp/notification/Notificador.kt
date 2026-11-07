package com.prieto.pokeapp.notification

interface Notificador {
    fun mostrarNotificacion(titulo: String, mensaje: String)
    fun mostrar(titulo: String, mensaje: String) = mostrarNotificacion(titulo, mensaje)
}
