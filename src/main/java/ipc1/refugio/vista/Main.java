package ipc1.refugio.vista;

import ipc1.refugio.modelo.Usuario;
import ipc1.refugio.persistencia.GestorArchivos;
import ipc1.refugio.servicios.SistemaRefugio;

/**
 * Punto de entrada de la aplicacion Centro de Rescate Animal.
 *
 * Flujo:
 *  1. Crea la instancia unica de SistemaRefugio.
 *  2. Carga los datos persistidos en archivos.
 *  3. Si no existen usuarios, crea admin y auxiliar por defecto.
 *  4. Abre la ventana de login.
 */
public class Main {

    public static void main(String[] args) {

        // 1. Creamos el "cerebro" del sistema. Es un unico objeto que
        //    se compartira con todas las ventanas a traves de sus constructores.
        SistemaRefugio sistema = new SistemaRefugio();

        // 2. Cargamos la informacion guardada en archivos (animales, adoptantes, etc.).
        GestorArchivos.cargarTodo(sistema);

        // 3. Si despues de cargar no hay ningun usuario registrado,
        if (sistema.getContadorUsuarios() == 0) {
            sistema.agregarUsuario(new Usuario("admin", "admin", "ADMIN", true));
            sistema.agregarUsuario(new Usuario("auxiliar", "auxiliar", "AUXILIAR", true));

            // Guardamos inmediatamente para que en el proximo arranque ya existan.
            GestorArchivos.guardarTodo(sistema);
        }

        // 4. Abrimos la ventana de login. Swing se encarga del resto.
        new VentanaLogin(sistema).setVisible(true);
    }
}
