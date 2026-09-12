package ipc1.refugio.reportes;

import ipc1.refugio.modelo.*;
import ipc1.refugio.servicios.SistemaRefugio;
import ipc1.refugio.utilidades.FechaUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Generador de reportes HTML del sistema.
 *
 * Cada metodo publico crea un archivo HTML en la carpeta datos/reportes/
 * con un nombre que incluye fecha y hora para no sobrescribir reportes
 * anteriores. El HTML incluye un CSS basico embebido en la etiqueta <style>.
 */
public class GeneradorReportes {

    // Carpeta donde se guardan los reportes.
    private static final String CARPETA_REPORTES =
            System.getProperty("user.dir") + "/datos/reportes";

    // ============================================================
    // UTILIDADES INTERNAS
    // ============================================================

    /**
     * Crea la carpeta de reportes si no existe.
     */
    private static void asegurarCarpeta() {
        File carpeta = new File(CARPETA_REPORTES);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    /**
     * Devuelve la ruta completa del archivo de reporte con fecha y hora.
     * Ejemplo: datos/reportes/reporte_animales_20260912_153045.html
     */
    private static String construirRuta(String prefijo) {
        return CARPETA_REPORTES + "/" + prefijo + "_"
                + FechaUtil.paraNombreArchivo() + ".html";
    }

    /**
     * Escribe el encabezado HTML comun a todos los reportes, incluyendo
     * el CSS basico embebido.
     */
    private static void escribirEncabezado(PrintWriter pw, String titulo) {
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"es\">");
        pw.println("<head>");
        pw.println("<meta charset=\"UTF-8\">");
        pw.println("<title>" + titulo + "</title>");
        pw.println("<style>");
        pw.println("  body { font-family: Arial, sans-serif; margin: 20px; color: #333; }");
        pw.println("  h1 { color: #2c3e50; border-bottom: 2px solid #2c3e50; padding-bottom: 8px; }");
        pw.println("  h2 { color: #34495e; margin-top: 30px; }");
        pw.println("  .info { background: #ecf0f1; padding: 10px; border-radius: 4px; }");
        pw.println("  table { border-collapse: collapse; width: 100%; margin-top: 15px; }");
        pw.println("  th { background: #2c3e50; color: white; padding: 8px; text-align: left; }");
        pw.println("  td { border: 1px solid #bdc3c7; padding: 6px; }");
        pw.println("  tr:nth-child(even) { background: #f9f9f9; }");
        pw.println("  .libre { color: #27ae60; font-weight: bold; }");
        pw.println("  .ocupado { color: #c0392b; font-weight: bold; }");
        pw.println("  .badge { display: inline-block; padding: 3px 8px; border-radius: 10px; color: white; font-size: 12px; }");
        pw.println("  .badge-alta { background: #c0392b; }");
        pw.println("  .badge-media { background: #e67e22; }");
        pw.println("  .badge-baja { background: #27ae60; }");
        pw.println("  .footer { margin-top: 40px; font-size: 12px; color: #7f8c8d; text-align: center; }");
        pw.println("</style>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<h1>" + titulo + "</h1>");
        pw.println("<div class=\"info\">");
        pw.println("<p><strong>Fecha de generacion:</strong> " + FechaUtil.ahoraConSegundos() + "</p>");
        pw.println("<p><strong>Generado por:</strong> "
                + "(sistema Centro de Rescate Animal)</p>");
        pw.println("</div>");
    }

    /**
     * Escribe el pie de pagina comun.
     */
    private static void escribirPie(PrintWriter pw) {
        pw.println("<div class=\"footer\">");
        pw.println("IPC1 - Centro de Rescate Animal - 2S2026 - Carnet 202504980");
        pw.println("</div>");
        pw.println("</body>");
        pw.println("</html>");
    }

