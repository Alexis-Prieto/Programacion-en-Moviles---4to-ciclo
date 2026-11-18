# Registro de prompts utilizados con Gemini — CampusGPS

**Laboratorio:** Semana 13 — CampusGPS, Fase 2  
**Rama de trabajo:** `mejora-ia`  
**Tecnologías:** Kotlin, Jetpack Compose, Google Maps Compose, MVVM + Repository  
**Paquete:** `com.prieto.campusgps`

## Objetivo

Documentar cómo se utilizó Gemini de Android Studio durante la implementación de las tres mejoras de la Fase 2, qué se aceptó, qué se ajustó y por qué. Las propuestas se revisaron mediante inspección del código y pruebas en un celular Android.

**Sobre el registro:** la primera columna identifica cada solicitud y enlaza al **Anexo A**, donde se incluyen versiones detalladas de los prompts preparados durante el trabajo. La columna «Qué respondió Gemini» resume los resultados contrastados con el código, las capturas y los reportes compartidos; no pretende ser una cita literal de toda su respuesta. Si alguna solicitud se editó antes de enviarla a Gemini, debe prevalecer el texto del historial real de Gemini.

## Tabla de prompts y decisiones

| Prompt que escribí | Qué respondió Gemini (resumen) | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **1. Filtrar por categoría.** Solicité chips «Todos», «Estudio», «Comida», «Eventos» y «Servicios», filtrado en ViewModel con `StateFlow` y actualización simultánea del mapa y la lista. [Texto completo: Prompt 1](#prompt-1--filtrado-por-categoría). | Se implementaron la categoría seleccionada y los lugares filtrados en `MapaViewModel.kt`, y los chips en `PantallaMapa.kt`. El mapa y la lista reciben los lugares visibles. | Acepté «Todos» para restaurar los cinco lugares y que «Ir al más cercano» considere la categoría activa. Conservé coordenadas y repositorios. | El laboratorio exige filtrar mapa y lista mediante cambios en **ViewModel + View**, manteniendo MVVM. |
| **2. Desplazamiento de la lista.** Solicité que «Ir al más cercano» hiciera visible la tarjeta seleccionada sin scroll manual. [Texto completo: Prompt 2](#prompt-2--desplazamiento-de-la-lista). | Se añadió el control de `LazyListState` y `animateScrollToItem()` para llevar la lista a la tarjeta seleccionada. | Acepté el desplazamiento automático, sin cambiar el cálculo de distancias ni el movimiento de la cámara. | El mapa ya enfocaba el lugar correcto, pero la tarjeta podía quedar fuera de la zona visible de la lista. |
| **3. Radio de cercanía.** Solicité el selector de 100, 200 y 500 m, un `Circle` centrado en mi posición y una marca en la lista para los lugares dentro del radio. [Texto completo: Prompt 3](#prompt-3--radio-de-cercanía). | Se añadió el estado del radio y el conjunto de lugares dentro de él en `MapaViewModel.kt`, el selector en `PantallaMapa.kt`, el círculo en `MapaUbicacion.kt` y la indicación en `ListaLugares.kt`. | Conservé visibles los lugares fuera del radio y mantuve el círculo alrededor de la ubicación del usuario, no del marcador seleccionado. | El PDF pide **marcar** los lugares cercanos, no ocultar los lejanos ni mover el centro del círculo. |
| **4. Punto azul nativo.** Solicité activar `isMyLocationEnabled` y el botón nativo solo con permiso, conservando las mejoras anteriores. [Texto completo: Prompt 4](#prompt-4--punto-azul-nativo). | Se usaron `MapProperties` y `MapUiSettings` condicionados al estado del permiso; la View comunica ese permiso al mapa. | Acepté el punto azul nativo sin añadir seguimiento continuo de ubicación. | La tercera mejora exige modificar **solo View** y condicionar la capa de ubicación al permiso concedido. |
| **5. Revocación del permiso.** Solicité ocultar el punto azul, el botón nativo, las coordenadas y el círculo al revocar el permiso, y recuperarlos al concederlo. [Texto completo: Prompt 5](#prompt-5--revocación-de-permisos). | La View vuelve a comprobar los permisos en `ON_RESUME`, actualiza su estado y reutiliza la gestión del permiso denegado. | Acepté impedir la visualización de datos anteriores y conservé el resto de la aplicación. Probé revocación y recuperación del permiso. | Una ubicación anterior no debe mostrarse como vigente cuando el usuario revoca la autorización. |
| **6. Corrección final del círculo.** Solicité condicionar el `Circle` al permiso y al estado `UbicacionUi.Lista`, además de eliminar el antiguo marcador rojo. [Texto completo: Prompt 6](#prompt-6--círculo-y-marcador-anterior). | Gemini informó que modificó solo `MapaUbicacion.kt`, eliminó el marcador rojo y que `:app:assembleDebug` terminó con `BUILD SUCCESSFUL`. | Acepté ambas correcciones y mantuve los marcadores de los cinco lugares. | El punto azul nativo reemplaza el marcador rojo y el círculo no debe dibujarse sin permiso. |

## Ajustes propios y decisiones adicionales

Estos cambios se revisaron o realizaron fuera de los seis prompts principales; **no se presentan como respuestas de Gemini**.

- **Pantallas de error:** se ajustaron `PantallaMapa.kt` y `MensajeError.kt` para ocultar mapa, chips y lista cuando ocurre un error y mostrar el mensaje correspondiente con «Abrir ajustes» o «Reintentar», conforme a la referencia visual del laboratorio.
- **Limpieza de datos al revocar el permiso:** se modificó `permisoDenegado()` en `MapaViewModel.kt` para volver a dejar las distancias en `null`, limpiar la selección y actualizar la lista filtrada. Se comprobó que las distancias vuelven a calcularse después de obtener nuevamente la ubicación.
- **Precisión del celular:** se detectó que una posición aparentemente incorrecta se debía a tener desactivada la opción «Ubicación precisa» en los ajustes de Android. Se activó sin cambiar el código de obtención de ubicación.

## Verificaciones realizadas y límite de las pruebas

- **Filtro por categoría:** se comprobó que los chips actualizan los lugares del mapa y de la lista, y que «Ir al más cercano» utiliza la categoría activa.
- **Radio de cercanía:** se comprobó visualmente que el círculo cambia al seleccionar 100, 200 y 500 m y permanece centrado en la ubicación obtenida.
- **Punto azul nativo:** se comprobó su aparición con permiso y su desactivación al revocarlo, así como la recuperación al volver a concederlo.
- **Manejo de errores:** se probó la presentación de los estados de error y la recuperación de la pantalla normal.
- **Arquitectura:** los cambios de filtrado y radio se mantuvieron en ViewModel + View; la activación del punto azul nativo se mantuvo en View. Las correcciones propias de manejo de datos se identifican por separado.
- **Prueba aún pendiente:** comprobar físicamente cerca de TECSUP que una tarjeta indique «Dentro del radio» cuando su distancia sea menor o igual a 100, 200 o 500 m.
- **Control de versiones:** las mejoras se trabajaron en `mejora-ia` y se previeron commits separados; para acreditar el historial exacto se utiliza `git log --oneline mejora-ia -- Semana13`.

**Seguridad:** los prompts documentados no contienen la clave API de Google Maps ni datos secretos de configuración.

---

## Anexo A — Versiones detalladas de los prompts preparados para Gemini

Los siguientes textos recuperan y organizan las instrucciones preparadas durante el desarrollo; no se presentan como transcripciones literales del historial de Gemini. Se conservan para que la primera columna de la tabla tenga una referencia más detallada. Si se editó algún texto antes de enviarlo a Gemini, corresponde contrastarlo con el historial real de Android Studio.

### Prompt 1 — Filtrado por categoría

> Mi proyecto Android utiliza Kotlin, Jetpack Compose, Google Maps Compose y arquitectura MVVM + Repository. El paquete raíz es `com.prieto.campusgps`.
>
> Necesito implementar **EXCLUSIVAMENTE la mejora 1: FILTRAR POR CATEGORÍA**.
>
> Según el PDF, esta mejora debe modificar solo las capas ViewModel + View. Deben existir chips para las categorías Estudio, Comida, Eventos y Servicios, además de la opción Todos para restablecer el filtro.
>
> Requisitos: (1) la opción inicial será Todos y mostrará los cinco lugares; (2) el filtrado debe realizarse en `MapaViewModel.kt` mediante `StateFlow`; (3) `PantallaMapa.kt` debe mostrar los chips y observar la lista filtrada; (4) la lista y los marcadores del mapa deben mostrar los mismos lugares filtrados; (5) conservar el marcador de ubicación actual; (6) conservar el orden por distancia; (7) mantener la selección y «Ir al más cercano» coherentes con el filtro; (8) conservar el diseño azul; (9) no modificar repositorios, modelos, utilidades, permisos, API Key ni coordenadas; (10) no implementar las demás mejoras; (11) respetar MVVM, sin `Context` en ViewModel ni cálculos de distancia en Composables.
>
> Analiza primero `MapaViewModel.kt`, `PantallaMapa.kt`, `MapaUbicacion.kt` y `ListaLugares.kt`. Implementa directamente los cambios mínimos necesarios. Comprueba las importaciones y la compilación. Explica qué archivos modificaste y cómo probar los filtros en el celular. **No hagas commit ni push**.

### Prompt 2 — Desplazamiento de la lista

> Necesito corregir **UN ÚNICO detalle** de la interfaz de CampusGPS en la rama `mejora-ia`.
>
> Actualmente, el botón «Ir al más cercano» funciona correctamente: centra el mapa en el lugar correspondiente y resalta su tarjeta con un borde azul.
>
> **Único problema:** cuando la lista está desplazada hacia abajo y presiono «Ir al más cercano», tengo que hacer scroll manualmente para encontrar la tarjeta seleccionada.
>
> **Corrección requerida:** al presionar «Ir al más cercano», la `LazyColumn` debe desplazarse automáticamente hasta la tarjeta del lugar más cercano, para que quede visible.
>
> Realiza el cambio mínimo necesario en la capa View, utilizando `LazyListState` y `animateScrollToItem()` si corresponde.
>
> No modifiques el comportamiento del mapa ni del botón; no cambies marcadores, distancias o filtros; no modifiques ViewModel, Repository ni modelos; no implementes otras mejoras; **no hagas commit ni push**.

### Prompt 3 — Radio de cercanía

> Estoy trabajando en la Fase 2 del Laboratorio 13 – CampusGPS, en la rama `mejora-ia`. Mi aplicación utiliza Kotlin, Jetpack Compose, Google Maps Compose y arquitectura MVVM + Repository; el paquete raíz es `com.prieto.campusgps`.
>
> La primera mejora, «Filtrar por categoría», ya funciona. Implementa **únicamente la mejora 2: RADIO DE CERCANÍA**, que según el PDF modifica ViewModel + View.
>
> Agrega un selector de **100, 200 y 500 metros**. Administra el radio mediante `StateFlow` en `MapaViewModel.kt` y determina cuáles lugares están dentro del radio reutilizando las distancias actuales. En `MapaUbicacion.kt`, dibuja un `Circle` centrado en la posición obtenida del usuario y actualízalo cuando cambie el radio. En `ListaLugares.kt`, identifica los lugares dentro del radio, por ejemplo con «Dentro del radio».
>
> Los lugares fuera del radio deben permanecer visibles; esta mejora los identifica, no los elimina. Debe funcionar junto con los filtros de categoría. Si aún no hay ubicación, no dibujes un círculo con coordenadas inventadas ni marques lugares cercanos. Conserva el orden por distancia y el botón «Ir al más cercano».
>
> No modifiques repositorios, modelos, utilidades, coordenadas, permisos, API Key ni configuración de Maps. No alteres chips, marcadores, scroll ni el punto azul nativo, que corresponde a otra mejora. Mantén MVVM y el diseño actual. Analiza los archivos existentes, realiza cambios mínimos, verifica que compile, indica qué archivos cambiaste y explica cómo probarlo. **No hagas commit ni push**.

### Prompt 4 — Punto azul nativo

> Estoy trabajando en la Fase 2 del Laboratorio 13 – CampusGPS, en la rama `mejora-ia`. Uso Kotlin, Jetpack Compose, Google Maps Compose y arquitectura MVVM + Repository. El paquete raíz es `com.prieto.campusgps`.
>
> Ya funcionan las mejoras de filtrado por categoría y radio de cercanía. Implementa **únicamente la mejora 3: PUNTO AZUL NATIVO**, modificando **solo la capa View**.
>
> Revisa los permisos en `PantallaMapa.kt` y el mapa en `MapaUbicacion.kt`. Activa `MapProperties(isMyLocationEnabled = true)` y `MapUiSettings(myLocationButtonEnabled = true)` **solo si el permiso de ubicación fue concedido**; sin permiso ambas propiedades deben quedar desactivadas. Actualiza el estado cuando el usuario conceda o rechace permisos.
>
> Evita superponer el antiguo marcador rojo con el punto azul nativo. El botón nativo debe permitir volver a la posición actual. Conserva el botón «Obtener mi ubicación», los chips, el radio, las distancias y «Ir al más cercano».
>
> No modifiques `MapaViewModel.kt`, repositorios, modelos, utilidades, permisos de Manifest, API Key ni configuración de Google Maps. No agregues seguimiento en vivo ni otras mejoras. Respeta MVVM, realiza cambios mínimos, verifica la compilación y explica cómo probar el punto azul y el botón en el celular. **No hagas commit ni push**.

### Prompt 5 — Revocación de permisos

> Necesito realizar una corrección mínima en la Mejora 3: Punto azul nativo de CampusGPS, dentro de la rama `mejora-ia`.
>
> Actualmente, el punto azul nativo y el botón de ubicación funcionan correctamente. Sin embargo, quiero mejorar el comportamiento cuando el usuario revoca el permiso de ubicación desde los ajustes del celular.
>
> Aunque `permisoUbicacionConcedido` se actualiza al regresar a la aplicación, el estado anterior del ViewModel podría conservar las últimas coordenadas y permitir que se siga mostrando el círculo alrededor de una posición antigua.
>
> Detecta cuando el permiso pasa de concedido a denegado; desactiva inmediatamente `isMyLocationEnabled` y `myLocationButtonEnabled`; evita mostrar las coordenadas y el círculo antiguos; reutiliza `vm.permisoDenegado()` cuando corresponda; y permite volver a obtener la ubicación al conceder el permiso nuevamente.
>
> Conserva los filtros y el radio. Esta corrección corresponde a la capa View: modifica solo `PantallaMapa.kt` y `MapaUbicacion.kt` si hace falta. No modifiques ViewModel, Repository, modelos ni utilidades; no agregues funciones nuevas. Realiza cambios mínimos, explica cómo probar la revocación y recuperación. **No hagas commit ni push**.

### Prompt 6 — Círculo y marcador anterior

> Revisa `MapaUbicacion.kt` de CampusGPS, específicamente la lógica del círculo de cercanía y el marcador antiguo de ubicación del usuario.
>
> Actualmente el círculo se dibuja cuando `estado is UbicacionUi.Lista`, y dentro existe un bloque `if (!permisoUbicacionConcedido)` que podría mostrar el marcador rojo anterior.
>
> Realiza únicamente estas dos correcciones: **(1)** dibuja el `Circle` solamente cuando `permisoUbicacionConcedido` sea `true` y `estado` sea `UbicacionUi.Lista`; **(2)** elimina el marcador rojo personalizado del usuario, porque ya utilizamos el punto azul nativo de Google Maps.
>
> Modifica solo `MapaUbicacion.kt`. Conserva los marcadores de los lugares del campus, los radios, colores, dimensiones, punto azul y botón nativo. No cambies ViewModel, Repository ni otros archivos. Verifica la compilación. **No hagas commit ni push**.
