package ipc1.refugio.vista;

import ipc1.refugio.persistencia.GestorArchivos;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Ventana principal del sistema.
// Muestra botones a cada modulo y controla el cierre de sesion
// y el cierre de la aplicacion (guardando datos antes de salir).
public class VentanaPrincipal extends JFrame {

    private final SistemaRefugio sistema;  // referencia al sistema compartido

    public VentanaPrincipal(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Centro de Rescate Animal - Panel Principal");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  // manejamos el cierre a mano
        setLayout(new BorderLayout());

        inicializarComponentes();
        registrarEventos();
    }

    // Construye el menu superior, el panel central de botones
    // y la barra de estado inferior.
    private void inicializarComponentes() {

        // ---------- Menu superior ----------
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

        // ---------- Encabezado con nombre del usuario ----------
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

        // ---------- Panel central con botones ----------
        JPanel panelBotones = new JPanel(new GridLayout(3, 3, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Boton de Animales
        JButton botonAnimales = new JButton("Animales");
        botonAnimales.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAnimales.addActionListener(e ->
                new VentanaAnimales(sistema).setVisible(true));
        panelBotones.add(botonAnimales);

        // Boton de Adoptantes
        JButton botonAdoptantes = new JButton("Adoptantes");
        botonAdoptantes.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAdoptantes.addActionListener(e ->
                new VentanaAdoptantes(sistema).setVisible(true));
        panelBotones.add(botonAdoptantes);

        // Boton de Solicitudes
        JButton botonSolicitudes = new JButton("Solicitudes");
        botonSolicitudes.setFont(new Font("Arial", Font.PLAIN, 14));
        botonSolicitudes.addActionListener(e ->
                new VentanaSolicitudes(sistema).setVisible(true));
        panelBotones.add(botonSolicitudes);

        // Boton de Rescates
        JButton botonRescates = new JButton("Rescates");
        botonRescates.setFont(new Font("Arial", Font.PLAIN, 14));
        botonRescates.addActionListener(e ->
                new VentanaRescates(sistema).setVisible(true));
        panelBotones.add(botonRescates);

        // Boton de Ubicaciones
        JButton botonUbicaciones = new JButton("Ubicaciones");
        botonUbicaciones.setFont(new Font("Arial", Font.PLAIN, 14));
        botonUbicaciones.addActionListener(e ->
                new VentanaUbicaciones(sistema).setVisible(true));
        panelBotones.add(botonUbicaciones);

        // Boton de Reportes
        JButton botonReportes = new JButton("Reportes");
        botonReportes.setFont(new Font("Arial", Font.PLAIN, 14));
        botonReportes.addActionListener(e ->
                new VentanaReportes(sistema).setVisible(true));
        panelBotones.add(botonReportes);

        // Boton de Datos del Estudiante
        JButton botonDatos = new JButton("Datos del Estudiante");
        botonDatos.setFont(new Font("Arial", Font.PLAIN, 14));
        botonDatos.addActionListener(e ->
                new VentanaDatosEstudiante().setVisible(true));
        panelBotones.add(botonDatos);

        // Boton de cerrar sesion
        panelBotones.add(crearBotonCerrarSesion());

        add(panelBotones, BorderLayout.CENTER);

        // ---------- Barra de estado inferior ----------
        JLabel estado = new JLabel(" Sistema listo.");
        estado.setBorder(BorderFactory.createEtchedBorder());
        add(estado, BorderLayout.SOUTH);
    }

    // Crea el boton de cerrar sesion (rojo)
    private JButton crearBotonCerrarSesion() {
        JButton boton = new JButton("Cerrar Sesion");
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(220, 80, 80));
        boton.addActionListener(e -> cerrarSesion());
        return boton;
    }

    // Registra el cierre de ventana con la X
    private void registrarEventos() {

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

    // Cierra la sesion actual y vuelve al login.
    // Guarda los datos antes de salir.
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

    // Guarda los datos y cierra toda la aplicacion
    private void salirDelSistema() {
        sistema.registrarBitacora("SALIDA", "Cierre de la aplicacion");
        GestorArchivos.guardarTodo(sistema);
        System.exit(0);
    }
}