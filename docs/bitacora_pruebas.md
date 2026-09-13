# Bitácora de Pruebas

**Estudiante:** Oscar Regino Sequen Tezén
**Carné:** 202504980
**Sección:** F
**Proyecto:** Centro de Rescate Animal - IPC1 2S2026
**Fecha de elaboración:** 12/09/2026

---

## 1. Introducción

Esta bitácora documenta las pruebas realizadas sobre el sistema Centro de Rescate Animal, los problemas encontrados durante el desarrollo, las soluciones aplicadas y las decisiones de diseño tomadas. El objetivo es dejar evidencia del proceso de razonamiento detrás del código, no solo del resultado final.

---

## 2. Escenarios de prueba

### Escenario 1: Refugio vacío

**Fecha:** 12/09/2026
**Preparación:** Se eliminó la carpeta `datos/` completa con `rm -rf datos/`.
**Objetivo:** Verificar que el sistema arranca sin datos y crea los archivos necesarios.

**Pruebas realizadas:**

| # | Acción | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| 1 | Iniciar sesión con admin/admin | Entra correctamente |  Correcto |
| 2 | Abrir módulo Animales | Tabla vacía |  Correcto |
| 3 | Abrir módulo Adoptantes | Tabla vacía |  Correcto |
| 4 | Abrir módulo Solicitudes | Combo vacío con aviso |  Correcto |
| 5 | Abrir módulo Rescates | Tabla vacía |  Correcto |
| 6 | Abrir Panel de Ubicaciones | Matriz con 50 celdas LIBRE |  Correcto |
| 7 | Generar los 4 reportes | Sin errores |  Correcto |
| 8 | Cerrar sesión | Guarda archivos vacíos |  Correcto |

**Incidencias:** Ninguna.

---

### Escenario 2: Refugio parcialmente ocupado

**Fecha:** 12/09/2026
**Preparación:** 3 animales (A001, A002, A003), 2 adoptantes (AD001, AD002), 2 espacios asignados (Área 0/Jaula 0 y Área 1/Jaula 3).
**Objetivo:** Verificar que los datos reales se manejan correctamente en todos los módulos.

**Pruebas realizadas:**

| # | Acción | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| 1 | Ver disponibilidad en Ubicaciones | 2 ocupados, 48 libres, 4% |  Correcto |
| 2 | Generar reporte de animales | 3 activos, 2 con ubicación |  Correcto |
| 3 | Generar reporte de ocupación | Matriz con celdas coloreadas |  Correcto |
| 4 | Eliminar A001 lógicamente | Espacio Área 0/Jaula 0 se libera |  Correcto |
| 5 | Ver disponibilidad otra vez | 1 ocupado, 49 libres |  Correcto |
| 6 | Intentar reasignar A002 | Rechaza por ya estar asignado |  Correcto |
| 7 | Reactivar el código A001 | Se reutiliza la posición del arreglo |  Correcto |

**Incidencias:** Ninguna.

---

### Escenario 3: Solicitudes acumuladas

**Fecha:** 12/09/2026
**Preparación:** 3 animales disponibles, 2 adoptantes activos, 3 solicitudes creadas.
**Objetivo:** Verificar el cambio de estado de solicitudes y su impacto en los animales.

**Pruebas realizadas:**

| # | Acción | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| 1 | Ver solicitudes registradas | Tabla con las 3 pendientes |  Correcto |
| 2 | Filtrar por Pendientes | Solo las pendientes |  Correcto |
| 3 | Aprobar S001 | El animal pasa a Adoptado |  Correcto |
| 4 | Rechazar S002 | El animal vuelve a Disponible |  Correcto |
| 5 | Consultar historial de un animal | Solo sus solicitudes |  Correcto |
| 6 | Registrar nueva solicitud a un animal adoptado | No aparece en el combo |  Correcto |

**Incidencias encontradas:** Ver sección 3 (errores detectados).

---

## 3. Errores detectados y corregidos

