package ipc1.refugio.vista;

import ipc1.refugio.modelo.Usuario;
import ipc1.refugio.persistencia.GestorArchivos;
import ipc1.refugio.servicios.SistemaRefugio;

// Punto de entrada de la aplicacion.
// Crea el sistema, carga los datos persistidos, crea usuarios por defecto
// si es la primera vez y abre la ventana de login.
public class Main {

    public static void main(String[] args) {

        // 1. Creamos el sistema. Esta instancia se comparte con todas
        //    las ventanas para que trabajen sobre los mismos arreglos.
        SistemaRefugio sistema = new SistemaRefugio();

        // 2. Cargamos los datos guardados en archivos. Si es la primera
        //    ejecucion, los archivos no existen y no se carga nada.
        GestorArchivos.cargarTodo(sistema);

        // 3. Si no hay usuarios registrados (primera ejecucion),
        //    creamos los dos usuarios por defecto.
        if (sistema.getContadorUsuarios() == 0) {
            sistema.agregarUsuario(new Usuario("admin", "admin", "ADMIN", true));
            sistema.agregarUsuario(new Usuario("auxiliar", "auxiliar", "AUXILIAR", true));

            // Guardamos de una vez para que en el proximo arranque existan.
            GestorArchivos.guardarTodo(sistema);
        }

        // 4. Abrimos la ventana de login. Swing se encarga del resto.
        new VentanaLogin(sistema).setVisible(true);
    }
}