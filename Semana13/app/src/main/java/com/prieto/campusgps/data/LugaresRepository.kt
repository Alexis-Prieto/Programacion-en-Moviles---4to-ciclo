package com.prieto.campusgps.data

import com.prieto.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}