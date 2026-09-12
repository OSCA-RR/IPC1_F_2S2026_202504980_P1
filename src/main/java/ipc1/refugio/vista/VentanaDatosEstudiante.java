package ipc1.refugio.vista;

import ipc1.refugio.utilidades.FechaUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Ventana informativa con los datos del estudiante y del proyecto.
 *
 * No realiza operaciones sobre el sistema, es solo una pantalla de
 * presentacion que el PDF exige como parte de la interfaz principal.
 */
public class VentanaDatosEstudiante extends JFrame {

    // ============================================================
    // DATOS DEL ESTUDIANTE
    // Edita estos valores con tus datos reales.
    // ============================================================
    private static final String NOMBRE_ESTUDIANTE = "Oscar Regino Sequen Tezén"; 
    private static final String CARNET            = "202504980";
    private static final String CURSO             = "Introduccion a la Programacion y Computacion 1";
    private static final String SECCION           = "F";
    private static final String CICLO             = "Segundo Semestre 2026";
    private static final String NOMBRE_PROYECTO   = "Centro de Rescate Animal: Gestion de Refugio y Adopciones";
    private static final String REPOSITORIO       = "https://github.com/OSCA-RR/IPC1_F_2S2026_202504980_P1";

    public VentanaDatosEstudiante() {
        setTitle("Datos del Estudiante");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // -------- Encabezado --------
        JLabel encabezado = new JLabel(
                "Informacion del Proyecto",
                SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 20));
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(encabezado, BorderLayout.NORTH);

        // -------- Cuerpo: tabla de datos --------
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        // Cada dato va con una etiqueta en negrita a la izquierda y el
        // valor a la derecha.
        agregarFila(panelDatos, gbc, fila++, "Nombre completo:", NOMBRE_ESTUDIANTE);
        agregarFila(panelDatos, gbc, fila++, "Carnet:", CARNET);
        agregarFila(panelDatos, gbc, fila++, "Curso:", CURSO);
        agregarFila(panelDatos, gbc, fila++, "Seccion:", SECCION);
        agregarFila(panelDatos, gbc, fila++, "Ciclo:", CICLO);
        agregarFila(panelDatos, gbc, fila++, "Proyecto:", NOMBRE_PROYECTO);
        agregarFila(panelDatos, gbc, fila++, "Fecha actual:", FechaUtil.ahoraConSegundos());
        agregarFilaRepositorio(panelDatos, gbc, fila++);

        add(panelDatos, BorderLayout.CENTER);

        // -------- Panel inferior: boton volver --------
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener((ActionEvent e) -> dispose());
        panelSur.add(botonVolver);
        add(panelSur, BorderLayout.SOUTH);
    }

    /**
     * Agrega una fila del tipo "Etiqueta: Valor" al panel de datos.
     */
    private void agregarFila(JPanel panel, GridBagConstraints gbc,
                             int fila, String etiqueta, String valor) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel labelEtiqueta = new JLabel(etiqueta);
        labelEtiqueta.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(labelEtiqueta, gbc);

        gbc.gridx = 1;
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(labelValor, gbc);
    }

    /**
     * Agrega la fila del repositorio con un boton que abre el navegador.
     */
    private void agregarFilaRepositorio(JPanel panel, GridBagConstraints gbc, int fila) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel labelEtiqueta = new JLabel("Repositorio:");
        labelEtiqueta.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(labelEtiqueta, gbc);

        gbc.gridx = 1;
        JButton botonRepo = new JButton("Abrir en GitHub");
        botonRepo.setFont(new Font("Arial", Font.PLAIN, 12));
        botonRepo.addActionListener((ActionEvent e) -> abrirRepositorio());
        panel.add(botonRepo, gbc);
    }

    /**
     * Abre el repositorio de GitHub en el navegador por defecto.
     */
    private void abrirRepositorio() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new java.net.URI(REPOSITORIO));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Abra manualmente: " + REPOSITORIO,
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el navegador: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}