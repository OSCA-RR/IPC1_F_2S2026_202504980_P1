# Centro de Rescate Animal

Aplicación de escritorio para la gestión de un refugio de animales, desarrollada en **Java 17** con interfaz gráfica **Swing** programada manualmente.

---

**Proyecto:** Centro de Rescate Animal - Gestión de Refugio y Adopciones
**Curso:** Introducción a la Programación y Computación 1
**Ciclo:** Segundo Semestre 2026
**Facultad de Ingeniería, USAC**

---

## Autor

| Campo | Valor |
|---|---|
| Nombre | Oscar Regino Sequen Tezén |
| Carné | 202504980 |
| Sección | F |

## Descripción

El sistema administra el ciclo básico de un refugio: registro de animales rescatados, control clínico y de adopción, gestión de adoptantes, solicitudes, rescates urgentes, distribución física en jaulas y generación de reportes. Toda la lógica está implementada con arreglos estáticos y matrices, y la persistencia se realiza en archivos de texto plano.

## Ejecución

```bash
git clone https://github.com/OSCA-RR/IPC1_F_2S2026_202504980_P1.git
```

Abrir el proyecto en NetBeans y ejecutar la clase principal:

```
ipc1.refugio.vista.Main
```

## Credenciales iniciales

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin | Administrador |
| auxiliar | auxiliar | Auxiliar |

## Funcionalidades

- Autenticación local con dos roles.
- Registro, búsqueda, edición y eliminación lógica de animales.
- Gestión de adoptantes con validación de DPI y correo.
- Solicitudes de adopción con cambio de estado automático.
- Rescates urgentes con prioridad Alta, Media y Baja.
- Matriz de ubicaciones (5 áreas × 10 jaulas).
- Reportes HTML con CSS embebido.
- Persistencia en archivos `.txt` y `.csv`.

## Restricciones respetadas

- Java 17 o superior.
- Swing programado únicamente por código.
- Uso exclusivo de arreglos estáticos y matrices (sin `ArrayList`, `HashMap`, `List`, `Queue`, `Stack`, `Vector` ni similares).
- Reportes HTML generados desde Java.
- Commits semánticos en Git.

## Documentación

| Documento | Enlace |
|---|---|
| Manual Técnico | [docs/manual_tecnico.md](docs/manual_tecnico.md) |
| Manual de Usuario | [docs/manual_usuario.pdf](docs/manual_usuario.pdf) |
| Bitácora de Pruebas | [docs/bitacora_pruebas.md](docs/bitacora_pruebas.md) |
| Diagrama de Flujo | [docs/diagrama_flujo.md](docs/diagrama_flujo.md) |
| Diagrama de Módulos | [docs/diagrama_modulos.md](docs/diagrama_modulos.md) |
| Diagrama de Matriz | [docs/diagrama_matriz.md](docs/diagrama_matriz.md) |
| Diagrama de Clases | [docs/diagrama_clases.md](docs/diagrama_clases.md) |

## Estructura

```
src/main/java/ipc1/refugio/
├── modelo/         Clases del dominio
├── servicios/      SistemaRefugio
├── persistencia/   GestorArchivos
├── reportes/       GeneradorReportes
├── utilidades/     FechaUtil, TextoLimitado
└── vista/          Ventanas Swing + Main
```

---
