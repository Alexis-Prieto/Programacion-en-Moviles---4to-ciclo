# DOCUMENTACIÓN DE TODOS LOS PROMPTS QUE SE TUVO AL REALIZAR LA FASE 2 (mejora -ia)

## Prompt 1
**Texto enviado a Gemini:** "Necesito actualizar `UserEntity` y `UserDao` en Room para que la consulta de datos del usuario sea reactiva retornando `Flow<User>`."
**Que genero la IA:** Generó la entidad, la consulta SQL `@Query` en el DAO retornando `Flow<User>` y la clase de implementación `UserRepositoryImpl`.
**Que tuve que corregir yo:** La IA no declaró la función en la interfaz base. Tuve que agregar primero `fun getUserStream(): Flow<User?>` en `CafeRepository` antes de implementarla.
**Respeta MVVM?:** Sí, tras la corrección la capa de presentación se comunica exclusivamente mediante la interfaz del repositorio.

## Prompt 2
**Texto enviado a Gemini:** "¿Cómo declaro correctamente `getUserStream()` en la interfaz `CafeRepository` e impleméntalo en el repositorio concreto sin romper la arquitectura?"
**Que genero la IA:** La firma de la función en la interfaz `CafeRepository` y su correspondiente implementación en `UserRepositoryImpl`.
**Que tuve que corregir yo:** Verificar que el tipo devuelto permitiera nulos (`User?`) para manejar el estado cuando no hay ningún usuario autenticado.
**Respeta MVVM?:** Sí, mantiene la abstracción mediante contratos en la capa de datos.

## Prompt 3
**Texto enviado a Gemini:** "Crea `ProfileViewModel` y `NavigationViewModel` inyectando `CafeRepository`. Deben exponer el usuario activo y la ruta actual mediante `StateFlow`."
**Que genero la IA:** Los ViewModels solicitados exponiendo sus estados inmutables con `asStateFlow()`.
**Que tuve que corregir yo:** En el bloque `catch`, la IA intentó instanciar directamente el `UserDao`. Eliminé ese código e hice que la recuperación de datos pase 100% por `CafeRepository`.
**Respeta MVVM?:** Sí, la vista reacciona únicamente a los `StateFlow` expuestos por los ViewModels.

## Prompt 4
**Texto enviado a Gemini:** "En `MenuScreen` y `AdminScreen` conecta sus ViewModels con `collectAsStateWithLifecycle()`. Aplica State Hoisting para no pasar `NavController` a los componentes hijos."
**Que genero la IA:** La conexión de estados y el paso de lambdas de eventos (`onNavigateToDetail`, `onLogout`) hacia los Composables secundarios.
**Que tuve que corregir yo:** Renombré y estandaricé los nombres de las lambdas enviadas a las vistas hijas para mantener la consistencia en el proyecto.
**Respeta MVVM?:** Sí, promueve la elevación de estado (*State Hoisting*), manteniendo los Composables limpios.

## Prompt 5
**Texto enviado a Gemini:** "Genera la pantalla de registro en Compose y la función `registerUser` en `AuthViewModel` consumiendo `CafeRepository`."
**Que genero la IA:** El formulario de registro y la función básica de creación de usuario en el ViewModel.
**Que tuve que corregir yo:** Añadí la regla de negocio para restringir el registro únicamente a correos institucionales (`@unsa.edu.pe`) dentro del ViewModel antes de llamar al repositorio.
**Respeta MVVM?:** Sí, las validaciones y reglas de negocio residen en la capa del ViewModel.

## Prompt 6
**Texto enviado a Gemini:** "Implementa en `AuthViewModel` el control de la sesión activa para que persista el estado del usuario al cerrar y reabrir la app usando `CafeRepository`."
**Que genero la IA:** Funciones `saveSession` y `clearSession` gestionando la clave de sesión activa.
**Que tuve que corregir yo:** Asegurar que al cerrar sesión el `StateFlow` de la sesión se resetee inmediatamente a nulo en el ViewModel.
**Respeta MVVM?:** Sí, la persistencia y control de estado de sesión residen en la capa de datos y ViewModel.

