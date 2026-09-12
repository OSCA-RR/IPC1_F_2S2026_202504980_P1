package ipc1.refugio.vista;

import ipc1.refugio.modelo.Usuario;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana de inicio de sesion.
 *
 * El usuario ingresa credenciales, se valida contra el arreglo de usuarios
 * del SistemaRefugio y, si son correctas, se abre la VentanaPrincipal.
 */
public class VentanaLogin extends JFrame {

    // Referencia al sistema compartido (no se crea uno nuevo, se recibe).
    private final SistemaRefugio sistema;

    // Componentes de la interfaz. Se declaran como atributos para poder
    // consultar su contenido desde los listeners.
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonIngresar;
    private JButton botonSalir;

    public VentanaLogin(SistemaRefugio sistema) {
        this.sistema = sistema;

        // Configuracion basica de la ventana.
        setTitle("Centro de Rescate Animal - Iniciar Sesion");
        setSize(400, 300);
        setLocationRelativeTo(null);        // Centrada en pantalla.
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Construimos la interfaz.
        inicializarComponentes();

        // Registramos los eventos de los botones.
        registrarEventos();
    }

    /**
     * Crea y acomoda los componentes visuales de la ventana.
     * Se usa un panel con GridBagLayout para tener control fino de la
     * posicion de los campos.
     */
    private void inicializarComponentes() {

        // -------- Encabezado --------
        JLabel titulo = new JLabel("Centro de Rescate Animal", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // -------- Formulario central --------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);   // Margen entre componentes.
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: etiqueta + campo de usuario.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        campoUsuario = new JTextField(15);
        panelFormulario.add(campoUsuario, gbc);

        // Fila 2: etiqueta + campo de contrasena.
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Contrasena:"), gbc);

        gbc.gridx = 1;
        campoContrasena = new JPasswordField(15);
        panelFormulario.add(campoContrasena, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        // -------- Botones --------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botonIngresar = new JButton("Ingresar");
        botonSalir = new JButton("Salir");
        panelBotones.add(botonIngresar);
        panelBotones.add(botonSalir);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Conecta los botones con sus acciones correspondientes.
     */
    private void registrarEventos() {

        // Accion del boton Ingresar.
        botonIngresar.addActionListener((ActionEvent e) -> intentarLogin());

        // Accion del boton Salir: cierra la aplicacion.
        botonSalir.addActionListener((ActionEvent e) -> System.exit(0));

        // Permite iniciar sesion presionando Enter desde el campo de contrasena.
        campoContrasena.addActionListener((ActionEvent e) -> intentarLogin());
    }

    /**
     * Toma los datos ingresados por el usuario, valida las credenciales
     * contra el sistema y, si son correctas, abre la ventana principal.
     */
    private void intentarLogin() {
        String usuario = campoUsuario.getText().trim();
        String contrasena = new String(campoContrasena.getPassword());

        // Validacion basica: campos vacios.
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar usuario y contrasena.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Intentamos validar el login contra los usuarios cargados.
        Usuario u = sistema.validarLogin(usuario, contrasena);

        if (u == null) {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contrasena incorrectos.",
                    "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
            campoContrasena.setText("");
            return;
        }

        // Login correcto: registramos en bitacora y abrimos la ventana principal.
        sistema.registrarBitacora("LOGIN", "Ingreso del usuario " + u.getNombreUsuario());

        // Abrimos la ventana principal pasandole el sistema y el usuario.
        new VentanaPrincipal(sistema).setVisible(true);

        // Cerramos esta ventana de login.
        dispose();
    }
}
