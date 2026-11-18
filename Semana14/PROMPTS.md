# PROMPTS.md — Fase 2 con IA

**Proyecto:** CentroTareas — Laboratorio 14: Hilos en Android  
**Tecnologías:** Kotlin, Jetpack Compose, corrutinas y arquitectura MVVM + Repository  
**Paquete:** `com.prieto.centrotareas`  
**Rama:** `mejora-ia`  
**Herramienta de IA:** Gemini en Android Studio

## Objetivo

Documentar los prompts importantes utilizados con Gemini durante la Fase 2, sus propuestas, las decisiones tomadas y los motivos técnicos. Se realizaron tres mejoras sobre la aplicación desarrollada en la Fase 1, procurando conservar sus funcionalidades y la arquitectura MVVM + Repository.

**Nota de registro:** la columna «Prompt que escribí» expresa las instrucciones esenciales de cada consulta en estilo directo y resumido; no es una transcripción literal palabra por palabra del historial de Gemini.

## Mejora 1. Comparación de tiempos

**Objetivo:** guardar los últimos tiempos exitosos de las cargas «Una por una» y «A la vez», mostrar su diferencia e identificar cuál fue más rápida.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **Prompt 1 — Análisis (resumen).** «Revisa `TareasViewModel.kt`, `PantallaTareas.kt`, `Estados.kt` y `TareasRepositoryLocal.kt`. Analiza cómo se ejecutan y miden las cargas “Una por una” y “A la vez”. Propón una manera de guardar sus últimos tiempos exitosos, calcular la diferencia y mostrar qué modalidad fue más rápida. Respeta MVVM + Repository, los `StateFlow`, la cancelación y el diseño. No modifiques archivos, no compiles ni ejecutes Git; primero explica el plan». | Identificó que ambas modalidades ya medían su duración, pero el resultado de una reemplazaba al de la otra en un único `CargaUi`. Propuso un modelo `ComparacionTiemposUi`, un `StateFlow` en el ViewModel y una sección comparativa en la pantalla. | **Acepté** modificar solamente `Estados.kt`, `TareasViewModel.kt` y `PantallaTareas.kt`. **Descarté** modificar el Repository o agregar persistencia para esta mejora. | Las cargas ya funcionaban y medían el tiempo. Bastaba con conservar ambos resultados exitosos y presentarlos juntos, sin cambiar el acceso a los datos. |
| **Prompt 2 — Implementación (resumen).** «Implementa la comparación de tiempos solo en `Estados.kt`, `TareasViewModel.kt` y `PantallaTareas.kt`. Crea `ComparacionTiemposUi`, conserva por separado los últimos tiempos exitosos, calcula la diferencia y el ganador, y muestra el resultado cuando ambas modalidades hayan finalizado exitosamente. Los errores posteriores no deben borrar los tiempos guardados. Conserva “Reintentar”, la cancelación y las demás tarjetas. No modifiques el Repository, no agregues persistencia ni ejecutes Git». | Implementó el modelo, el `StateFlow` y la visualización «Comparación de tiempos». Los fallos posteriores no eliminan los tiempos exitosos guardados y los nuevos éxitos actualizan únicamente su modalidad. | **Conservé** la solución tras probar ambas modalidades, el error simulado y «Reintentar». **No añadí** las otras mejoras durante este paso. | En el emulador se observaron **5010 ms** para «Una por una» y **3004 ms** para «A la vez», con una diferencia de **2006 ms**. Al simular un error, la comparación permaneció visible. |

**Archivos de esta mejora:** `model/Estados.kt`, `viewmodel/TareasViewModel.kt` y `ui/PantallaTareas.kt`.

## Mejora 2. Primos con progreso

**Objetivo:** mostrar el porcentaje real de avance mientras el Repository calcula números primos, conservando el resultado, el tiempo y la cancelación.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **Prompt 1 — Análisis (resumen).** «Analiza el cálculo de números primos en el Repository y el ViewModel. Propón cómo mostrar un avance real de 0 a 100 % en función de los candidatos procesados, sin alterar el algoritmo ni añadir demoras artificiales. Mantén `Dispatchers.Default`, `ensureActive()`, la cancelación, el número del cálculo y la comparación de tiempos. Revisa si conviene un callback suspendible y un `StateFlow` independiente. No modifiques archivos ni ejecutes Git; primero explica los cambios mínimos». | Propuso añadir a `contarPrimos()` un **callback suspendible** para comunicar el porcentaje; calcularlo con operaciones `Long`; actualizar solo cuando cambie el porcentaje entero; y exponerlo con un `StateFlow<Int>` independiente en el ViewModel. | **Acepté** el callback y un estado independiente de progreso. **Descarté** modificar `Estados.kt`, agregar dependencias o emitir un evento por cada número procesado. | Era suficiente para comunicar el avance real sin saturar la interfaz. Además, no era necesario cambiar los estados existentes ni la lógica de las otras funciones. |
| **Prompt 2 — Implementación (resumen).** «Implementa “Primos con progreso” modificando únicamente `TareasRepository.kt`, `TareasRepositoryLocal.kt`, `TareasViewModel.kt` y `PantallaTareas.kt`. Comunica desde el Repository el porcentaje calculado, actualiza un `StateFlow` independiente y muestra una barra azul con su porcentaje en Compose. Debe comenzar en 0 %, llegar a 100 % al finalizar y dejar de actualizarse al cancelar. Evita resultados antiguos después de reiniciar. Conserva el resultado y el tiempo del cálculo, además de las demás funcionalidades. No ejecutes Git». | Implementó el callback, el cálculo porcentual en el Repository, `progresoPrimos` como `StateFlow` y la barra en Compose. Conservó el algoritmo de primos y la cancelación por corrutinas. | **Conservé** la implementación después de probar un cálculo completo, la cancelación y el reinicio. **No añadí** esperas artificiales para hacer más lenta la barra. | En el emulador la barra llegó al **100 %** en un cálculo completo; al cancelar un cálculo grande, quedó en **45 %** con el mensaje «Cancelado». La interfaz conservó su respuesta y la comparación de tiempos siguió funcionando. |