| # | Fecha | Descripción | Solución aplicada |
|---|---|---|---|
| 1 | 11/09/2026 | Al reactivar un animal eliminado, se duplicaba el registro en el archivo `animales.csv` | Se modificó `agregarAnimal` para reutilizar la posición del arreglo en lugar de crear una nueva |
| 2 | 11/09/2026 | La carpeta `datos/` no se creaba en una ubicación predecible | Se usó `System.getProperty("user.dir")` para obtener una ruta absoluta |
| 3 | 11/09/2026 | Los archivos `target/` y `datos/` se subían a Git | Se agregó `.gitignore` con las exclusiones correspondientes |
| 4 | 12/09/2026 | Se podían registrar múltiples solicitudes activas para el mismo animal, porque el combo de animales incluía también los que estaban en estado "En proceso" | Se cambió la condición del combo en `VentanaSolicitudes.cargarCombos()` para que solo aparezcan animales en estado "Disponible" |

---

## 4. Decisiones de diseño

Estas son las decisiones tomadas durante el desarrollo, con su justificación:

| # | Decisión | Justificación |
|---|---|---|
| 1 | Usar el carácter `\|` como separador en los archivos | Las direcciones y descripciones pueden contener comas, así que usar `,` rompería el formato |
| 2 | Eliminar lógicamente en lugar de borrar físicamente | Permite mantener el histórico de registros sin perder información |
| 3 | Reutilizar la posición del arreglo al reactivar un registro eliminado | Evita que el contador de animales crezca innecesariamente y previene duplicados en el archivo |
| 4 | Usar `AbstractTableModel` en lugar de `DefaultTableModel` | `DefaultTableModel` usa `Vector` internamente, lo cual está prohibido. Además, tener un modelo propio permite trabajar directamente con arreglos |
| 5 | Pasar la instancia única de `SistemaRefugio` a todas las ventanas por constructor | Garantiza que todas las ventanas compartan el mismo estado; si cada ventana creara su propio sistema, los datos quedarían desincronizados |
| 6 | Al crear una solicitud, cambiar automáticamente el animal a "En proceso" | Evita que se registren múltiples solicitudes activas para el mismo animal |
| 7 | Al aprobar una solicitud, cambiar automáticamente el animal a "Adoptado" | Refleja el estado real del refugio: un animal adoptado no debe estar disponible |
| 8 | Usar comillas en los textos de los diagramas Mermaid | Los caracteres especiales (como `¿`, `?`, acentos) rompen el renderizado en GitHub |
| 9 | Guardar los reportes HTML con fecha y hora en el nombre | Permite generar múltiples reportes sin sobrescribir los anteriores |
| 10 | Cargar los espacios al final en `GestorArchivos.cargarTodo` | Los espacios necesitan que los animales ya estén cargados para asignar correctamente |

---

## 5. Verificación de restricciones del proyecto

| Restricción | Estado |
|---|---|
| Java 17 o superior |  Cumplido |
| Interfaz Swing programada manualmente |  Cumplido |
| Sin uso de `ArrayList`, `LinkedList`, `HashMap`, `List`, `Queue`, `Stack`, `Vector` |  Cumplido |
| Uso de arreglos estáticos y matrices |  Cumplido |
| Persistencia en archivos `.txt` y `.csv` |  Cumplido |
| Reportes HTML con CSS embebido |  Cumplido |
| Control de versiones con Git y commits semánticos |  Cumplido |

---

## 6. Conclusión

Los 3 escenarios de prueba se ejecutaron satisfactoriamente después de las correcciones aplicadas. El sistema maneja correctamente:

- Arranque sin datos.
- Registro y consulta de animales, adoptantes, solicitudes, rescates y espacios.
- Persistencia en archivos.
- Generación de reportes HTML.
- Validaciones de campos vacíos, duplicados, valores inválidos y espacios ocupados.

Las decisiones de diseño tomadas (documentadas en la sección 4) reflejan el razonamiento detrás de la implementación y garantizan el cumplimiento de las restricciones del proyecto.