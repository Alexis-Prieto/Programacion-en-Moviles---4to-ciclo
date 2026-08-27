# Laboratorio 02 - Carrito de Compras

## Nombre completo: 
Alexis Stephano Prieto Huiza

## Descripción
Programa de un carrito de compras hecho en kotlin para TECSUP. Permite agregar productos, mostrar el detalle y calcular el subtotal, IGV y total. También identifica el producto más caro y aplica descuentos según el monto de la compra.

## Prompts usados
<img width="1062" height="526" alt="image" src="https://github.com/user-attachments/assets/ee9f1735-0f89-4413-8c09-93af1f67445a" />
<img width="1050" height="460" alt="image" src="https://github.com/user-attachments/assets/9680f6dd-a35c-43d8-ace8-d5fdd7dc2324" />
<img width="1117" height="555" alt="image" src="https://github.com/user-attachments/assets/bd29fd51-086e-4d64-949a-b2580bf2ffce" />
<img width="1072" height="495" alt="image" src="https://github.com/user-attachments/assets/8c71b82c-d278-456b-b809-13318a5ab8a7" />
<img width="1021" height="375" alt="image" src="https://github.com/user-attachments/assets/7c257237-f7b7-410b-9575-dcc15c0a37c4" />


## Pregunta de la Parte 2
||¿por qué nombre y precio son val pero cantidad es var? ¿Qué pasaría si intentas cambiar el precio después de crear el producto?||
nombre y precio son val porque no deben cambiar después de crear el producto. Cantidad es var porque sí puede cambiar, por ejemplo, de 1 a 2 unidades. Si intentamos cambiar el precio, kotlin mostrará un error porque fue declarado como val.
