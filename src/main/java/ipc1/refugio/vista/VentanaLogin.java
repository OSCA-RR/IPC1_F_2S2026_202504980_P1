package ipc1.refugio.vista;

import ipc1.refugio.modelo.Usuario;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Ventana de inicio de sesion.
// El usuario ingresa credenciales y, si son correctas, se abre VentanaPrincipal.
public class VentanaLogin extends JFrame {

    // Referencia al sistema compartido (no se crea uno nuevo, se recibe)
    private final SistemaRefugio sistema;

    // Componentes de la interfaz
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;   // oculta los caracteres
    private JButton botonIngresar;
    private JButton botonSalir;

    public VentanaLogin(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Centro de Rescate Animal - Iniciar Sesion");
        setSize(400, 300);
        setLocationRelativeTo(null);           // centrada en pantalla
        setDefaultCloseOperation(EXIT_ON_CLOSE);   // al cerrar, termina la app
        setLayout(new BorderLayout());

        inicializarComponentes();
        registrarEventos();
    }

    // Construye la interfaz: titulo arriba, formulario al centro, botones abajo
    private void inicializarComponentes() {

        // ---------- Encabezado ----------
        JLabel titulo = new JLabel("Centro de Rescate Animal", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // ---------- Formulario central ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        campoUsuario = new JTextField(15);
        panelFormulario.add(campoUsuario, gbc);

        // Fila 2: contrasena
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Contrasena:"), gbc);

        gbc.gridx = 1;
        campoContrasena = new JPasswordField(15);
        panelFormulario.add(campoContrasena, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        // ---------- Botones ----------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botonIngresar = new JButton("Ingresar");
        botonSalir = new JButton("Salir");
        panelBotones.add(botonIngresar);
        panelBotones.add(botonSalir);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Conecta los botones con sus acciones
    private void registrarEventos() {

        // Boton Ingresar
        botonIngresar.addActionListener((ActionEvent e) -> intentarLogin());

        // Boton Salir: cierra toda la aplicacion
        botonSalir.addActionListener((ActionEvent e) -> System.exit(0));

        // Enter desde el campo de contrasena tambien intenta el login
        campoContrasena.addActionListener((ActionEvent e) -> intentarLogin());
    }

    // Toma los datos ingresados, valida las credenciales y abre la ventana principal
    private void intentarLogin() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        // Validacion: campos vacios
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar usuario y contrasena.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validamos las credenciales contra el sistema
        Usuario u = sistema.validarLogin(usuario, contrasena);

        if (u == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contrasena incorrectos.",
                    "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
            campoContrasena.setText("");
            return;
        }

        // Login correcto: registramos en bitacora y abrimos la ventana principal
        sistema.registrarBitacora("LOGIN", "Ingreso del usuario " + u.getNombreUsuario());

        new VentanaPrincipal(sistema).setVisible(true);

        // Cerramos esta ventana de login
        dispose();
    }
}