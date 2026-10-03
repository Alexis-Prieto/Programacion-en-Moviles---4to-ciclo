# Flujo de Prompts para la Construcción de la Aplicación — Tecsup Store

## Prompt 1: Configuración de Tema Material 3 y Barra de Estado

**Lo que se le pidió:**  
Configura el tema de Material 3 en `MainActivity.kt`, fijando la paleta de colores en morado e implementando `SystemBarStyle.light` para que la barra de estado (*StatusBar*) sea de fondo blanco con íconos oscuros para contrastar de forma limpia con la barra superior de la app.

**Correcciones y ajustes realizados:**  
Se ajustó la llamada a `enableEdgeToEdge()` especificando `SystemBarStyle.light(Color.White.toArgb(), Color.White.toArgb())`, asegurando que la barra del sistema quede en fondo blanco limpio.

---

## Prompt 2: Maquetado del Catálogo de Productos con LazyColumn (RF-01)

**Lo que se le pidió:**  
Construye la pantalla principal del catálogo usando un `LazyColumn` en `TarjetaProducto.kt`, renderizando cada tarjeta con el ícono de producto, nombre y precio formateado en soles (`S/ 00.00`).

**Correcciones y ajustes realizados:**  
Se ajustaron los márgenes internos (*padding*) de las tarjetas (`Card`) y se amplió el listado de elementos para garantizar el desplazamiento vertical (*scroll*).

---

## Prompt 3: Implementación del Menú Contextual (DropdownMenu)

**Lo que se le pidió:**  
Agrega el ícono de tres puntos (`⋮`) a la derecha de cada tarjeta de producto para desplegar un `DropdownMenu` con las opciones "Favoritos", "Compartir" y "Reportar", incluyendo su respectivo `leadingIcon`.

**Correcciones y ajustes realizados:**  
Se controló el estado de apertura (`expanded`) de forma independiente en cada tarjeta y se asignaron los íconos correspondientes a cada opción del menú.

---

## Prompt 4:

**Lo que se le pidió:**  
Crea la estructura superior de `AppDrawer.kt` en un `ModalDrawerSheet`, incluyendo un avatar circular lavanda con las iniciales "AP", el nombre "Alexis Prieto", su correo institucional y un divisor horizontal (`HorizontalDivider`).

**Correcciones y ajustes realizados:**  
Se maquetó la cabecera dentro de una fila (`Row`) alineada verticalmente y se ajustó la separación visual respecto al cuerpo del menú.

---

## Prompt 5: Opciones de Navegación en el AppDrawer (RF-03)

**Lo que se le pidió:**  
Implementa las opciones del menú lateral ("Inicio", "Mis pedidos", "Favoritos", "Perfil", "Cerrar sesion") usando componentes `NavigationDrawerItem` con íconos circulares delineados (`RadioButtonUnchecked`).

**Correcciones y ajustes realizados:**  
Se ordenaron los destinos de navegación respetando la estructura solicitada en la guía del laboratorio.

---

## Prompt 6:

**Lo que se le pidió:**  
Haz que el destino activo ("Inicio") en el `AppDrawer` resalte del resto mediante un contenedor de fondo en forma de píldora de color lavanda suave y texto/ícono en morado oscuro.

**Correcciones y ajustes realizados:**  
Se parametrizó la propiedad `colors` con `NavigationDrawerItemDefaults.colors(selectedContainerColor = Color(0xFFF3EDF7))` y bordes redondeados a `16.dp`.

---

## Prompt 7: Elevación de Estado para Favoritos

**Lo que se le pidió:**  
Conecta la acción "Favoritos" del `DropdownMenu` de las tarjetas con el menú lateral, aplicando elevación de estado (*state hoisting*) sobre la variable `cantidadFavoritos` para actualizar el valor global cada vez que se seleccione un producto.

**Correcciones y ajustes realizados:**  
Se extrajo el estado de `cantidadFavoritos` al componente contenedor principal, pasando la función lambda de incremento a las tarjetas y el valor acumulado al `AppDrawer`.

---

## Prompt 8: Integración del Badge Reactivo en Favoritos (Mejora Obligatoria)

**Lo que se le pidió:**  
Agrega un componente `Badge` con la variable `cantidadFavoritos` directamente en el slot `badge` del `NavigationDrawerItem` correspondiente a la opción "Favoritos".

**Correcciones y ajustes realizados:**  
Se utilizó el parámetro `badge` nativo de `NavigationDrawerItem`, renderizando una burbuja violeta con texto en blanco al lado derecho de "Favoritos".

---

## Prompt 9:

**Lo que se le pidió:**  
Ajusta la lógica del `Badge` en `AppDrawer.kt` para que cuando no haya favoritos (contador en 0), la burbuja morada y el número "0" no se muestren en la interfaz.

**Correcciones y ajustes realizados:**  
Se envolvió el componente `Badge` en la condición `if (opcion == "Favoritos" && cantidadFavoritos > 0)`, haciendo que la burbuja se oculte limpiamente cuando no existen elementos guardados.