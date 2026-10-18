package com.prieto.cafecampus.model

import com.prieto.cafecampus.data.local.entity.Producto

data class ItemCarrito(
    val producto: Producto,
    val cantidad: Int = 1
)