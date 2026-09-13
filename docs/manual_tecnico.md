# Manual Técnico

**Proyecto:** Centro de Rescate Animal - Gestión de Refugio y Adopciones
**Estudiante:** Oscar Regino Sequen Tezén
**Carné:** 202504980
**Sección:** F
**Curso:** Introducción a la Programación y Computación 1
**Ciclo:** Segundo Semestre 2026
**Facultad de Ingeniería, USAC**

---

## 1. Arquitectura general

El proyecto sigue una arquitectura en capas que separa responsabilidades. Cada capa tiene una función específica y se comunica con las demás mediante llamadas a métodos.

| Capa | Responsabilidad |
|---|---|
| Modelo | Clases del dominio (datos puros: Animal, Adoptante, etc.) |
| Servicios | Lógica del sistema con arreglos estáticos y matriz |
| Persistencia | Lectura y escritura de archivos |
| Reportes | Generación de HTML |
| Utilidades | Helpers (fechas, límite de texto) |
| Vista | Interfaz gráfica Swing + Main |

Toda la lógica de negocio vive en `SistemaRefugio`. Las ventanas Swing nunca implementan lógica por su cuenta, solo delegan y muestran resultados.

---

## 2. Clases del modelo

### 2.1 Usuario

Representa a un usuario del sistema, ya sea administrador o auxiliar.

**Atributos:**

- `nombreUsuario` (String): nombre con el que inicia sesión.
- `contrasena` (String): contraseña del usuario.
- `rol` (String): ADMIN o AUXILIAR.
- `activo` (boolean): indica si el usuario está habilitado.

### 2.2 Animal

Representa a un animal rescatado.

**Atributos:**

- `codigo` (String): identificador único (por ejemplo, A001).
- `nombre` (String): nombre del animal.
- `especie` (String): Perro o Gato.
- `raza` (String): raza del animal.
- `sexo` (String): Macho o Hembra.
- `edadEstimada` (int): edad en meses.
- `estadoClinico` (String): Sano, En tratamiento, Crítico.
- `estadoAdopcion` (String): Disponible, En proceso, Adoptado, No apto.
- `eliminadoLogico` (boolean): marca de eliminación lógica.
- `area` (int): fila en la matriz, -1 si no asignado.
- `jaula` (int): columna en la matriz, -1 si no asignado.

### 2.3 Adoptante

Representa a una persona que puede adoptar.

**Atributos:**

- `codigo` (String): identificador único.
- `nombre` (String): nombre completo.
- `dpi` (String): documento personal de identificación.
- `telefono` (String): número de contacto.
- `direccion` (String): dirección de residencia.
- `correo` (String): correo electrónico.
- `activo` (boolean): marca de activación.

### 2.4 Solicitud

Solicitud de adopción que vincula un animal con un adoptante.

**Atributos:**

- `codigo` (String): identificador de la solicitud.
- `codigoAnimal` (String): código del animal solicitado.
- `codigoAdoptante` (String): código del adoptante.
- `fecha` (String): fecha y hora de la solicitud.
- `estado` (String): Pendiente, Aprobada, Rechazada, Cancelada.
- `observaciones` (String): comentarios opcionales.

### 2.5 Rescate

Caso de rescate urgente reportado.

**Atributos:**

- `codigo` (String): identificador del rescate.
- `ubicacion` (String): lugar donde se reporta.
- `descripcion` (String): descripción del caso.
- `prioridad` (String): Alta, Media, Baja.
- `estado` (String): Activo o Atendido.
- `fecha` (String): fecha y hora de reporte.

### 2.6 EspacioRefugio

Cada celda de la matriz de espacios del refugio.

**Atributos:**

- `area` (int): fila de la matriz.
- `jaula` (int): columna de la matriz.
- `capacidadMaxima` (int): capacidad de la jaula.
- `codigoAnimal` (String): código del animal asignado o null si está libre.

### 2.7 Bitacora

Registro de acciones del sistema.

**Atributos:**

- `fechaHora` (String): momento de la acción.
- `usuario` (String): quién la realizó.
- `accion` (String): tipo de acción.
- `detalle` (String): descripción.

---

## 3. Arreglos estáticos y matriz

Todas las estructuras del sistema son arreglos de tamaño fijo. No se utilizan colecciones dinámicas.

| Estructura | Tipo | Tamaño |
|---|---|---|
| Usuarios | Usuario[] | 10 |
| Animales | Animal[] | 100 |
| Adoptantes | Adoptante[] | 50 |
| Solicitudes | Solicitud[] | 100 |
| Rescates | Rescate[] | 50 |
| Bitácora | Bitacora[] | 500 |
| Espacios | EspacioRefugio[][] | 5 x 10 = 50 |

### 3.1 Contadores

Cada arreglo tiene un contador asociado (`contadorAnimales`, `contadorAdoptantes`, etc.) que indica cuántas posiciones están ocupadas. Los datos siempre se insertan al inicio, sin dejar huecos.

### 3.2 Matriz de espacios

La matriz representa los espacios del refugio:

- **Filas:** áreas del refugio.
- **Columnas:** jaulas dentro de cada área.
- **Cada celda:** guarda el código del animal asignado o null si está libre.

Ejemplo visual:

| | Jaula 0 | Jaula 1 | Jaula 2 | ... | Jaula 9 |
|---|---|---|---|---|---|
| Área 0 | A001 | LIBRE | LIBRE | ... | LIBRE |
| Área 1 | LIBRE | LIBRE | A002 | ... | LIBRE |
| Área 2 | LIBRE | LIBRE | LIBRE | ... | LIBRE |
| Área 3 | LIBRE | LIBRE | LIBRE | ... | LIBRE |
| Área 4 | LIBRE | LIBRE | LIBRE | ... | LIBRE |

