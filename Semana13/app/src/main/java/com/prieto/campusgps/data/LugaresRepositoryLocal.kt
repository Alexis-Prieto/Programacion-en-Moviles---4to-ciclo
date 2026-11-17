package com.prieto.campusgps.data

import com.prieto.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {

    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Biblioteca Central", "Estudio", -12.044964217690318, -76.95286207627282),
        Lugar(2, "Cafetería Campus", "Comida", -12.044077456288676, -76.95217393889318),
        Lugar(3, "Laboratorio de Cómputo", "Estudio", -12.044469373519254, -76.95274873599853),
        Lugar(4, "Auditorio Principal", "Eventos", -12.044766280131315, -76.95278516680098),
        Lugar(5, "Estacionamiento Norte", "Servicios", -12.045106732642438, -76.95226703983279)
    )
}