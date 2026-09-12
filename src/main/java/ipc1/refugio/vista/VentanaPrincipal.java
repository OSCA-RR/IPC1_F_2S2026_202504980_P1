package ipc1.refugio.vista;

import ipc1.refugio.persistencia.GestorArchivos;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana principal del sistema.
 *
 * Muestra botones a cada modulo del refugio. Los modulos se implementaran
 * en las siguientes fases; por ahora los botones solo muestran un mensaje
 * indicando que el modulo estara disponible proximamente.
 */
public class VentanaPrincipal extends JFrame {

    private final SistemaRefugio sistema;

    public VentanaPrincipal(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Centro de Rescate Animal - Panel Principal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  // Manejamos el cierre manualmente.
        setLayout(new BorderLayout());

        inicializarComponentes();
        registrarEventos();
    }

    /**
     * Construye el menu superior, el panel central con botones y la barra
     * de estado inferior.
     */
    private void inicializarComponentes() {

        // -------- Menu superior --------
        JMenuBar barraMenu = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemGuardar = new JMenuItem("Guardar datos");
        JMenuItem itemSalir = new JMenuItem("Salir");
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        barraMenu.add(menuArchivo);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de...");
        menuAyuda.add(itemAcerca);
        barraMenu.add(menuAyuda);

        setJMenuBar(barraMenu);

        // -------- Encabezado con nombre del usuario --------
        String nombreUsuario = (sistema.getUsuarioActual() != null)
                ? sistema.getUsuarioActual().getNombreUsuario()
                : "Invitado";
        String rol = (sistema.getUsuarioActual() != null)
                ? sistema.getUsuarioActual().getRol()
                : "-";

        JLabel encabezado = new JLabel(
                "Bienvenido, " + nombreUsuario + " (" + rol + ")",
                SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 16));
        encabezado.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(encabezado, BorderLayout.NORTH);

                // -------- Panel central con botones --------
        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Boton de Animales conectado al modulo real.
        JButton botonAnimales = new JButton("Animales");
        botonAnimales.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAnimales.addActionListener(e ->
                new VentanaAnimales(sistema).setVisible(true));
        panelBotones.add(botonAnimales);

        // El resto de modulos aun no estan implementados.
        JButton botonAdoptantes = new JButton("Adoptantes");
        botonAdoptantes.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAdoptantes.addActionListener(e ->
        new VentanaAdoptantes(sistema).setVisible(true));
        panelBotones.add(botonAdoptantes);
        
        JButton botonSolicitudes = new JButton("Solicitudes");
        botonSolicitudes.setFont(new Font("Arial", Font.PLAIN, 14));
        botonSolicitudes.addActionListener(e ->
        new VentanaSolicitudes(sistema).setVisible(true));
        panelBotones.add(botonSolicitudes);
        
        JButton botonRescates = new JButton("Rescates");
        botonRescates.setFont(new Font("Arial", Font.PLAIN, 14));
        botonRescates.addActionListener(e ->
        new VentanaRescates(sistema).setVisible(true));
        panelBotones.add(botonRescates);
        
        JButton botonUbicaciones = new JButton("Ubicaciones");
        botonUbicaciones.setFont(new Font("Arial", Font.PLAIN, 14));
        botonUbicaciones.addActionListener(e ->
        new VentanaUbicaciones(sistema).setVisible(true));
        panelBotones.add(botonUbicaciones);
        
        panelBotones.add(crearBotonModulo("Reportes"));
        panelBotones.add(crearBotonModulo("Datos del Estudiante"));
        panelBotones.add(crearBotonCerrarSesion());

        add(panelBotones, BorderLayout.CENTER);

        // -------- Barra de estado inferior --------
        JLabel estado = new JLabel(" Sistema listo.");
        estado.setBorder(BorderFactory.createEtchedBorder());
        add(estado, BorderLayout.SOUTH);
    }

    /**
     * Crea un boton para un modulo generico. Por ahora solo muestra un
     * mensaje indicando que estara disponible mas adelante.
     */
    private JButton crearBotonModulo(String nombreModulo) {
        JButton boton = new JButton(nombreModulo);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "El modulo '" + nombreModulo + "' estara disponible proximamente.",
                "Modulo pendiente", JOptionPane.INFORMATION_MESSAGE));
        return boton;
    }

    /**
     * Crea el boton de cerrar sesion.
     */
    private JButton crearBotonCerrarSesion() {
        JButton boton = new JButton("Cerrar Sesion");
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(220, 80, 80));
        boton.addActionListener(e -> cerrarSesion());
        return boton;
    }

    /**
     * Registra los eventos de la ventana (por ejemplo, el cierre con la X).
     */
    private void registrarEventos() {

        // Antes de cerrar la ventana, preguntamos al usuario y guardamos.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(VentanaPrincipal.this,
                        "¿Desea salir? Se guardaran los datos.",
                        "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    salirDelSistema();
                }
            }
        });
    }

    /**
     * Cierra la sesion actual y regresa al login.
     */
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Cerrar sesion? Se guardaran los datos.",
                "Cerrar sesion", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        sistema.registrarBitacora("LOGOUT", "Cierre de sesion del usuario actual");
        GestorArchivos.guardarTodo(sistema);
        sistema.cerrarSesion();

        dispose();
        new VentanaLogin(sistema).setVisible(true);
    }

    /**
     * Guarda datos y cierra la aplicacion por completo.
     */
    private void salirDelSistema() {
        sistema.registrarBitacora("SALIDA", "Cierre de la aplicacion");
        GestorArchivos.guardarTodo(sistema);
        System.exit(0);
    }
}