**Archivos de esta mejora:** `data/TareasRepository.kt`, `data/TareasRepositoryLocal.kt`, `viewmodel/TareasViewModel.kt` y `ui/PantallaTareas.kt`.

## Mejora 3. Progreso desde el Repository

**Objetivo:** trasladar la simulación de la primera tarjeta desde el ViewModel al Repository y comunicar el progreso mediante `Flow`.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **Prompt 1 — Análisis (resumen).** «Revisa la tarjeta “Tarea con progreso” y los archivos `TareasRepository.kt`, `TareasRepositoryLocal.kt`, `TareasViewModel.kt`, `PantallaTareas.kt` y `Estados.kt`. Explica cómo trasladar los 100 pasos simulados desde el ViewModel al Repository mediante `flow { emit(...) }` y `flowOn(Dispatchers.Default)`. Mantén el mismo `StateFlow`, el `Job`, la cancelación, los mensajes y el contador “Tócame”. No afectes las Mejoras 1 y 2. No cambies archivos ni ejecutes Git; presenta el plan antes de implementar». | Propuso el contrato `ejecutarTareaConProgreso(): Flow<Int>` en el Repository, emitir de 0 a 100 con `delay(40)`, recolectar el Flow en `viewModelScope` y seguir publicando el mismo `StateFlow<Float>` que ya observa la pantalla. | **Acepté** cambiar únicamente la interfaz del Repository, su implementación y el ViewModel. **Descarté** modificar `PantallaTareas.kt`, modelos, estilos o dependencias. | La interfaz ya disponía de la barra y los botones necesarios. La mejora buscaba separar responsabilidades y no introducir una pantalla nueva. |
| **Prompt 2 — Implementación (resumen).** «Modifica únicamente `TareasRepository.kt`, `TareasRepositoryLocal.kt` y `TareasViewModel.kt`. Añade `ejecutarTareaConProgreso(): Flow<Int>`; implementa en el Repository las emisiones de 0 a 100 con `delay(40)` y `flowOn(Dispatchers.Default)`, y recolecta el Flow desde `viewModelScope.launch` usando `collect`. Reinicia el progreso al comenzar, conserva “Trabajando...”, “Terminó” y “Cancelado”, y cancela mediante el mismo `Job`. Comprueba que no se publiquen avances después de cancelar, relanza `CancellationException` y no alteres la UI ni las otras dos mejoras. No ejecutes Git ni edites `PROMPTS.md`». | Añadió el método en `TareasRepository`, implementó el Flow en `TareasRepositoryLocal` y modificó `tareaLarga()` para recolectarlo. Incluyó comprobaciones de actividad y protección contra reinicios antes de terminar una cancelación. | **Conservé** esta reorganización y **no introduje** cambios visuales ni funcionalidades adicionales. Probé la cancelación y el comportamiento de los controles existentes. | La tarea simulada queda a cargo del Repository, mientras que el ViewModel administra los estados. En el emulador se observó cancelación al **81 %** con el mensaje «Cancelado», manteniendo el contador «Tócame» independiente del progreso. |

**Archivos de esta mejora:** `data/TareasRepository.kt`, `data/TareasRepositoryLocal.kt` y `viewmodel/TareasViewModel.kt`.

## Decisiones técnicas generales

- Mantener la arquitectura **MVVM + Repository**: la pantalla observa el ViewModel y el Repository realiza las operaciones correspondientes.
- Utilizar `viewModelScope`, `StateFlow`, `Flow`, `Dispatchers.Default`, `delay`, `async` y `await` según la tarea.
- Evitar `GlobalScope`, `runBlocking` y `Thread.sleep` en las implementaciones finales de la Fase 2.
- Conservar la propagación de `CancellationException` para que las tareas se cancelen correctamente.
- No mezclar mejoras: revisar, implementar y probar una antes de continuar con la siguiente.
- No añadir dependencias o modificaciones ajenas a los requisitos.

## Resultado

Se desarrollaron tres mejoras asistidas por Gemini: **Comparación de tiempos**, **Primos con progreso** y **Progreso desde el Repository**. Los prompts de análisis permitieron delimitar los cambios antes de editar el código, y los prompts de implementación establecieron restricciones para conservar las funcionalidades de la Fase 1. La validación en el emulador permitió revisar los resultados, la cancelación y la continuidad de las funcionalidades anteriores.
