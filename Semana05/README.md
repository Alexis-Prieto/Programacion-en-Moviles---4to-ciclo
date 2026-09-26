**Prompt final utilizado para la mejora visual y lógica de la primera parte.**

Actúa como un Desarrollador Senior de Android especializado en Jetpack Compose y Material 3.

Necesito refactorizar y generar el código fuente completo, listo para producción y totalmente funcional de una aplicación móvil académica modularizada en las siguientes 5 pantallas y su sistema de diseño.

Proporciona el código Kotlin completo con todas sus importaciones para los siguientes archivos, siguiendo estrictamente estas especificaciones técnicas:

1. Tema y Sistema Visual (ui/theme/Color.kt):
- Define la paleta de colores oficial: PrimaryPurple = Color(0xFF5B439B), DarkPurple = Color(0xFF321A5C), LightLilac = Color(0xFFDED0F2), CardBackground = Color(0xFFEDE7F2) y ErrorRed = Color(0xFFB00020).
- Crea un fondo general reusable con degradado vertical (Brush.verticalGradient) que vaya desde LightLilac hasta blanco puro (Color.White).

2. Pantalla 1: Login / Autenticación (screens/LoginScreen.kt):
- Contenedor con fondo degradado y un ElevatedCard flotante centrado (esquinas de 28.dp, elevación de 8.dp).
- Título "Portal Académico" y subtítulo "Accede a tu cuenta".
- Campos OutlinedTextField para "Correo Institucional" (ícono Email) y "Contraseña" (ícono Lock con IconButton posterior interactivo para ocultar/mostrar texto mediante PasswordVisualTransformation).
- Lógica de Validación: Estado interno errorMessage. Valida exactamente las credenciales alexis.prieto@tecsup.edu.pe / 123. Si son incorrectas o están vacías, muestra un Text dinámico centrado en color ErrorRed. Al validar exitosamente, navega a HomeScreen limpiando la pila con popUpTo(Screen.Login.route) { inclusive = true }.

3. Pantalla 2: Inicio / Dashboard (screens/HomeScreen.kt):
- Encabezado con bienvenida al usuario autenticado y resumen del portal académico.
- Tarjetas de acceso rápido (ElevatedCard) organizadas visualmente para navegar hacia el "Directorio de Alumnos" (ListScreen) o al "Perfil Personal" (ProfileScreen).

4. Pantalla 3: Directorio de Alumnos (screens/ListScreen.kt):
- Scaffold con TopAppBar en tono lila sólido (LightLilac), título "Directorio de Alumnos" y botón de retorno.
- Renderiza una lista con LazyColumn a partir del modelo StudentData (id, name, career, code, avatarUrl).
- Diseña cada ítem con ElevatedCard interactivo (clickable).
- Usa AsyncImage de Coil para descargar avatares dinámicos desde URLs de Unsplash con recorte circular (CircleShape, ContentScale.Crop, tamaño 50.dp).
- Muestra el nombre en negrita, la carrera en tono morado (PrimaryPurple) y un ícono indicando navegación (KeyboardArrowRight).

5. Pantalla 4: Detalle del Estudiante (screens/DetailScreen.kt):
- Recibe el studentId parametrizado desde el NavHost.
- Encabezado superior con botón de navegación hacia atrás.
- Muestra la foto de perfil del estudiante seleccionado en alta resolución usando AsyncImage (120.dp, recorte circular).
- Organiza los detalles del alumno (Código, Carrera, Estado Académico) en tarjetas informativas independientes (ElevatedCard) estructuradas con íconos descriptivos y jerarquía tipográfica clara.

6. Pantalla 5: Perfil de Usuario (screens/ProfileScreen.kt):
- Vista del perfil del usuario en sesión.
- Avatar institucional cargado asíncronamente con AsyncImage de Coil en formato circular.
- Tarjetas informativas con los datos del usuario logueado (alexis.prieto@tecsup.edu.pe), rol institucional y opción para cerrar sesión navegando de vuelta a LoginScreen.

7. Configuración de Rutas y Dependencias (navigation/AppNavigation.kt y Gradle):
- Incluye la declaración de la clase sellada Screen con rutas tipo-seguras y paso de argumentos.
- Especifica la inclusión de la dependencia io.coil-kt:coil-compose y el permiso INTERNET en el Manifest.

Genera el código fuente en Kotlin estructurado, limpio y listo para integrarse directamente en Android Studio.



## CAPTURAS DE PANTALLAS DE LA APLICACIÓN EN EJECUCIÓN

<img width="687" height="641" alt="image" src="https://github.com/user-attachments/assets/5e3a0926-7eb7-4c5f-a407-2d1ad0c095a7" />

<img width="705" height="665" alt="image" src="https://github.com/user-attachments/assets/17a57a43-4333-4e3b-b7f4-652c03d471b9" />

<img width="942" height="625" alt="image" src="https://github.com/user-attachments/assets/4326c428-4dfc-4708-9056-1cf7f2b87cd0" />



