package ipc1.refugio.utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaUtil {

    private static final String FORMATO = "dd/MM/yyyy HH:mm";

    public static String ahora() {
        return new SimpleDateFormat(FORMATO).format(new Date());
    }

    public static String ahoraConSegundos() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public static String paraNombreArchivo() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
}