## Prompt 7
**Texto enviado a Gemini:** "Al seleccionar una foto de la galería en `PerfilScreen`, obtengo una URI temporal. ¿Cómo la manejo desde la vista para actualizar el perfil mediante el ViewModel?"
**Que genero la IA:** El launcher en Compose con `rememberLauncherForActivityResult` y la invocación de actualización en `ProfileViewModel`.
**Que tuve que corregir yo:** Detecté que la URI de la galería pierde permisos al reiniciar la app. Delegué el procesamiento a la capa del repositorio.
**Respeta MVVM?:** Sí, la vista solo captura la URI y se la envía al ViewModel.

## Prompt 8
**Texto enviado a Gemini:** "¿Cómo hago en la capa de datos para copiar físicamente la foto de perfil desde la URI obtenida hacia la memoria interna de la app y persistir esa ruta local en Room?"
**Que genero la IA:** Función auxiliar de copia de archivos con `InputStream` y `FileOutputStream` integrada en la implementación del repositorio.
**Que tuve que corregir yo:** Declarar la función en la interfaz `CafeRepository` antes de usarla y manejar excepciones en caso de espacio insuficiente.
**Respeta MVVM?:** Sí, la gestión de archivos e I/O ocurre estrictamente en la capa del repositorio.

## Prompt 9
**Texto enviado a Gemini:** "En el panel de administración necesito un gráfico de barras por hora que calcule el volumen de pedidos. La agregación debe procesarse en `AdminViewModel`."
**Que genero la IA:** El algoritmo de agrupación por hora en `AdminViewModel` y la estructura gráfica con Canvas/Compose.
**Que tuve que corregir yo:** Reajusté las proporciones y espacios entre las barras en Compose para evitar desbordamientos en pantallas pequeñas.
**Respeta MVVM?:** Sí, las operaciones matemáticas ocurren en el ViewModel y la UI se limita a dibujar.

## Prompt 10
**Texto enviado a Gemini:** "Añade al panel de administración un reporte del día que muestre el producto más vendido y el desglose de ventas por forma de pago (Efectivo, Yape, Plin)."
**Que genero la IA:** Consultas de agregación expuestas desde `AdminViewModel` consumiendo `CafeRepository` y tarjetas informativas en Compose.
**Que tuve que corregir yo:** Apliqué formato local de moneda (`S/`) y redondeo a dos decimales dentro del Composable.
**Respeta MVVM?:** Sí, la lógica contable y de cálculo reside en el ViewModel.

## Prompt 11
**Texto enviado a Gemini:** "Agrega un campo de búsqueda por nombre en `MenuScreen`. La lógica debe ir en `MenuViewModel` combinando `StateFlow`s (`combine`). No coloques lógica en el Composable."
**Que genero la IA:** El componente de texto `CafeTextField` y el filtrado reactivo combinando flujos en el ViewModel.
**Que tuve que corregir yo:** Ajusté los bordes redondeados y colores del campo de texto para alinearlos con el tema global de la cafetería.
**Respeta MVVM?:** Sí, la vista reacciona al flujo filtrado emitido por el `StateFlow` del ViewModel.

## Prompt 12
**Texto enviado a Gemini:** "El carrito de compras debe aislarse por `userId`. Modifica la entidad `CartItem` y la lógica de `CafeRepository` para soportar múltiples carritos."
**Que genero la IA:** Adición de la clave foránea del usuario en `CartItem` y consultas filtradas en la base de datos.
**Que tuve que corregir yo:** Asegurar que si no hay usuario activo, la consulta del carrito retorne un flujo vacío de manera segura.
**Respeta MVVM?:** Sí, el aislamiento de datos por usuario se maneja en la capa del repositorio.

