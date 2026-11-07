# 📱 Laboratorio 12 - App de Pokémon

**Tema:** Pokémon (PokeAPI)
**Variante:** Tipo [fire / water / grass]

## ¿De qué trata la app?
Es una aplicación para Android que permite buscar y ver Pokémon usando la API oficial de PokeAPI. La app tiene un sistema de inicio de sesión con dos tipos de usuarios: **Usuario** y **Administrador**.
Los Pokémon se guardan en la memoria del celular (Room), por lo que la app sigue funcionando aunque te quedes sin internet, mostrando un aviso para que el usuario lo sepa.

## ¿Qué puede hacer cada usuario?
Dependiendo de con qué cuenta inicies sesión, la app te mostrará diferentes opciones:

| Acción | Usuario | Administrador |
| :--- | :---: | :---: |
| Entrar con correo y contraseña | Sí | Sí |
| Ver la lista y el detalle de los Pokémon | Sí | Sí |
| Marcar y ver tus Pokémon favoritos | Sí | Sí |
| Cerrar sesión | Sí | Sí |
| **Entrar al Panel de Administración** | **No** | **Sí** |

*Nota: Si entras como usuario normal, el botón de "Admin" ni siquiera aparece en la barra de navegación inferior.*

## ¿Cómo abrir y probar el proyecto?
1. Descarga o clona este repositorio en tu computadora.
2. Ábrelo con **Android Studio**.
3. Pídeme el archivo `google-services.json` (no lo subí por seguridad) y pégalo dentro de la carpeta `app/`.
4. Dale clic al botón verde de "Play" para ejecutarlo en un celular o emulador.

## ⚙Usuarios de prueba (Firebase)
Para que el profesor pueda probar la app, estos son los usuarios que creé en Firebase:

*   **Usuario normal:** `alexisprietohuiza0826@gmail.com` / Contraseña: `usuario1234`
*   **Administrador:** `stephano@admin.com` / Contraseña: `123456`

*(Nota: Si se desea crear un usuario nuevo, se debe registrar en Firebase Authentication y luego asignarle el campo `rol` en la colección `usuarios` de Firestore).*

## Capturas de la app funcionando

*   **Pantalla de Login:** `![Login](drawable/login.png)`
*   **Lista de Pokémon:** `![Lista](drawable/lista.png)`
*   **Detalle del Pokémon:** `![Detalle](drawable/detalle.png)`
*   **Pantalla de Favoritos:** `![Favoritos](drawable/favoritos.png)`
*   **Panel de Administración:** `![Admin](drawable/admin.png)`
*   **Notificación al Sincronizar:** `![Notificacion](drawable/notificacion.png)`

## Desarrollo del proyecto
Este laboratorio fue desarrollado de manera individual. Me encargué de todas las etapas del proyecto:
*   **Diseño de interfaz (UI):** Creación de las pantallas en Jetpack Compose con Material 3, incluyendo la barra de navegación adaptativa según el rol.
*   **Lógica y Datos:** Configuración de Room para guardar los datos locales y Retrofit para consumir la API de PokeAPI.
*   **Autenticación:** Configuración de Firebase Auth y Firestore para el login y la asignación de roles (Admin/Usuario).
*   **Notificaciones:** Implementación del canal y permiso `POST_NOTIFICATIONS` para avisar al usuario cuando termina la sincronización.