# **Especificación de Requisitos de Software (ERS)**
**Estándar IEEE 830**
**Proyecto:** Plataforma Móvil Integrada de Difusión de Impacto y Vinculación
**Organización:** Corporación de Padres y Amigos por el Limitado Visual (CORPALIV / Alba Lab)
**Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles

---

## **1. Introducción**

### **1.1 Propósito**
El propósito de este documento es especificar los requisitos funcionales y no funcionales para el desarrollo del Producto Mínimo Viable (MVP) de la aplicación móvil en Android Studio. Este sistema unificará la difusión del impacto social de CORPALIV y la Red Sociolaboral Alba Lab con la captación de colaboradores, voluntarios y compradores en un único punto de acceso.

### **1.2 Alcance del Sistema**
La aplicación móvil actuará como canal centralizado e intuitivo para exponer de forma efectiva la labor de la fundación, visualizar catálogos de productos y servicios de inclusión, y procesar intenciones de colaboración (donaciones, voluntariado, compra de artículos y alianzas institucionales).

Se ha identificado que la plataforma web de la fundación no es capaz de mostrar adecuadamente la variedad ni la extensión de sus actividades, con una tienda de difícil acceso, un portal de la escuela Van Dijk que queda a medio camino entre ser un folleto y un portal institucional del colegio sin cumplir con ninguna. En cuanto a los potenciales colaboradores, la sección para colaborar o hacerse socio está destinada enteramente al aporte económico, teniendo la persona natural incluso 3 formularios distintos donde puede donar cuando no sólo debería ser uno, sino que toda la sección de registro debería ser capaz de manejar las donaciones, el voluntariado, contacto con empresas (porque incluso una empresa interesada hace su proceso mediante una persona) y hasta las compras no anónimas en la tienda, dependiendo de las acciones que el usuario tenga a disposición.

---

## **2. Descripción General**

### **2.1 Características de los Usuarios (Sección 2.3 IEEE 830)**

| Perfil de Usuario | Descripción y Contexto | Nivel Técnico | Frecuencia de Uso | Necesidad Principal |
| :--- | :--- | :--- | :--- | :--- |
| **Comprador de la Tienda** | Personas naturales interesadas en adquirir artículos confeccionados en los talleres de la fundación. | Básico / Intermedio | Ocasional | Explorar el catálogo de productos solidarios y generar cotizaciones o pedidos de forma directa. |
| **Socio Aportante** | Personas naturales que desean apoyar financieramente a la institución de manera recurrente o puntual. | Básico | Mensual / Ocasional | Conocer el destino transparente de los aportes y completar su registro de donante sin duplicar credenciales. |
| **Voluntario** | Personas que ofrecen su tiempo, conocimientos o apoyo operativo a las actividades de la fundación. | Básico / Intermedio | Ocasional | Revisar áreas de apoyo disponibles y postular mediante su información de contacto y disponibilidad. |
| **Empresa o Institución Colaboradora** | Representantes de organizaciones que buscan contratar servicios de inclusión laboral, capacitaciones o alianzas corporativas. | Intermedio | Ocasional | Consultar la oferta de servicios institucionales y solicitar cotizaciones o reuniones formales en un canal directo. |
| **Equipo de Vinculación y Ventas** | Personal interno administrativo de CORPALIV y Alba Lab encargados de gestionar las solicitudes recibidas. | Intermedio | Diario | Recibir, clasificar y dar seguimiento centralizado a las intenciones de colaboración capturadas. |

---

## **3. Requisitos Funcionales (RF)**

### **3.1 Módulo Público e Institucional**
* **RF-01: Exposición Institucional**
  * **Descripción:** El sistema debe desplegar la misión, los propósitos y los programas educativos/laborales de CORPALIV y la Red Sociolaboral Alba Lab.
  * **Entradas:** Selección del menú institucional por el usuario.
  * **Proceso:** Obtención de datos institucionales e historias de impacto desde la base de datos a través de la API REST.
  * **Salida:** Vista estructurada con información, indicadores de impacto e historias de éxito anonimizadas.

### **3.2 Módulo de Comercialización y Servicios**
* **RF-02: Navegación de Catálogo Solidario**
  * **Descripción:** El sistema debe presentar el catálogo unificado de productos (línea de bienestar, accesorios) y la oferta de servicios para empresas (capacitaciones, evaluación de puestos de trabajo).
  * **Entradas:** Selección de categoría de productos o servicios.
  * **Proceso:** Filtrado y renderizado de la lista de elementos en la interfaz gráfica.
  * **Salida:** Ficha detallada del producto o servicio con descripción, imágenes y valor estimado.
* **RF-03: Solicitud de Cotización / Pedido**
  * **Descripción:** El sistema debe permitir al **Comprador** o a la **Empresa Colaboradora** seleccionar artículos o servicios y enviar un formulario de cotización simplificado.
  * **Entradas:** Lista de artículos/servicios seleccionados, nombre de contacto, correo electrónico sintético y teléfono ficticio de prueba.
  * **Proceso:** Validar que los campos requeridos no estén vacíos y estructurar una petición HTTP POST hacia el servidor.
  * **Salida:** Mensaje de confirmación en pantalla con el número de folio generado.