## Prompt 13
**Texto enviado a Gemini:** "Al confirmar el pedido en `ConfirmarPedidoScreen` o cerrar sesión, limpia la tabla del carrito consumiendo `CafeRepository` desde el ViewModel."
**Que genero la IA:** Métodos de limpieza en el repositorio y sus respectivas llamadas en `CartViewModel` y `AuthViewModel`.
**Que tuve que corregir yo:** Aseguré que el `StateFlow` del carrito emitiera un estado vacío inmediato antes de cargar la siguiente cuenta.
**Respeta MVVM?:** Sí, el ciclo de vida y estado del carrito se manejan en la capa de datos/repositorio.

## Prompt 14
**Texto enviado a Gemini:** "En `SeguimientoScreen` no quiero mostrar el ID autogenerado de Room, sino un número de pedido relativo por cliente (#1, #2, #3). ¿Cómo lo calculo?"
**Que genero la IA:** Consulta SQL para calcular la posición relativa del pedido según el historial del usuario e integración en el repositorio.
**Que tuve que corregir yo:** Formateé la cadena en la vista para presentar el número con ceros a la izquierda (ej. `#001`).
**Respeta MVVM?:** Sí, el cálculo del correlativo proviene de la base de datos a través de la interfaz del repositorio.

## Prompt 15
**Texto enviado a Gemini:** "En `PedidosAdminScreen` necesito mostrar el nombre real del cliente en cada tarjeta de pedido haciendo un JOIN entre la tabla de pedidos y usuarios."
**Que genero la IA:** Creó la consulta con `@Relation` en el DAO e intentó instanciar el DAO directamente dentro del Composable de administración.
**Que tuve que corregir yo:** **Violación de MVVM cometida por la IA.** Gemini saltó las capas arquitectónicas. Corregí el código declarando la función primero en `CafeRepository`, implementándola en el repositorio y exponiendo los datos mediante `AdminViewModel`.
**Respeta MVVM?:** No en la propuesta inicial de la IA, pero fue corregido totalmente para mantener el flujo desacoplado.

## Prompt 16
**Texto enviado a Gemini:** "Revisa la ventana emergente de notas de producto. Elimina el scroll innecesario y reemplázalo por un contenedor estático y compacto."
**Que genero la IA:** Eliminación del modificador `verticalScroll` y reestructuración del layout con `Column`.
**Que tuve que corregir yo:** Ajusté los espacios internos (*paddings*) para evitar que el campo de texto se solapara con los botones.
**Respeta MVVM?:** Sí, es una optimización estructural de la interfaz en Compose.

## Prompt 17
**Texto enviado a Gemini:** "Configura el formulario de notas para que al desplegarse el teclado virtual (`IME`), los botones de confirmación no queden tapados."
**Que genero la IA:** Aplicación del modificador `Modifier.imePadding()` en el contenedor principal de la vista.
**Que tuve que corregir yo:** Ajusté el comportamiento del scroll suave para garantizar que el campo enfocado permanezca siempre visible.
**Respeta MVVM?:** Sí, gestiona eventos nativos de la interfaz de usuario en Android.

## Prompt 18
**Texto enviado a Gemini:** "En `ConfirmarPedidoScreen` agrega alertas de eliminación para los productos de la lista."
**Que genero la IA:** Componentes `AlertDialog` solicitando confirmación de borrado antes de llamar al ViewModel.
**Que tuve que corregir yo:** Conectar el evento de confirmación del diálogo con el método `removeItem` del ViewModel.
**Respeta MVVM?:** Sí, el diálogo solo captura la intención del usuario y delega la acción al ViewModel.

## Prompt 19
**Texto enviado a Gemini:** "En `ConfirmarPedidoScreen` valida si la cafetería está abierta evaluando el horario comercial en el ViewModel."
**Que genero la IA:** Lógica de validación de horario comercial y banner de advertencia si la cafetería está cerrada.
**Que tuve que corregir yo:** La IA colocó la verificación de hora (`LocalTime.now()`) directamente en el Composable. Trasladé esa lógica al ViewModel para exponer la propiedad booleana `isStoreOpen`.
**Respeta MVVM?:** Sí, la regla de negocio del horario fue corregida e integrada en el ViewModel.

