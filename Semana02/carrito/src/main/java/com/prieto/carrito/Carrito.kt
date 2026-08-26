package com.prieto.carrito

// =========================================
// CLIENTE
// =========================================
data class Cliente(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val correo: String,
    val telefono: String
)


// =========================================
// CALCULAR SUBTOTAL
// =========================================
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0

    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }

    return subtotal
}


// =========================================
// CALCULAR IGV
// =========================================
fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}


// =========================================
// CALCULAR TOTAL
// =========================================
fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}


// =========================================
// MOSTRAR DETALLE DEL CARRITO
// =========================================
fun mostrarDetalle(productos: List<Producto>) {

    println()
    println("---------- DETALLE DEL CARRITO ----------")

    if (productos.isEmpty()) {
        println("El carrito está vacío.")
        println("--------------------------------")
        return
    }

    var i = 1

    for (p in productos) {

        val importe = p.precio * p.cantidad

        println(
            String.format(
                "%d. %-20s x%d S/ %8.2f",
                i,
                p.nombre,
                p.cantidad,
                importe
            )
        )

        i++
    }

    println("--------------------------------")
}


// =========================================
// CALCULAR DESCUENTO
// =========================================
fun calcularDescuento(total: Double): Double {

    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}


// =========================================
// BUSCAR PRODUCTO
// =========================================
fun buscarProducto(
    productos: List<Producto>,
    nombre: String
): Producto? {

    return productos.find {
        it.nombre.equals(nombre, ignoreCase = true)
    }
}


// =========================================
// MOSTRAR PRODUCTOS DISPONIBLES
// =========================================
fun mostrarProductosDisponibles(
    productos: List<Producto>
) {

    println()
    println("---------- PRODUCTOS DISPONIBLES ----------")

    var i = 1

    for (producto in productos) {

        println(
            String.format(
                "%d. %-20s S/ %8.2f",
                i,
                producto.nombre,
                producto.precio
            )
        )

        i++
    }

    println("-------------------------------------------")
}


// =========================================
// LEER TEXTO
// =========================================
fun leerTexto(mensaje: String): String {

    while (true) {

        print(mensaje)

        val texto = readln().trim()

        if (texto.isNotEmpty()) {
            return texto
        }

        println("El campo no puede estar vacío.")
    }
}


// =========================================
// LEER ENTERO POSITIVO
// =========================================
fun leerEnteroPositivo(mensaje: String): Int {

    while (true) {

        print(mensaje)

        val entrada = readln()
        val numero = entrada.toIntOrNull()

        if (numero != null && numero > 0) {
            return numero
        }

        println("Ingrese un número entero mayor que 0.")
    }
}


// =========================================
// LEER DNI
// =========================================
fun leerDni(): String {

    while (true) {

        print("DNI: ")

        val dni = readln().trim()

        if (dni.length == 8 && dni.all { it.isDigit() }) {
            return dni
        }

        println("El DNI debe tener exactamente 8 dígitos.")
    }
}


// =========================================
// LEER TELEFONO
// =========================================
fun leerTelefono(): String {

    while (true) {

        print("Telefono: ")

        val telefono = readln().trim()

        if (telefono.length == 9 && telefono.all { it.isDigit() }) {
            return telefono
        }

        println("El telefono debe tener exactamente 9 dígitos.")
    }
}


// =========================================
// LEER CORREO
// =========================================
fun leerCorreo(): String {

    while (true) {

        print("Correo: ")

        val correo = readln().trim()

        if (correo.contains("@") && correo.contains(".")) {
            return correo
        }

        println("Ingrese un correo válido.")
    }
}


// =========================================
// MOSTRAR TOTALES
// =========================================
fun mostrarTotales(productos: List<Producto>) {

    println(
        "Cantidad de productos: ${productos.size}"
    )

    val subtotal = calcularSubtotal(productos)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(
        String.format(
            "Subtotal      : S/ %8.2f",
            subtotal
        )
    )

    println(
        String.format(
            "IGV (18%%)     : S/ %8.2f",
            igv
        )
    )

    println(
        String.format(
            "TOTAL A PAGAR : S/ %8.2f",
            total
        )
    )

    println("--------------------------------")
}


// =========================================
// MOSTRAR DESCUENTO
// =========================================
fun mostrarDescuento(total: Double) {

    val descuento = calcularDescuento(total)

    if (total > 5000) {

        println(
            "Descuento aplicado: 10% por compra mayor a S/ 5000"
        )

    } else if (total > 3000) {

        println(
            "Descuento aplicado: 5% por compra mayor a S/ 3000"
        )

    } else {

        println("No se aplicó descuento")
    }

    val totalConDescuento = total - descuento

    println(
        String.format(
            "TOTAL CON DESCUENTO : S/ %.2f",
            totalConDescuento
        )
    )
}