---

## 4. Métodos principales de SistemaRefugio

### 4.1 Usuarios

- `validarLogin(usuario, contrasena)`: devuelve el Usuario si las credenciales son correctas.
- `agregarUsuario(Usuario)`: agrega un usuario al arreglo.
- `cerrarSesion()`: limpia el usuario actual.

### 4.2 Animales

- `agregarAnimal(Animal)`: valida duplicados y reactiva registros eliminados.
- `buscarAnimalPorCodigo(String)`: búsqueda exacta.
- `buscarAnimalesPorNombre(String)`: búsqueda parcial.
- `buscarAnimalesPorEspecie(String)`: filtra por especie.
- `buscarAnimalesPorEstado(String)`: filtra por estado de adopción.
- `editarEstadoAnimal(...)`: cambia los estados clínico y de adopción.
- `eliminarAnimalLogico(String)`: marca como eliminado y libera su espacio.

### 4.3 Adoptantes

- `agregarAdoptante(Adoptante)`: valida código y DPI únicos.
- `buscarAdoptantePorCodigo(String)` y `buscarAdoptantePorDpi(String)`.
- `editarAdoptante(...)`.

### 4.4 Solicitudes

- `agregarSolicitud(Solicitud)`: valida código único.
- `cambiarEstadoSolicitud(codigo, nuevoEstado)`: al aprobar, cambia el animal a Adoptado.
- `getSolicitudesPendientes()` y `getSolicitudesPorAnimal(String)`.

### 4.5 Rescates

- `agregarRescate(Rescate)` y `atenderRescate(String)`.
- `getRescatesActivos()` y `getRescatesPorPrioridad(String)`.

### 4.6 Espacios

- `asignarEspacio(area, jaula, codigoAnimal)`: valida celda libre y animal existente.
- `liberarEspacio(area, jaula)`: marca la celda como libre.
- `contarEspaciosLibres()`, `contarEspaciosOcupados()` y `porcentajeOcupacion()`.

### 4.7 Bitácora

- `registrarBitacora(accion, detalle)`: usa el usuario activo como autor.

---

## 5. Persistencia

La clase `GestorArchivos` guarda y carga la información en la carpeta `datos/`.

| Archivo | Formato | Contenido |
|---|---|---|
| usuarios.txt | usuario\|contrasena\|rol\|activo | Usuarios |
| animales.csv | 11 campos separados por \| | Animales |
| adoptantes.csv | 7 campos | Adoptantes |
| solicitudes.csv | 6 campos | Solicitudes |
| rescates.csv | 6 campos | Rescates |
| bitacora.txt | fechaHora\|usuario\|accion\|detalle | Bitácora |
| espacios.csv | area\|jaula\|codigoAnimal | Matriz |

El separador es la barra vertical `|`. Se usa `try-with-resources` para garantizar el cierre de archivos.

**Orden de carga (importante):**

1. Usuarios.
2. Animales.
3. Adoptantes.
4. Solicitudes.
5. Rescates.
6. Bitácora (no re-registra).
7. Espacios (al final, porque necesita animales cargados).

---

## 6. Reportes HTML

La clase `GeneradorReportes` crea 4 archivos HTML con CSS embebido:

- `reporte_animales_YYYYMMDD_HHMMSS.html`
- `reporte_adopciones_YYYYMMDD_HHMMSS.html`
- `reporte_ocupacion_YYYYMMDD_HHMMSS.html`
- `bitacora_YYYYMMDD_HHMMSS.html`

Se guardan en `datos/reportes/`. El nombre incluye fecha y hora para no sobrescribir reportes anteriores.

---

## 7. Interfaz gráfica

Todas las ventanas están programadas manualmente en Swing. Ninguna usa generadores visuales.

| Ventana | Propósito |
|---|---|
| VentanaLogin | Autenticación |
| VentanaPrincipal | Menú principal |
| VentanaAnimales | CRUD de animales |
| VentanaAdoptantes | CRUD de adoptantes |
| VentanaSolicitudes | Solicitudes de adopción |
| VentanaRescates | Rescates urgentes |
| VentanaUbicaciones | Matriz de espacios |
| VentanaReportes | Generación de reportes |
| VentanaDatosEstudiante | Información del proyecto |

Cada ventana recibe la instancia única de `SistemaRefugio` por constructor. No se crean instancias nuevas.

---

## 8. Validaciones

El sistema valida en todos los módulos:

- Campos vacíos.
- Códigos duplicados.
- DPI duplicado en adoptantes.
- Edad numérica y positiva.
- Correo con formato básico.
- DPI con formato de 13 dígitos.
- Estados válidos.
- Espacios ocupados.
- Longitud máxima de campos con `TextoLimitado`.

---

## 9. Decisiones de diseño

- **SistemaRefugio como instancia única:** centraliza el estado y evita duplicidad.
- **Eliminación lógica:** los registros no se borran del arreglo, se marcan como eliminados.
- **Reutilización de posición al reactivar:** al registrar un código eliminado, se reutiliza la posición del arreglo.
- **Modelos de tabla propios:** las clases `AnimalTableModel`, `AdoptanteTableModel`, etc., heredan de `AbstractTableModel` para no depender de `DefaultTableModel`.
- **Bitácora con usuario activo:** el autor se obtiene del usuario logueado.
- **Sin Collections:** todas las estructuras son arreglos y matrices.

---

## 10. Conclusión

El proyecto demuestra el uso de arreglos estáticos, matrices, ciclos, condicionales, métodos, validaciones, archivos y reportes HTML, integrados en una aplicación de escritorio con interfaz gráfica Java Swing programada completamente por código.