## Prompt 20
**Texto enviado a Gemini:** "Permite agregar un producto al carrito directamente desde el icono '+' en la tarjeta de `MenuScreen` mediante un evento que notifique al ViewModel."
**Que genero la IA:** Asignación de la lambda en el botón '+' de `ProductoCard` para ejecutar la función `addToCart` del `MenuViewModel`.
**Que tuve que corregir yo:** Implementé una animación rápida en el icono del carrito superior como retroalimentación visual al agregar un producto.
**Respeta MVVM?:** Sí, utiliza el patrón de Flujo de Datos Unidireccional (UDF).

## Prompt 21
**Texto enviado a Gemini:** "Estandariza la barra inferior de navegación (`NavigationBar`) para las pantallas del cliente (Menú, Pedidos, Perfil) con componentes de Material 3."
**Que genero la IA:** Estructura de `NavigationBar` con selección dinámica de icono según el destino activo.
**Que tuve que corregir yo:** Reemplacé los colores genéricos por los tokens de la paleta corporativa (`indicatorColor` y `selectedIconColor`).
**Respeta MVVM?:** Sí, la barra reacciona al estado de navegación expuesto.

## Prompt 22
**Texto enviado a Gemini:** "Diseña un `SplashScreen` que consulte mediante `CafeRepository` si hay una sesión activa para enrutar al Menú o al Login."
**Que genero la IA:** El Composable `SplashScreen` invocando directamente a `CafeRepository` dentro de un `LaunchedEffect`.
**Que tuve que corregir yo:** **Violación de MVVM cometida por la IA.** La vista no debe comunicarse con el repositorio. Creé `SplashViewModel` para verificar la sesión en la capa de negocio y emitir el evento de navegación a la vista.
**Respeta MVVM?:** Corregido. La vista consulta únicamente a su ViewModel correspondiente.

## Prompt 23
**Texto enviado a Gemini:** "Adapta las pantallas de Login, Registro, Confirmar Pedido y Seguimiento para el modo oscuro utilizando `MaterialTheme.colorScheme`."
**Que genero la IA:** Sustitución de colores estáticos por variables del tema dinámico.
**Que tuve que corregir yo:** Ajusté la opacidad de los bordes e iconos de los campos de texto para mantener contraste adecuado sobre fondos oscuros.
**Respeta MVVM?:** Sí, es una adaptación del sistema de diseño visual.

## Prompt 24
**Texto enviado a Gemini:** "En el panel de administración, optimiza la visibilidad de los reportes y tarjetas informativas en tema oscuro según Material 3."
**Que genero la IA:** Aplicación de los roles de color `surfaceVariant` y `onSurfaceVariant` a las tarjetas del dashboard.
**Que tuve que corregir yo:** Corregí los colores de fuente dentro del Canvas del gráfico de ventas por hora para asegurar su legibilidad sobre fondos oscuros.
**Respeta MVVM?:** Sí, refactorización exclusiva de temas y estilos visuales.

## Prompt 25
**Texto enviado a Gemini:** "Integra la librería Coil con `AsyncImage` para la carga de imágenes de productos almacenadas localmente en la app."
**Que genero la IA:** Incorporación de `AsyncImage` en `ProductoCard` leyendo la ruta guardada en el modelo de producto.
**Que tuve que corregir yo:** Configuré imágenes de reserva (*placeholders*) para manejar rutas vacías o no encontradas.
**Respeta MVVM?:** Sí, la vista renderiza la ruta emitida por la base de datos sin procesarla.

