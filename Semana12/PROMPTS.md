# Documentacion de los prompts utilizados para la fase 2 (mejora-ia)

## Consulta 1

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Revisa el flujo de sincronización de los Pokémon de tipo fuego y su almacenamiento en Room. Asegura que la sincronización actualice los datos locales y conserve los favoritos. Al limpiar la caché, los cambios deben reflejarse en las pantallas que observan Room. Mantén el comportamiento de carga y notificación requerido por el laboratorio.
- Qué respondió (resumen): Explicó cómo se relacionan la sincronización remota, Room y los favoritos, y propuso ajustes para mantener los datos locales actualizados.
- Qué usamos / qué cambiamos y por qué: Se ajustó la sincronización del catálogo y su caché local para actualizar los datos y conservar los favoritos.
- Qué aprendimos: La interfaz debe mostrar los datos observados desde Room, que funciona como fuente local de verdad.

## Consulta 2

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Revisa la restauración de sesión y la navegación inicial. El rol debe obtenerse de Firestore después de autenticar al usuario y determinar las pantallas disponibles para usuario y administrador. Mantén el catálogo y los favoritos disponibles para ambos roles y reserva el panel para el administrador.
- Qué respondió (resumen): Indicó que la navegación debía considerar la sesión restaurada y el rol registrado en Firestore.
- Qué usamos / qué cambiamos y por qué: Se corrigió la restauración de sesión y el acceso según el rol para que cada tipo de usuario llegue a las pantallas correspondientes.
- Qué aprendimos: El rol se consulta desde Firestore y determina qué funciones se habilitan en la aplicación.

## Consulta 3

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Incorpora una búsqueda reactiva al catálogo por nombre o número de Pokédex. El filtrado debe actualizarse mientras se escribe, ignorar diferencias entre mayúsculas y minúsculas e incluir una acción visible para borrar rápidamente el texto. Conserva los estados existentes de la lista.
- Qué respondió (resumen): Propuso filtrar los datos del catálogo a medida que cambia el texto de búsqueda.
- Qué usamos / qué cambiamos y por qué: Se agregó búsqueda en vivo para encontrar Pokémon por nombre o número.
- Qué aprendimos: La búsqueda puede actualizar los resultados inmediatamente sin volver a consultar la API en cada letra.

## Consulta 4

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Añade opciones para ordenar el catálogo por nombre y por número de Pokédex. La búsqueda debe seguir funcionando junto con el orden elegido. Al cambiar el criterio, muestra la lista desde el inicio del nuevo orden.
- Qué respondió (resumen): Explicó cómo combinar el filtrado y el ordenamiento, y cómo reiniciar la posición de desplazamiento cuando cambia el criterio.
- Qué usamos / qué cambiamos y por qué: Se agregó ordenamiento por nombre o número y se ajustó la lista para mostrar el inicio del nuevo orden.
- Qué aprendimos: Al actualizar el orden de los resultados también debe actualizarse la posición visible de la lista.

## Consulta 5

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Mejora la presentación de las pantallas de detalle y favoritos para que mantengan el estilo visual Pokémon de la aplicación. Conserva la navegación, los datos del detalle y el funcionamiento de los favoritos. Ajusta también el tamaño del título de Favoritos para que sea coherente con los demás títulos.
- Qué respondió (resumen): Propuso mejoras visuales para dar coherencia a las pantallas y facilitar la lectura de sus elementos.
- Qué usamos / qué cambiamos y por qué: Se ajustó el diseño de detalle y favoritos, incluido el tamaño del título y la presentación de sus tarjetas.
- Qué aprendimos: Mantener tamaños y jerarquías de texto consistentes ayuda a que las pantallas se sientan parte de la misma aplicación.

## Consulta 6

- Fecha: 08/11/26
- Herramienta: ChatGPT
- Prompt: Centra los títulos de las barras superiores para mantener una presentación consistente. Oculta el acceso de cierre de sesión en la pantalla de inicio únicamente para el administrador, porque esa acción ya está disponible en su panel. Mantén ese acceso para el usuario normal.
- Qué respondió (resumen): Recomendó alinear los títulos y controlar la acción de salida según el rol.
- Qué usamos / qué cambiamos y por qué: Se centraron los títulos y se ocultó el icono de cierre de sesión solo en la pantalla del administrador.
- Qué aprendimos: Las acciones visibles pueden variar según el rol, aunque algunas pantallas sean compartidas.

