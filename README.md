# CORPALIV

Aplicación móvil para la Fundación CORPALIV. Permite ver noticias, donar, comprar en la tienda solidaria y registrarse como socio. El personal de la fundación administra usuarios, donaciones, productos y noticias.

Proyecto del curso Desarrollo de Aplicaciones Móviles (DSY1105), DuocUC.

## Integrantes

- Manuel Valenzuela

## Funcionalidades

**Invitado**
- Ver noticias.
- Hacer una donación express.
- Ver los productos de la tienda. Para comprar debe iniciar sesión.
- Registrarse como socio (con validación de RUT y dirección por GPS).

**Socio**
- Comprar productos según el stock disponible.

**Funcionario**
- Ver usuarios y cambiar su rol.
- Ver y eliminar donaciones.
- Ver las ventas y administrar productos (crear, editar, eliminar).
- Administrar noticias (crear, editar, eliminar).

**General**
- Inicio de sesión con token JWT.
- Conversión opcional de montos a dólares y UF.

## Endpoints utilizados

### Microservicios (Spring Boot)

| Servicio | Puerto | Endpoints |
|---|---|---|
| auth | 8081 | `POST /api/auth/registro`, `POST /api/auth/login`, `GET /api/auth/cuentas`, `PATCH /api/auth/cuentas/{id}/rol` |
| usuarios | 8082 | `GET /api/usuarios`, `GET /api/usuarios/{id}`, `POST /api/usuarios`, `PUT /api/usuarios/{id}`, `DELETE /api/usuarios/{id}` |
| donaciones | 8083 | `POST /api/donaciones`, `GET /api/donaciones`, `DELETE /api/donaciones/{id}` |
| noticias | 8084 | `GET /api/noticias`, `GET /api/noticias/{id}`, `POST /api/noticias`, `PUT /api/noticias/{id}`, `DELETE /api/noticias/{id}` |
| ventas | 8085 | `GET /api/productos`, `GET /api/productos/{id}`, `POST /api/productos`, `PUT /api/productos/{id}`, `DELETE /api/productos/{id}`, `POST /api/ventas`, `GET /api/ventas` |

Ubicación Backend: https://github.com/manu-valenzuela/corpaliv_grupo8_backend

### API externa

| API | Endpoint | Uso |
|---|---|---|
| mindicador.cl | `GET https://mindicador.cl/api` | Valor del dólar y la UF |

## Pasos para ejecutar

Requisitos: Android Studio, JDK 17 o superior, MySQL 8 y un emulador con Android 14 (API 34) o superior.

1. En MySQL, ejecutar `bd.sql` y luego `datos.sql` (repositorio de microservicios).
2. Levantar los cinco microservicios desde IntelliJ, o con `mvnw spring-boot:run` en cada carpeta: `auth/auth`, `usuarios/usuarios`, `donaciones/donaciones`, `noticias/noticias` y `ventas/ventas`.
3. Abrir este proyecto en Android Studio, sincronizar Gradle y ejecutar `app` en el emulador.
4. Para entrar como funcionario: `admin@corpaliv.cl` / `admin123`.

## Firma APK

Las llaves usadas en la firma están en app/corpaliv.jks.
Las evidencias del proceso de firma están en evidencias_firma.