## Prompt 26
**Texto enviado a Gemini:** "En `MenuScreen` cuando no haya productos al buscar, ¿debería ver un mensaje no? donde diga q no hay."
**Que genero la IA:** Implementó el contenedor `Box` informativo de estado vacío en `MenuScreen`.
**Que tuve que corregir yo:** Reintegré los colores del esquema del proyecto (`onSurfaceVariant`) para mantener coherencia estético-visual en ambos temas.
**Respeta MVVM?:** Sí, la vista reacciona al estado emitido por el ViewModel.

## Prompt 27
**Texto enviado a Gemini:** "Añade una X en el campo `CafeTextField` para limpiar la búsqueda de un solo toque."
**Que genero la IA:** El `trailingIcon` con `IconButton` en el componente de texto.
**Que tuve que corregir yo:** Conectar el evento de limpieza para restablecer la variable de búsqueda en el `MenuViewModel`.
**Respeta MVVM?:** Sí, la acción notifica al ViewModel y refresca el `StateFlow`.

## Prompt 28
**Texto enviado a Gemini:** "Estructura los espacios, márgenes y tarjetas de productos en la grilla de `MenuScreen` para lograr una visualización uniforme."
**Que genero la IA:** Aplicación de `Arrangement.spacedBy(16.dp)` y padding periférico en la grilla.
**Que tuve que corregir yo:** Ajustar la altura máxima de las tarjetas para evitar desalineación entre elementos.
**Respeta MVVM?:** Sí, maquetación visual en Compose.

## Prompt 29
**Texto enviado a Gemini:** "Establece una separación constante entre las tarjetas de pedidos en `SeguimientoScreen` para facilitar el seguimiento."
**Que genero la IA:** Configuración de espaciados verticales en `LazyColumn`.
**Que tuve que corregir yo:** Homologar la elevación y bordes con las tarjetas del menú principal.
**Respeta MVVM?:** Sí, ajuste exclusivo de interfaz.

## Prompt 30
**Texto enviado a Gemini:** "Acomoda las tarjetas y el contenido en la sección de `MisPedidosScreen` para estructurar mejor el historial."
**Que genero la IA:** Rediseño del contenedor de tarjetas de pedidos del cliente.
**Que tuve que corregir yo:** Revisar el contraste de los textos secundarios y badges de estado en modo oscuro.
**Respeta MVVM?:** Sí, cambio de presentación en el Composable.

## Prompt 31
**Texto enviado a Gemini:** "En `PedidosAdminScreen` el icono de usuario se ve opaco al cambiar a modo oscuro. ¿Cómo mejoro su visibilidad?"
**Que genero la IA:** Propuso ajustar la propiedad de color del icono.
**Que tuve que corregir yo:** Asignar explícitamente `tint = MaterialTheme.colorScheme.secondary` en el Composable para garantizar contraste.
**Respeta MVVM?:** Sí, modificación visual en la vista.


## Reflexión

- **Qué mejoró la IA y qué no:** La IA mejoró notablemente la rapidez para generar entidades de Room, consultas SQL y la estructura base de los ViewModels, pero no respetó siempre la arquitectura ni los estilos de la interfaz.
- **Qué errores cometió:** Intentó acceder al DAO y a la interfaz `CafeRepository` directamente desde los Composables (en lugar de pasar por el ViewModel) y colocó reglas de negocio, como la validación del horario de atención, en la capa de la vista.
- **Qué aprendí sobre MVVM:** Aprendí que la vista solo habla con su ViewModel mediante `StateFlow`, y que todas las validaciones y reglas de negocio deben ir en la capa lógica, nunca en la UI.
- **Qué aprendí sobre Room:** Entendí que usar `Flow<User?>` permite manejar la sesión de forma reactiva (incluso cuando no hay usuario), y que el aislamiento del carrito o la numeración correlativa de pedidos corresponden a la capa de datos.
- **Conclusión:** La IA ahorra muchísimo tiempo en código repetitivo, pero requiere una supervisión constante para garantizar que no rompa el desacoplamiento entre capas.
