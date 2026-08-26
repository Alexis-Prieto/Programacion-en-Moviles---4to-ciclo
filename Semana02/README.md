# Laboratorio 02 - Carrito de Compras

## Nombre completo: 
Alexis Stephano Prieto Huiza

## Descripción
Programa de un carrito de compras hecho en kotlin para TECSUP. Permite agregar productos, mostrar el detalle y calcular el subtotal, IGV y total. También identifica el producto más caro y aplica descuentos según el monto de la compra.

## Captura de la consola





## Pregunta de la Parte 2
||¿por qué nombre y precio son val pero cantidad es var? ¿Qué pasaría si intentas cambiar el precio después de crear el producto?||
nombre y precio son val porque no deben cambiar después de crear el producto. Cantidad es var porque sí puede cambiar, por ejemplo, de 1 a 2 unidades. Si intentamos cambiar el precio, kotlin mostrará un error porque fue declarado como val.