### **3.3 Módulo de Captación de Colaboradores**
* **RF-04: Registro de Socio Aportante**
  * **Descripción:** El sistema debe permitir al **Socio Aportante** seleccionar una modalidad de donación y registrar sus datos para ser contactado.
  * **Entradas:** Monto seleccionado, frecuencia (única/mensual) y datos de contacto de prueba.
  * **Proceso:** Envío de la solicitud de inscripción al backend mediante la API REST.
  * **Salida:** Confirmación visual del envío de la solicitud de aportante.
* **RF-05: Postulación de Voluntariado**
  * **Descripción:** El sistema debe disponer de un formulario para que el **Voluntario** ingrese su perfil, disponibilidad de tiempo y área de apoyo de su interés.
  * **Entradas:** Área de interés seleccionada, días/horarios disponibles y datos de contacto ficticios.
  * **Proceso:** Transmisión del formulario completado hacia el servicio web.
  * **Salida:** Notificación de postulación recibida exitosamente.

### **3.4 Módulo de Administración Backend**
* **RF-06: Clasificación de Solicitudes Entrantes**
  * **Descripción:** El backend debe recibir los datos de la app y categorizar automáticamente la entrada según el perfil del emisor.
  * **Entradas:** Payload JSON recibido a través del endpoint de la API REST.
  * **Proceso:** Identificación del tipo de formulario (donación, compra, voluntariado o reunión de empresas) y almacenamiento en la base de datos en su tabla correspondiente.
  * **Salida:** Registro categorizado en el sistema interno disponible para el **Equipo de Vinculación y Ventas**.

---

## **4. Requisitos No Funcionales (RNF)**

* **RNF-01: Accesibilidad e Interfaz Inclusiva**
  * La interfaz de la aplicación Android debe construirse bajo el sistema de diseño **Material Design 3**, garantizando contraste de colores adecuado, tamaños de texto escalables y compatibilidad con lectores de pantalla (TalkBack) para personas con discapacidad visual.
* **RNF-02: Comunicación y Arquitectura de Red**
  * La aplicación debe conectarse obligatoriamente con una arquitectura backend (API REST / Microservicios) a través del protocolo HTTPS para la transferencia segura de datos estructurados en formato JSON.
* **RNF-03: Protección y Anonimización de Datos**
  * El sistema no debe transmitir información personal real o imágenes no autorizadas de beneficiarios.
* **RNF-04: Rendimiento y Adaptabilidad**
  * Las vistas de la interfaz deben ser adaptables a múltiples resoluciones y densidades de pantalla de dispositivos móviles Android.

---

## **5. Vistas de Arquitectura y Diseño Frontend (UML)**

### **5.1 Vista de Casos de Uso (Diagrama de Casos de Uso)**

```text
  [ Comprador ] --------> ( UC-01: Explorar el contenido de la fundación )
        |
        +---------------> ( UC-02: Explorar Catálogo Unificado )
        |
        +---------------> ( UC-03: Solicitar Cotización de Productos )


  [ Socio Aportante ] --> ( UC-01: Explorar el contenido de la fundación )
        |
        +---------------> ( UC-04: Registrar Aporte Monetario )


  [ Voluntario ] -------> ( UC-01: Explorar el contenido de la fundación )
        |
        +---------------> ( UC-05: Postular a Voluntariado )


  [ Empresa ] ----------> ( UC-01: Explorar el contenido de la fundación )
        |
        +---------------> ( UC-06: Solicitar Servicios RSE / Alianzas )


  [ Todos los Usuarios ] -> ( UC-07: Ajustar Accesibilidad Visual/Auditiva )

```

### **5.1 Vista de Casos de Uso (Diagrama de Casos de Uso)**

```
+-------------------------------------------------------------------------+
|                              app.frontend                               |
+-------------------------------------------------------------------------+
       |
       +---> [ ui.navigation ]
       |        └─ MainHostActivity / BottomNavRouter
       |
       +---> [ ui.contenido ]
       |        ├─ InicioFragment
       |        ├─ HistoriasAdapter
       |        └─ ContenidoFundacionView
       |
       +---> [ ui.tienda ]
       |        ├─ CatalogoFragment
       |        ├─ ProductoDetalleBottomSheet
       |        └─ CotizacionCartView
       |
       +---> [ ui.colaborar ]
       |        ├─ ColaborarHubFragment
       |        ├─ FormDonacionSubView
       |        ├─ FormVoluntarioSubView
       |        └─ FormEmpresaSubView
       |
       +---> [ ui.perfil ]
       |        ├─ PerfilUsuarioFragment
       |        ├─ HistorialGestionesView
       |        └─ AccessibilitySettingsView
       |
       +---> [ ui.common ]
                ├─ AccessibleButton
                ├─ AccessibleTextView
                └─ LoadingStateView
```
