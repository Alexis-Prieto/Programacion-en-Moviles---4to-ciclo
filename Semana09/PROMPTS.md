# PROMPTS.md — Fase 2 con IA

**Proyecto:** MuroCampus — Laboratorio 09  
**Rama:** `mejora-ia`  
**Herramienta principal de IA:** Gemini en Android Studio  
**Herramienta de apoyo:** ChatGPT  
**Arquitectura:** MVVM + Repository  
**API:** JSONPlaceholder

## Registro de prompts y decisiones

En esta fase realicé cuatro mejoras sobre la aplicación de la Fase 1. Utilicé Gemini para implementar las mejoras y ChatGPT como apoyo para revisar y corregir un detalle de la edición. Los prompts de la tabla están resumidos, conservando las indicaciones principales de cada consulta. Revisé el código generado y decidí qué conservar y qué corregir.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| **Mejora 1: Búsqueda.** Implementa un campo de búsqueda que filtre las publicaciones por título mientras escribo, sin distinguir mayúsculas de minúsculas y sin realizar nuevas peticiones a la API. Conserva el filtro por usuario y los estados de la pantalla. Modifica únicamente `PostsViewModel.kt` y `PantallaLista.kt`. | Agregó un `StateFlow` para el texto de búsqueda, una lista con las publicaciones descargadas y un filtro mediante `contains` con `ignoreCase = true`. Incorporó el campo de búsqueda, un botón para limpiarlo y un mensaje cuando no hay resultados. | Acepté la propuesta y conservé la búsqueda local en el ViewModel. No agregué otro endpoint ni nuevas dependencias. | Las publicaciones ya están descargadas, por lo que no es necesario consultar la API cada vez que se escribe. La pantalla solo muestra los resultados y comunica las acciones al ViewModel. |
| **Mejora 2: Borrar publicación.** Agrega un botón Eliminar en el detalle utilizando `@DELETE("posts/{id}")` y `Response<Unit>`. Comprueba el código de respuesta en el Repository, maneja los estados desde el ViewModel y solicita confirmación antes de eliminar. | Implementó el endpoint DELETE, la operación en el Repository con `Result<Unit>` y la validación de `isSuccessful`. Agregó el estado de eliminación, el diálogo de confirmación y el regreso a la lista con un mensaje de éxito. | Acepté la implementación. Decidí conservar la eliminación temporal en memoria y no agregar persistencia ni un registro adicional de publicaciones eliminadas. | Es necesario comprobar que la API respondió correctamente antes de retirar la publicación de la lista. JSONPlaceholder simula la eliminación, por lo que una nueva consulta puede recuperar la publicación original. |
| **Mejora 3: Encabezado con interceptor.** Agrega un interceptor de OkHttp en `RetrofitClient.kt` que incluya `Accept: application/json` en todas las peticiones. Utiliza `header` para evitar duplicados y colócalo antes del interceptor de registro, sin modificar los endpoints ni la URL HTTPS. | Agregó un interceptor que modifica la petición mediante `newBuilder()`, incorpora el encabezado con `header` y continúa con `chain.proceed()`. Lo registró antes de `HttpLoggingInterceptor`. | Acepté el código sin cambios. Conservé la configuración de Retrofit, la URL HTTPS y el interceptor de registro existente. | Centralizar el encabezado evita repetirlo en cada endpoint. Colocarlo antes del interceptor de registro permite comprobar en Logcat que se envía con las peticiones. |
| **Mejora 4: Editar con PATCH.** Implementa una pantalla para modificar únicamente el título de una publicación mediante `@PATCH("posts/{id}")`. Muestra la respuesta del servidor, valida el título y actualiza temporalmente la lista y el detalle sin una descarga inmediata. Conserva las tres mejoras anteriores. | Creó el modelo `EditarPost`, el endpoint PATCH, el método en el Repository y un estado de edición en el ViewModel. Agregó `PantallaEditar.kt` y su navegación. La pantalla muestra el resultado de la API y el ViewModel actualiza la lista y el detalle conservando los comentarios. | Acepté la implementación de PATCH y la pantalla independiente de edición. Durante las pruebas detecté que el título original reaparecía al volver a abrir el detalle, por lo que decidí revisar el ViewModel antes de finalizar la mejora. | PATCH permite modificar únicamente el título sin reemplazar toda la publicación. Mostrar la respuesta del servidor permite comprobar el resultado de la operación. |
| **Corrección de la mejora 4 (consulta a ChatGPT).** Después de editar una publicación, el título cambia en la lista, pero al volver a abrir el detalle aparece el título original. Compartí `PostsViewModel.kt` para revisar el problema y solicité el archivo completo corregido. | **Respuesta de ChatGPT, no de Gemini:** identificó que `abrir()` consultaba nuevamente la API y propuso utilizar el título almacenado en `publicacionesOriginales` cuando estuviera disponible, conservando los demás datos recibidos del servidor y los comentarios. | Apliqué la corrección en `abrir()` de `PostsViewModel.kt` y comprobé en el celular que el título se mantenía al volver a abrir el detalle. Conservé las consultas GET y decidí no agregar persistencia para mantener los cambios al cambiar de filtro. | JSONPlaceholder no guarda las modificaciones permanentemente. Utilizar el título que ya está en memoria evita que la lista y el detalle muestren títulos diferentes, sin incorporar una base de datos ni cambiar la arquitectura. |

## Revisión del código generado

Durante la revisión de las cuatro mejoras comprobé que se mantuviera la arquitectura MVVM + Repository:

- Las pantallas muestran los estados y comunican las acciones al ViewModel, sin llamar directamente a la API.
- El ViewModel utiliza `viewModelScope` y se comunica con el Repository. No utiliza Retrofit, `HttpException` ni `GlobalScope`.
- El Repository gestiona las excepciones mediante `Result`, sin bloques `catch` vacíos, y vuelve a lanzar `CancellationException` para respetar la cancelación de las corrutinas.
- Se conservó la URL HTTPS de JSONPlaceholder.

En el código revisado no encontré los errores de arquitectura mencionados en el enunciado. La corrección que realicé fue la inconsistencia del título entre la lista y el detalle, identificada durante las pruebas de la cuarta mejora.

## Comprobaciones realizadas

| Mejora | Comprobación |
| --- | --- |
| Búsqueda | Se revisó el filtrado local por título y su integración con el filtro por usuario. |
| Eliminar con DELETE | En Logcat se comprobó la respuesta HTTP 200. La publicación se retira temporalmente de la lista. |
| Interceptor | En Logcat se verificó el encabezado `Accept: application/json` en las peticiones registradas. |
| Editar con PATCH | En Logcat se comprobó la respuesta HTTP 200 y el título actualizado devuelto por el servidor. Después de corregir `abrir()`, comprobé en el celular que la lista y el detalle conservaban el mismo título al volver a abrir la publicación. |

## Limitación de la API

JSONPlaceholder es una API de prueba que simula las operaciones de creación, edición y eliminación sin guardar los cambios permanentemente. Por eso, al cambiar el filtro por usuario y realizar una nueva consulta GET, o al reiniciar la aplicación, pueden reaparecer los datos originales. Decidí conservar este comportamiento, ya que el objetivo de las mejoras elegidas es trabajar con servicios web y no implementar una base de datos local.