    // ============================================================
    // REPORTE DE ANIMALES
    // ============================================================
    public static String generarReporteAnimales(SistemaRefugio sistema) {
        asegurarCarpeta();
        String ruta = construirRuta("reporte_animales");

        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            escribirEncabezado(pw, "Reporte de Animales");

            // Seccion: resumen general.
            Animal[] activos = sistema.getAnimalesActivos();
            int totalActivos = 0;
            for (int i = 0; i < activos.length; i++) {
                if (activos[i] != null) totalActivos++;
                else break;
            }

            pw.println("<h2>Resumen</h2>");
            pw.println("<p>Total de animales activos registrados: <strong>"
                    + totalActivos + "</strong></p>");

            // Seccion: tabla de animales.
            pw.println("<h2>Listado de animales activos</h2>");
            pw.println("<table>");
            pw.println("<tr>");
            pw.println("<th>Codigo</th><th>Nombre</th><th>Especie</th>"
                    + "<th>Raza</th><th>Sexo</th><th>Edad (meses)</th>"
                    + "<th>Estado Clinico</th><th>Estado Adopcion</th>"
                    + "<th>Ubicacion</th>");
            pw.println("</tr>");

            for (int i = 0; i < activos.length; i++) {
                Animal a = activos[i];
                if (a == null) break;
                String ubicacion = (a.getArea() >= 0 && a.getJaula() >= 0)
                        ? "Area " + a.getArea() + " - Jaula " + a.getJaula()
                        : "Sin asignar";
                pw.println("<tr>");
                pw.println("<td>" + a.getCodigo() + "</td>");
                pw.println("<td>" + a.getNombre() + "</td>");
                pw.println("<td>" + a.getEspecie() + "</td>");
                pw.println("<td>" + a.getRaza() + "</td>");
                pw.println("<td>" + a.getSexo() + "</td>");
                pw.println("<td>" + a.getEdadEstimada() + "</td>");
                pw.println("<td>" + a.getEstadoClinico() + "</td>");
                pw.println("<td>" + a.getEstadoAdopcion() + "</td>");
                pw.println("<td>" + ubicacion + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            escribirPie(pw);

        } catch (IOException e) {
            System.out.println("Error al generar reporte de animales: " + e.getMessage());
            return null;
        }

        return ruta;
    }

    // ============================================================
    // REPORTE DE ADOPCIONES
    // ============================================================
    public static String generarReporteAdopciones(SistemaRefugio sistema) {
        asegurarCarpeta();
        String ruta = construirRuta("reporte_adopciones");

        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            escribirEncabezado(pw, "Reporte de Adopciones");

            // Contamos solicitudes aprobadas.
            Solicitud[] todas = sistema.getTodasLasSolicitudes();
            int aprobadas = 0, pendientes = 0, rechazadas = 0, canceladas = 0;

            for (int i = 0; i < todas.length; i++) {
                Solicitud s = todas[i];
                if (s == null) break;
                if (s.getEstado().equalsIgnoreCase("Aprobada")) aprobadas++;
                else if (s.getEstado().equalsIgnoreCase("Pendiente")) pendientes++;
                else if (s.getEstado().equalsIgnoreCase("Rechazada")) rechazadas++;
                else if (s.getEstado().equalsIgnoreCase("Cancelada")) canceladas++;
            }

            pw.println("<h2>Resumen de solicitudes</h2>");
            pw.println("<table>");
            pw.println("<tr><th>Estado</th><th>Cantidad</th></tr>");
            pw.println("<tr><td>Aprobadas</td><td>" + aprobadas + "</td></tr>");
            pw.println("<tr><td>Pendientes</td><td>" + pendientes + "</td></tr>");
            pw.println("<tr><td>Rechazadas</td><td>" + rechazadas + "</td></tr>");
            pw.println("<tr><td>Canceladas</td><td>" + canceladas + "</td></tr>");
            pw.println("</table>");

            // Detalle de todas las solicitudes.
            pw.println("<h2>Detalle de solicitudes</h2>");
            pw.println("<table>");
            pw.println("<tr><th>Codigo</th><th>Fecha</th><th>Animal</th>"
                    + "<th>Adoptante</th><th>Estado</th>"
                    + "<th>Observaciones</th></tr>");

            for (int i = 0; i < todas.length; i++) {
                Solicitud s = todas[i];
                if (s == null) break;
                pw.println("<tr>");
                pw.println("<td>" + s.getCodigo() + "</td>");
                pw.println("<td>" + s.getFecha() + "</td>");
                pw.println("<td>" + s.getCodigoAnimal() + "</td>");
                pw.println("<td>" + s.getCodigoAdoptante() + "</td>");
                pw.println("<td>" + s.getEstado() + "</td>");
                pw.println("<td>" + s.getObservaciones() + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            escribirPie(pw);

        } catch (IOException e) {
            System.out.println("Error al generar reporte de adopciones: " + e.getMessage());
            return null;
        }

        return ruta;
    }

    // ============================================================
    // REPORTE DE OCUPACION
    // ============================================================
    public static String generarReporteOcupacion(SistemaRefugio sistema) {
        asegurarCarpeta();
        String ruta = construirRuta("reporte_ocupacion");

        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            escribirEncabezado(pw, "Reporte de Ocupacion del Refugio");

            // Estadisticas generales.
            int libres = sistema.contarEspaciosLibres();
            int ocupados = sistema.contarEspaciosOcupados();
            int total = SistemaRefugio.AREAS * SistemaRefugio.JAULAS;
            double porcentaje = sistema.porcentajeOcupacion();

            pw.println("<h2>Estadisticas</h2>");
            pw.println("<table>");
            pw.println("<tr><th>Metrica</th><th>Valor</th></tr>");
            pw.println("<tr><td>Total de espacios</td><td>" + total + "</td></tr>");
            pw.println("<tr><td>Espacios libres</td><td class=\"libre\">" + libres + "</td></tr>");
            pw.println("<tr><td>Espacios ocupados</td><td class=\"ocupado\">" + ocupados + "</td></tr>");
            pw.println("<tr><td>Porcentaje de ocupacion</td><td>"
                    + String.format("%.2f%%", porcentaje) + "</td></tr>");
            pw.println("</table>");

            // Tabla de la matriz completa.
            pw.println("<h2>Distribucion detallada (Area x Jaula)</h2>");
            pw.println("<table>");
            pw.println("<tr><th>Area</th>");
            for (int j = 0; j < SistemaRefugio.JAULAS; j++) {
                pw.println("<th>Jaula " + j + "</th>");
            }
            pw.println("</tr>");

            for (int i = 0; i < SistemaRefugio.AREAS; i++) {
                pw.println("<tr>");
                pw.println("<td><strong>Area " + i + "</strong></td>");
                for (int j = 0; j < SistemaRefugio.JAULAS; j++) {
                    EspacioRefugio e = sistema.getEspacio(i, j);
                    if (e.estaLibre()) {
                        pw.println("<td class=\"libre\">LIBRE</td>");
                    } else {
                        pw.println("<td class=\"ocupado\">" + e.getCodigoAnimal() + "</td>");
                    }
                }
                pw.println("</tr>");
            }

            pw.println("</table>");
            escribirPie(pw);

        } catch (IOException e) {
            System.out.println("Error al generar reporte de ocupacion: " + e.getMessage());
            return null;
        }

        return ruta;
    }

    // ============================================================
    // REPORTE DE BITACORA
    // ============================================================
    public static String generarReporteBitacora(SistemaRefugio sistema) {
        asegurarCarpeta();
        String ruta = construirRuta("bitacora");

        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {
            escribirEncabezado(pw, "Bitacora de Acciones");

            Bitacora[] bitacora = sistema.getBitacora();

            pw.println("<h2>Ultimas acciones registradas</h2>");
            pw.println("<table>");
            pw.println("<tr><th>Fecha y hora</th><th>Usuario</th>"
                    + "<th>Accion</th><th>Detalle</th></tr>");

            for (int i = 0; i < bitacora.length; i++) {
                Bitacora b = bitacora[i];
                if (b == null) break;
                pw.println("<tr>");
                pw.println("<td>" + b.getFechaHora() + "</td>");
                pw.println("<td>" + b.getUsuario() + "</td>");
                pw.println("<td>" + b.getAccion() + "</td>");
                pw.println("<td>" + b.getDetalle() + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            escribirPie(pw);

        } catch (IOException e) {
            System.out.println("Error al generar bitacora: " + e.getMessage());
            return null;
        }

        return ruta;
    }
}