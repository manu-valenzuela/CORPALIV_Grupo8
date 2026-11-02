# CORPALIV
Trabajo semestral para el curso Desarrollo de Aplicaciones Móviles (DSY1105) de Ingeniería en Informática, DuocUC.

## Integrantes

Sebastián Rojo

Manuel Valenzuela

## Asignación de tareas del proyecto

https://github.com/users/manu-valenzuela/projects/2

## Descripción de la App

```
CORPALIV

cl.duoc.corpaliv
├── MainActivity.kt                       → Punto de entrada y configuración de WindowSizeClass[cite: 24]
├── model/                                → Modelos de dominio y Data Transfer Objects
│   ├── DonacionExpress.kt                → Modelo de donación rápida[cite: 23]
│   ├── DonacionUiState.kt                → Estado UI y errores de donación[cite: 22]
│   ├── Evento.kt                         → Entidad de evento institucional[cite: 21]
│   ├── Noticia.kt                        → Entidad de noticia[cite: 20]
│   ├── Producto.kt                       → Entidad de producto de la tienda[cite: 20]
│   ├── Usuario.kt                        → Entidad Usuario, RolUsuario y OpcionDonacionMensual[cite: 19]
│   └── Venta.kt                          → Entidad de registro de venta comercial[cite: 18]
├── repository/                           → Fuentes de datos y Persistencia Local
│   ├── DonacionDataStore.kt              → Almacenamiento local de donaciones (DataStore)
│   ├── RepositorioNoticias.kt            → Repositorio de contenidos informativos
│   ├── RepositorioVenta.kt               → Gestión de catálogo y ventas en memoria[cite: 17]
│   └── UsuarioDataStore.kt               → Almacenamiento y actualización de usuarios[cite: 16]
├── viewmodel/                            → Capa de Lógica de Presentación (MVVM)
│   ├── ViewModelInicio.kt                → Gestión de inicio, noticias y donaciones[cite: 24]
│   ├── ViewModelTienda.kt                → Lógica comercial y compras[cite: 24]
│   └── ViewModelUsuario.kt               → Lógica de registro y roles de usuario[cite: 24]
├── ui/
│   ├── screens/                          → Pantallas UI y Componentes Reusables
│   │   ├── AndamiajeAplicacion.kt        → Scaffold, TopBar y NavigationBar reactiva[cite: 40]
│   │   ├── PantallaGestionDonaciones*.kt → Vista compacta/adaptativa de donaciones[cite: 39]
│   │   ├── PantallaGestionUsuarios*.kt   → Vista compacta/adaptativa de administración[cite: 38]
│   │   ├── PantallaInicio*.kt            → Home con noticias y Donación Express[cite: 37]
│   │   ├── PantallaRegistroSocio*.kt     → Formulario con captura GPS[cite: 36]
│   │   ├── PantallaResumenDonacion*.kt   → Vista de confirmación de aporte[cite: 34, 35]
│   │   ├── PantallaSeleccionRol*.kt      → Selector inicial de modo de acceso[cite: 33]
│   │   ├── PantallaTienda*.kt            → Vista comercial socio[cite: 31, 32]
│   │   ├── PantallaVentas*.kt            → Vista comercial funcionario[cite: 29, 30]
│   │   └── Tarjeta*.kt                   → Componentes modulares (Producto, Usuario, Evento)[cite: 26, 27, 28]
│   ├── theme/                            → Sistema de Diseño Material 3 (Color, Type, Theme)[cite: 25]
│   └── utils/                            → NavegacionAplicacion, RutaAplicacion y ManejadorUbicacion[cite: 7, 24]

```

## Instrucciones de ejecución

1. Requisitos Previos en Android StudioVersión de Android Studio: Se recomienda una versión reciente (Koala, Ladybug o superior) con soporte para JDK 11/17 y Jetpack Compose.Versión de Android en Emulador/Dispositivo: En la configuración del proyecto (app/build.gradle.kts), el minSdk está fijado en API 34.

2. Apertura y Sincronización del Proyecto
   
   Obtener el código: Ejecutar git clone https://github.com/manu-valenzuela/corpaliv_grupo8 en tu terminal.
   
   Abrir en Android Studio: Inicia Android Studio y selecciona Open. Navega hasta la carpeta raíz del proyecto (CORPALIV_Grupo8) y haz clic en OK.
   
   Sincronización de Gradle: Espera a que Android Studio descargue las dependencias e indexe el proyecto.

4. Configuración del Emulador (AVD)
   
   Abre el Device Manager desde el panel derecho de Android Studio.Haz clic en Create Device (ejemplo: Pixel 6).
   
   Selecciona una imagen de sistema con API Level 34 (UpsideDownCake).
   
   Finaliza la creación y enciende el emulador.
   
   Compilación y EjecuciónEn la barra superior, confirma que esté seleccionada la configuración de ejecución app y el emulador creado.
   
   Haz clic en el botón de reproducción Run 'app' (o presiona Shift + F10).

6. Guía de Prueba Paso a Paso por Funcionalidad
   
   A. Selección de Rol e IngresoAcceso: Al abrir la app, verás la pantalla inicial de selección de rol.
   
       - Prueba: Ingresa primero como "Acceder como Socio / Invitado".
   
   B. Flujo de Socio / InvitadoInicio y Donación Express:
   
       - Revisa el listado de noticias y eventos cargados. En el bloque de Donación Express, intenta presionar el botón sin llenar los campos para comprobar las           validaciones. Ingresa un nombre, correo y monto válido, y presiona confirmar.
   
       - Tienda Solidaria: En la barra de navegación inferior, selecciona Tienda. Cambia la cantidad de unidades (+ / -) de un producto para verificar el cálculo          dinámico del subtotal. Presiona Confirmar Compra para generar la transacción.
   
       - Registro de Socio con GPS: Selecciona Registrarse en el menú inferior. Completa los campos del formulario. Otorga el permiso de ubicación cuando la app           lo solicite y presiona el botón para capturar las coordenadas GPS (latitud y longitud) en tiempo real mediante los servicios nativos del dispositivo.
   
   C. Flujo de Funcionario / Intranet
   
       - Cambiar a Modo Funcionario: Vuelve a Inicio en el menú inferior y selecciona la opción para cambiar de rol, o reinicia la app e ingresa como                      Funcionario.
   
       - Gestión de Usuarios: Selecciona la pestaña Usuarios en la barra inferior. Visualiza los usuarios registrados. Haz clic en los roles (Socio, Voluntario, Funcionario) para cambiar dinámicamente el rol de un usuario y verificar que el cambio se guarde localmente.
   
       - Gestión de Donaciones: Selecciona la pestaña Donaciones para auditar las donaciones express realizadas durante las pruebas anteriores.
   
       - Gestión de Ventas: Selecciona Ventas para revisar el resumen financiero acumulado y el detalle de compras registradas en la tienda.   