// =========================================
// MAIN
// =========================================
fun main() {

    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")


    // =====================================
    // DATOS DEL CLIENTE
    // =====================================
    println()
    println("---------- DATOS DEL CLIENTE ----------")

    val nombre = leerTexto("Nombre: ")
    val apellido = leerTexto("Apellido: ")
    val dni = leerDni()
    val correo = leerCorreo()
    val telefono = leerTelefono()

    val cliente = Cliente(
        nombre,
        apellido,
        dni,
        correo,
        telefono
    )

    println()
    println(
        "Cliente: ${cliente.nombre} ${cliente.apellido}"
    )

    println()


    // =====================================
    // CATÁLOGO DE LA TIENDA
    // =====================================
    val productosDisponibles = listOf(

        Producto(
            "Laptop HP",
            2500.0,
            0
        ),

        Producto(
            "Mouse Logitech",
            45.5,
            0
        ),

        Producto(
            "Teclado",
            143.0,
            0
        ),

        Producto(
            "Audifonos",
            79.0,
            0
        )
    )


    // =====================================
    // CARRITO VACÍO
    // =====================================
    val carrito = mutableListOf<Producto>()


    // =====================================
    // SELECCIÓN DE PRODUCTOS
    // =====================================
    var seguirComprando = true

    while (seguirComprando) {

        mostrarProductosDisponibles(
            productosDisponibles
        )

        val opcion = leerEnteroPositivo(
            "Seleccione el producto que desea agregar: "
        )

        if (opcion > productosDisponibles.size) {

            println("Producto no válido.")
            continue
        }

        val productoSeleccionado =
            productosDisponibles[opcion - 1]

        val cantidad = leerEnteroPositivo(
            "Ingrese la cantidad: "
        )


        // =================================
        // COMPROBAR SI YA EXISTE
        // =================================
        val productoEnCarrito = buscarProducto(
            carrito,
            productoSeleccionado.nombre
        )

        if (productoEnCarrito != null) {

            // Si ya existe, aumenta la cantidad
            productoEnCarrito.cantidad += cantidad

            println()
            println(
                "Cantidad actualizada: " +
                        "${productoSeleccionado.nombre}"
            )

        } else {

            // Si no existe, se agrega al carrito
            carrito.add(
                Producto(
                    productoSeleccionado.nombre,
                    productoSeleccionado.precio,
                    cantidad
                )
            )

            println()
            println(
                "Producto agregado: " +
                        productoSeleccionado.nombre
            )
        }


        // =================================
        // CONTINUAR COMPRANDO
        // =================================
        println()

        print(
            "¿Desea seguir comprando? (si/no): "
        )

        val respuesta = readln()

        seguirComprando =
            respuesta.equals("si", ignoreCase = true)
    }


    // =====================================
    // MOSTRAR CARRITO
    // =====================================
    mostrarDetalle(carrito)


    // =====================================
    // TOTALES
    // =====================================
    if (carrito.isNotEmpty()) {

        mostrarTotales(carrito)

        val subtotal = calcularSubtotal(carrito)
        val igv = calcularIGV(subtotal)
        val total = calcularTotal(subtotal, igv)


        // =================================
        // PRODUCTO MÁS CARO
        // =================================
        val masCaro = carrito.maxByOrNull {
            it.precio
        }

        if (masCaro != null) {

            println(
                "Producto mas caro: ${masCaro.nombre}" +
                        String.format(
                            " (S/ %.2f)",
                            masCaro.precio
                        )
            )
        }
        // =================================
        // DESCUENTO
        // =================================
        mostrarDescuento(total)
    }

    // =====================================
    // BUSCAR PRODUCTO
    // =====================================
    println()
    print(
        "¿Desea buscar un producto en el carrito? (si/no): ")
    val respuestaBuscar = readln()

    if (respuestaBuscar.equals("si", ignoreCase = true)) {

        val nombreBuscar = leerTexto(
            "Ingrese el nombre del producto: "
        )
        val productoEncontrado = buscarProducto(
            carrito,
            nombreBuscar
        )
        println()

        if (productoEncontrado != null) {

            println("Producto encontrado:")
            println(
                "Nombre: ${productoEncontrado.nombre}"
            )

            println(
                String.format(
                    "Precio: S/ %.2f",
                    productoEncontrado.precio
                )
            )

            println(
                "Cantidad: ${productoEncontrado.cantidad}"
            )

        } else {

            println("Producto no encontrado en el carrito.")
        }
    }


    // =====================================
    // ELIMINAR PRODUCTO
    // =====================================
    println()

    print(
        "¿Desea eliminar un producto del carrito? (si/no): "
    )

    val respuestaEliminar = readln()

    if (respuestaEliminar.equals("si", ignoreCase = true)) {

        val nombreEliminar = leerTexto(
            "Ingrese el nombre del producto a eliminar: "
        )

        val eliminado = carrito.removeIf {

            it.nombre.equals(
                nombreEliminar,
                ignoreCase = true
            )
        }

        println()

        if (eliminado) {

            println(
                "Producto eliminado correctamente."
            )

            // Mostrar carrito actualizado
            mostrarDetalle(carrito)

            if (carrito.isNotEmpty()) {

                // Recalcular todo
                val nuevoSubtotal =
                    calcularSubtotal(carrito)

                val nuevoIgv =
                    calcularIGV(nuevoSubtotal)

                val nuevoTotal =
                    calcularTotal(
                        nuevoSubtotal,
                        nuevoIgv
                    )

                println(
                    "Cantidad de productos: ${carrito.size}"
                )

                println(
                    String.format(
                        "Subtotal      : S/ %8.2f",
                        nuevoSubtotal
                    )
                )

                println(
                    String.format(
                        "IGV (18%%)     : S/ %8.2f",
                        nuevoIgv
                    )
                )

                println(
                    String.format(
                        "TOTAL A PAGAR : S/ %8.2f",
                        nuevoTotal
                    )
                )

                println("--------------------------------")

                val nuevoMasCaro =
                    carrito.maxByOrNull {
                        it.precio
                    }

                if (nuevoMasCaro != null) {

                    println(
                        "Producto mas caro: " +
                                "${nuevoMasCaro.nombre}" +
                                String.format(
                                    " (S/ %.2f)",
                                    nuevoMasCaro.precio
                                )
                    )
                }

                mostrarDescuento(nuevoTotal)

            } else {

                println(
                    "El carrito quedó vacío."
                )
            }

        } else {

            println(
                "No se encontró el producto."
            )
        }
    }


    // =====================================
    // FINALIZAR
    // =====================================
    println()
    println(
        "Gracias por su compra, ${cliente.nombre}!"
    )
}