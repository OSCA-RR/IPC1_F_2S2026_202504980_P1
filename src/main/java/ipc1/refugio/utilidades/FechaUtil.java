package ipc1.refugio.utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

// Clase de utilidad para manejar fechas.
// Todos los metodos son estaticos, no hace falta instanciarla.
public class FechaUtil {

    // Formato por defecto: 12/09/2026 20:30
    private static final String FORMATO = "dd/MM/yyyy HH:mm";

    // Devuelve la fecha y hora actual en formato dd/MM/yyyy HH:mm
    // Se usa para bitacora, solicitudes y rescates.
    public static String ahora() {
        return new SimpleDateFormat(FORMATO).format(new Date());
    }

    // Igual que ahora() pero con segundos. Se usa en los reportes HTML.
    public static String ahoraConSegundos() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    // Fecha para nombre de archivo: 20260912_203045
    // Se usa para nombrar los reportes HTML sin sobrescribir.
    public static String paraNombreArchivo() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}