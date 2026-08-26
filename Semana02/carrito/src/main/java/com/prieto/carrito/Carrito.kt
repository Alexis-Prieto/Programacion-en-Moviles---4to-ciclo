package com.prieto.carrito

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0

    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }

    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>) {
    println()
    println("---------- DETALLE DEL CARRITO ----------")

    var i = 1

    for (p in productos) {
        val importe = p.precio * p.cantidad

        println(String.format("%d. %-20s x%d S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }

    println("--------------------------------")
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun main() {
    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    print("Ingrese el nombre del cliente: ")
    val nombreCliente = readln()

    println()
    println("Cliente: $nombreCliente")
    println()

    val productos = listOf(
        Producto("Laptop HP", 2500.0, 0),
        Producto("Mouse Logitech", 45.5, 0),
        Producto("Teclado", 143.0, 0),
        Producto("Audifonos", 79.0, 0)
    )

    val carrito = mutableListOf<Producto>()
    var continuar = "si"

    while (continuar.equals("si", ignoreCase = true)) {

        println()
        println("---------- PRODUCTOS DISPONIBLES ----------")

        var i = 1
        for (producto in productos) {
            println(
                String.format("%d. %-20s S/ %8.2f", i, producto.nombre, producto.precio))
            i++
        }
        println("-------------------------------------------")

        print("Seleccione el producto: ")
        val opcion = readln().toIntOrNull()

        if (opcion == null || opcion !in 1..productos.size) {
            println("Opción no válida.")
            continue
        }

        val productoSeleccionado = productos[opcion - 1]
        print("Ingrese la cantidad: ")
        val cantidad = readln().toIntOrNull()

        if (cantidad == null || cantidad <= 0) {
            println("Cantidad no válida.")
            continue
        }
        carrito.add(Producto(productoSeleccionado.nombre, productoSeleccionado.precio, cantidad))
        println()
        println("Producto agregado: ${productoSeleccionado.nombre}")

        print("¿Desea seguir comprando? (si/no): ")
        continuar = readln()
    }
    if (carrito.isEmpty()) {
        println()
        println("No se agregaron productos al carrito.")
    } else {
        mostrarDetalle(carrito)

        println("Cantidad de productos: ${carrito.size}")

        val subtotal = calcularSubtotal(carrito)
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)
        println(
            String.format("Subtotal      : S/ %8.2f", subtotal))
        println(String.format("IGV (18%%)     : S/ %8.2f", igv))
        println(String.format("TOTAL A PAGAR : S/ %8.2f", total))
        println("--------------------------------")

        val masCaro = carrito.maxByOrNull {
            it.precio
        }
        if (masCaro != null) {
            println(
                "Producto mas caro: ${masCaro.nombre}" + String.format(" (S/ %.2f)", masCaro.precio))
        }
        val descuento = calcularDescuento(total)
        if (total > 5000) {

            println("Descuento aplicado: 10% por compra mayor a S/ 5000")
        } else if (total > 3000) {
            println("Descuento aplicado: 5% por compra mayor a S/ 3000")

        } else {
            println("No se aplicó descuento")
        }
        val totalConDescuento = total - descuento
        println(
            String.format("TOTAL CON DESCUENTO : S/ %.2f", totalConDescuento))
    }
    println()
    println("Gracias por su compra, $nombreCliente!")
}