## Consulta 7

- Fecha: 09/11/26
- Herramienta: ChatGPT
- Prompt: Ajusta el panel Admin para que los totales de Pokémon guardados y favoritos reflejen los datos actuales de Room. Después de sincronizar o limpiar la caché, muestra el resultado de la operación de forma temporal y retíralo automáticamente.
- Qué respondió (resumen): Indicó que los totales debían actualizarse con los datos locales y que los avisos debían desaparecer después de un tiempo.
- Qué usamos / qué cambiamos y por qué: Se actualizaron los totales del panel y se ajustó la visualización de los mensajes de resultado.
- Qué aprendimos: Los totales deben derivarse de los datos actuales y los mensajes temporales no deben quedarse fijos en pantalla.

## Consulta 8

- Fecha: 09/11/26
- Herramienta: ChatGPT
- Prompt: Integra fondos temáticos relacionados con Pokémon en las pantallas de la aplicación. Mantén contraste suficiente para que los textos, las tarjetas y los botones sean legibles. Limita los cambios a la presentación visual.
- Qué respondió (resumen): Propuso incorporar fondos temáticos manteniendo la legibilidad de los controles y el contenido.
- Qué usamos / qué cambiamos y por qué: Se agregaron fondos temáticos para dar mayor unidad visual a la aplicación.
- Qué aprendimos: Los fondos deben complementar la interfaz sin reducir el contraste ni dificultar su uso.

## Consulta 9

- Fecha: 09/11/26
- Herramienta: ChatGPT
- Prompt: Configura el JDK del daemon de Gradle para utilizar Java 21 en este proyecto. Realiza únicamente el ajuste necesario y evita modificar dependencias o código fuente que no esté relacionado.
- Qué respondió (resumen): Señaló el ajuste necesario en la configuración del daemon de Gradle.
- Qué usamos / qué cambiamos y por qué: Se configuró Java 21 para el daemon mediante `gradle-daemon-jvm.properties`.
- Qué aprendimos: Gradle puede usar una versión de Java configurada para su daemon independientemente de la lógica de la aplicación.

## Consulta 10

- Fecha: 09/11/26
- Herramienta: ChatGPT
- Prompt: Implementa pruebas unitarias para `ListaViewModel` usando un repositorio falso que implemente `ItemRepository` y un notificador falso. Cubre la carga inicial, la caché, la sincronización, los errores, el reintento, los favoritos, la búsqueda y el ordenamiento. Incluye las pruebas necesarias para verificar esos comportamientos y no modifiques la lógica de producción.
- Qué respondió (resumen): Generó pruebas para distintos estados y operaciones del ViewModel usando dependencias falsas.
- Qué usamos / qué cambiamos y por qué: Se agregó `ListaViewModelTest.kt` para probar el ViewModel sin depender de internet ni servicios externos.
- Qué aprendimos: Un repositorio falso permite controlar los datos y errores para probar cada comportamiento del ViewModel de manera aislada.

## Consulta 11

- Fecha: 09/11/26
- Herramienta: ChatGPT
- Prompt: Revisa las dependencias necesarias para las pruebas unitarias y valida la ejecución de `ListaViewModelTest`. Indica el comando para ejecutarlas y explica el resultado de Gradle diferenciando errores de advertencias.
- Qué respondió (resumen): Confirmó que el módulo incluía JUnit y `kotlinx-coroutines-test`, y explicó cómo ejecutar las pruebas.
- Qué usamos / qué cambiamos y por qué: Se ejecutó `.\gradlew.bat :app:testDebugUnitTest --rerun-tasks`; Gradle terminó con `BUILD SUCCESSFUL`.
- Qué aprendimos: `--rerun-tasks` solicita volver a ejecutar las tareas y una advertencia deprecada no significa necesariamente que las pruebas hayan fallado.