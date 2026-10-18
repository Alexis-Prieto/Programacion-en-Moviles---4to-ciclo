package com.prieto.cafecampus.data.local

import com.prieto.cafecampus.data.local.entity.Producto
import com.prieto.cafecampus.data.local.entity.Usuario

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() == 0) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Alexis Prieto",
                    correo = "alexis@campus.test",
                    clave = "1234",
                    rol = "CLIENTE"
                )
            )
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador",
                    correo = "admin@cafe.test",
                    clave = "admin",
                    rol = "ADMIN"
                )
            )
            val productos = listOf(
                Producto(
                    nombre = "Café americano",
                    descripcion = "Café pasado recién elaborado",
                    categoria = "Bebidas",
                    precio = 4.00,
                    disponible = true,
                    icono = "cafe"
                ),
                Producto(
                    nombre = "Sándwich de pollo",
                    descripcion = "Con mayonesa y lechuga fresca",
                    categoria = "Desayunos",
                    precio = 9.00,
                    disponible = true,
                    icono = "pan"
                ),
                Producto(
                    nombre = "Menú del día",
                    descripcion = "Entrada + Segundo + Refresco",
                    categoria = "Almuerzos",
                    precio = 12.00,
                    disponible = true,
                    icono = "plato"
                ),
                Producto(
                    nombre = "Jugo de naranja",
                    descripcion = "100% natural exprimido al instante",
                    categoria = "Bebidas",
                    precio = 5.00,
                    disponible = true,
                    icono = "jugo"
                ),
                Producto(
                    nombre = "Empanada",
                    descripcion = "Horneada rellena de carne picada",
                    categoria = "Desayunos",
                    precio = 4.50,
                    disponible = true,
                    icono = "pan"
                ),
                Producto(
                    nombre = "Chicha morada",
                    descripcion = "Vaso helado con toque de limón",
                    categoria = "Bebidas",
                    precio = 3.50,
                    disponible = true,
                    icono = "botella"
                )
            )
            for (p in productos) {
                db.productoDao().insertar(p)
            }
        }
    }
}