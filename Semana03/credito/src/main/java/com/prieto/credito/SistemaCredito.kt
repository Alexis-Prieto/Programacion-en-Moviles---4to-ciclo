package com.prieto.credito
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun main() {
    println("========================================")
    println("       REGISTRO DE COMPRA A CREDITO")
    println("========================================")

    var nombreProducto = ""
    while (nombreProducto.isBlank()) {
        print("Nombre del producto: ")
        nombreProducto = readln().trim()
        if (nombreProducto.isBlank()) {
            println("El nombre no puede quedar vacio.")
        }
    }
    var precioProducto = 0.0
    var precioValido = false

    while (!precioValido) {
        print("Precio del producto: ")
        val entradaPrecio = readln().trim().replace(",", ".")
        val precioIngresado = entradaPrecio.toDoubleOrNull()

        if (precioIngresado == null) {
            println("Ingresa un precio valido.")
        } else if (precioIngresado <= 0) {
            println("El precio debe ser mayor que 0.")
        } else {
            precioProducto = precioIngresado
            precioValido = true
        }
    }

    var cantidad = 0
    var cantidadValida = false

    while (!cantidadValida) {
        print("Cantidad: ")
        val cantidadIngresada = readln().trim().toIntOrNull()

        if (cantidadIngresada == null) {
            println("Ingresa una cantidad valida.")
        } else if (cantidadIngresada <= 0) {
            println("La cantidad debe ser mayor que 0.")
        } else {
            cantidad = cantidadIngresada
            cantidadValida = true
        }
    }
    var cuotas = 0
    while (cuotas != 6 && cuotas != 12 && cuotas != 24) {
        print("Elige la cantidad de cuotas (6, 12 o 24): ")
        cuotas = readln().trim().toIntOrNull() ?: 0

        if (cuotas != 6 && cuotas != 12 && cuotas != 24) {
            println("Esa cantidad de cuotas no esta disponible. Usa 6, 12 o 24.")
        }
    }
    val montoInicial = precioProducto * cantidad
    val porcentajeInteres = when (cuotas) {
        6 -> 0.20
        12 -> 0.40
        24 -> 0.60
        else -> 0.0
    }
    val interes = montoInicial * porcentajeInteres
    val totalPagar = montoInicial + interes
    val valorCuota = totalPagar / cuotas

    println()
    println("---Compra registrada---")
    println("Producto: $nombreProducto")
    println("Precio: S/ %.2f".format(precioProducto))
    println("Cantidad: $cantidad")
    println("Cuotas elegidas: $cuotas")
    println()
    println("Monto inicial: S/ %.2f".format(montoInicial))
    println("Interes: S/ %.2f".format(interes))
    println("Total a pagar: S/ %.2f".format(totalPagar))
    println("Valor de cada cuota: S/ %.2f".format(valorCuota))

    var saldoPendiente = totalPagar
    val pagos = mutableListOf<String>()
    var numeroCuota = 1
    var opcion = 0

    while (opcion != 5) {
        println()
        println("------ MENU ------")
        println("1. Registrar pago")
        println("2. Ver pagos realizados")
        println("3. Ver saldo pendiente")
        println("4. Ver resumen")
        println("5. Salir")
        print("Elige una opcion: ")
        opcion = readln().trim().toIntOrNull() ?: 0

        when (opcion) {
            1 -> {
                if (saldoPendiente <= 0) {
                    println("La deuda ya fue pagada por completo.")
                } else {
                    println("Saldo pendiente: S/ %.2f".format(saldoPendiente))

                    var montoPagado = 0.0
                    var montoValido = false

                    while (!montoValido) {
                        print("Monto a pagar: S/ ")
                        val entradaPago = readln().trim().replace(",", ".")
                        val pagoIngresado = entradaPago.toDoubleOrNull()

                        if (pagoIngresado == null) {
                            println("Ingresa un monto valido.")
                        } else if (pagoIngresado <= 0) {
                            println("El pago debe ser mayor que 0.")
                        } else if (pagoIngresado > saldoPendiente) {
                            println("El pago supera el saldo pendiente.")
                        } else {
                            montoPagado = pagoIngresado
                            montoValido = true
                        }
                    }
                    var fechaValida = false
                    var fechaPago = ""

                    while (!fechaValida) {
                        print("Fecha del pago (DD-MM-YYYY): ")
                        fechaPago = readln().trim()

                        try {
                            val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            LocalDate.parse(fechaPago, formato)
                            fechaValida = true
                        } catch (e: DateTimeParseException) {
                            println("Ingresa una fecha valida. Usa DD-MM-YYYY.")
                        }
                    }

                    val saldoAntes = saldoPendiente
                    saldoPendiente -= montoPagado
                    val pago = "Cuota $numeroCuota | " + "Fecha: $fechaPago | " + "Saldo antes: S/ %.2f | ".format(saldoAntes) +
                            "Pagado: S/ %.2f | ".format(montoPagado) + "Saldo restante: S/ %.2f".format(saldoPendiente)

                    pagos.add(pago)
                    println("Pago registrado.")
                    numeroCuota++

                    if (saldoPendiente == 0.0) {
                        println("La deuda fue cancelada completamente.")
                    }
                }
            }
            2 -> {
                if (pagos.isEmpty()) {
                    println("Todavia no se ha registrado ningun pago.")
                } else {
                    println("------ PAGOS REALIZADOS ------")
                    for (pago in pagos) {
                        println(pago)
                    }
                }
            }
            3 -> {
                println("Saldo pendiente: S/ %.2f".format(saldoPendiente))
                if (saldoPendiente <= 0) {
                    println("La deuda ya fue cancelada completamente.")
                }
            }
            4 -> {
                println("------ RESUMEN DE LA COMPRA ------")
                println("Producto: $nombreProducto")
                println("Precio: S/ %.2f".format(precioProducto))
                println("Cantidad: $cantidad")
                println("Cuotas: $cuotas")
                println("Monto inicial: S/ %.2f".format(montoInicial))
                println("Interes: S/ %.2f".format(interes))
                println("Total a pagar: S/ %.2f".format(totalPagar))
                println("Valor de cada cuota: S/ %.2f".format(valorCuota))
                println("Saldo pendiente: S/ %.2f".format(saldoPendiente))
            }
            5 -> {
                println("Gracias por usar el sistema")
            }
            else -> {
                println("Esa opcion no existe. Elige del 1 al 5.")
            }
        }
    }
}