# PROMPTS.md — Fase 2 con IA

**Proyecto:** AvisosCampus — Laboratorio 15  
**Rama:** `mejora-ia`  
**Herramienta de IA:** Gemini en Android Studio  
**Arquitectura:** MVVM + Repository

## Registro de prompts y decisiones

Para esta fase utilicé Gemini para implementar tres mejoras sobre la aplicación desarrollada en la Fase 1. En la siguiente tabla se resumen las instrucciones utilizadas para cada mejora, las respuestas de Gemini y las decisiones que tomé después de revisar y probar el código generado.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| **Mejora 1: Dos canales de notificaciones.** Mantén el canal Avisos con `IMPORTANCE_DEFAULT` y crea el canal Urgentes con `IMPORTANCE_HIGH`. Los recordatorios deben usar el canal urgente y los demás avisos deben mantener el canal normal. Adapta el modelo, Repository y ViewModel sin romper MVVM. | Agregó el enum `Prioridad` con `NORMAL` y `URGENTE`. Adaptó el modelo `Aviso`, el Repository y el ViewModel. En `Notificador.kt` creó el segundo canal y seleccionó el canal según la prioridad del aviso. | Acepté la implementación sin modificar el código. Revisé que el recordatorio tenga prioridad urgente y los demás avisos prioridad normal. También conservé la configuración de prioridad de `NotificationCompat`, aunque es redundante para el `minSdk 26` del proyecto. | La prioridad se decide en el ViewModel y el Notificador se encarga de interactuar con Android. Así se mantiene la separación de responsabilidades. |
| **Mejora 2: Texto largo con BigTextStyle.** Implementa `NotificationCompat.BigTextStyle()` para mostrar el mensaje completo al expandir la notificación. Amplía el texto del recordatorio y conserva el mismo contenido en el historial. Mantén los canales, identificadores y PendingIntent existentes. | Agregó `BigTextStyle().bigText(aviso.texto)` en `Notificador.kt` y amplió el mensaje del recordatorio en `AvisosViewModel.kt`. Conservó el título, los identificadores y el `PendingIntent.FLAG_IMMUTABLE`. | Acepté el código sin cambios. Revisé que se utilizara el mismo texto del aviso para la notificación y el historial, sin agregar dependencias innecesarias. | Permite mostrar mensajes más largos sin duplicar información ni modificar la arquitectura de la aplicación. |
| **Mejora 3: Receptor del cargador.** Crea un receptor para `ACTION_POWER_CONNECTED` y `ACTION_POWER_DISCONNECTED`, agrega un Switch independiente y registra los eventos en el historial. Usa un callback y elimina el receptor mediante `onDispose`. | Creó `CargadorReceiver.kt`, agregó el origen `CARGADOR`, incorporó un estado independiente en el ViewModel y añadió el Switch en `PantallaAvisos.kt`. Registró el receptor mediante `DisposableEffect` y lo eliminó en `onDispose`. | Acepté la implementación sin cambios. Revisé que el Switch comenzara desactivado, que el ViewModel comprobara su estado antes de generar avisos y que el receptor se eliminara correctamente. | Evita generar avisos cuando la escucha está desactivada y mantiene la separación de responsabilidades. El receptor solo comunica el evento recibido y el ViewModel procesa la información. |

## Revisión del código

Revisé los seis archivos involucrados en las mejoras. Comprobé que el ViewModel no utiliza `Context` ni `NotificationManager`, que ambos receptores se eliminan mediante `onDispose` y que el `PendingIntent` sigue siendo inmutable.

También verifiqué que el aviso se registra en el Repository antes de intentar mostrar la notificación, conservando el manejo del permiso denegado.

Después de revisar el código generado por Gemini, no encontré errores funcionales que fuera necesario corregir. La compilación con `:app:assembleDebug` y las pruebas unitarias existentes con `:app:testDebugUnitTest` finalizaron correctamente.

## Pruebas de funcionamiento

Probé las tres mejoras en mi celular y comprobé los siguientes resultados:

- Los canales Avisos y Urgentes funcionan correctamente.
- El recordatorio utiliza el canal Urgentes.
- El mensaje completo se muestra al expandir la notificación.
- El receptor detecta la conexión y desconexión del cargador.
- El Switch del cargador permite controlar la recepción de los eventos.
- Los avisos generados se registran en el historial.
- Las funcionalidades de la Fase 1 siguen funcionando correctamente.

## Commits realizados

Los cambios de cada mejora se organizaron en commits independientes dentro de la rama `mejora-ia`.

- `feat(notificacion): agrega canal de avisos urgentes`
- `feat(notificacion): agrega texto expandible con BigTextStyle`
- `feat(receptor): agrega deteccion de conexion y desconexion del cargador`

Se comprobó la compilación del proyecto final, aunque no se realizaron compilaciones independientes de cada estado intermedio.

## Reflexión final

El uso de Gemini me permitió avanzar en las tres mejoras, pero fue necesario revisar y probar el código generado para comprobar que respetara las reglas del laboratorio. Aprendí a diferenciar los canales de notificaciones, mostrar mensajes extensos y detectar eventos del dispositivo, manteniendo la separación de responsabilidades de la arquitectura MVVM + Repository.
