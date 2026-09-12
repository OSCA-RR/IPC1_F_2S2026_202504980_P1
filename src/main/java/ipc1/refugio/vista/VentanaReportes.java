package ipc1.refugio.vista;

import ipc1.refugio.reportes.GeneradorReportes;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Ventana del modulo de reportes.
 *
 * Permite generar cuatro reportes HTML:
 *  - Reporte de animales.
 *  - Reporte de adopciones.
 *  - Reporte de ocupacion del refugio.
 *  - Bitacora de acciones.
 *
 * Cada boton genera el reporte correspondiente y ofrece abrirlo en el
 * navegador por defecto del sistema.
 */
public class VentanaReportes extends JFrame {

    private final SistemaRefugio sistema;

    // Etiqueta de estado para mostrar el ultimo reporte generado.
    private JLabel etiquetaEstado;

    public VentanaReportes(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Modulo de Reportes");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // -------- Encabezado --------
        JLabel encabezado = new JLabel(
                "Generacion de Reportes HTML",
                SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 18));
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(encabezado, BorderLayout.NORTH);

        // -------- Panel con botones --------
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton botonAnimales = new JButton("Reporte de Animales");
        JButton botonAdopciones = new JButton("Reporte de Adopciones");
        JButton botonOcupacion = new JButton("Reporte de Ocupacion");
        JButton botonBitacora = new JButton("Bitacora de Acciones");

        botonAnimales.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAdopciones.setFont(new Font("Arial", Font.PLAIN, 14));
        botonOcupacion.setFont(new Font("Arial", Font.PLAIN, 14));
        botonBitacora.setFont(new Font("Arial", Font.PLAIN, 14));

        panelBotones.add(botonAnimales);
        panelBotones.add(botonAdopciones);
        panelBotones.add(botonOcupacion);
        panelBotones.add(botonBitacora);

        add(panelBotones, BorderLayout.CENTER);

        // -------- Panel inferior: estado + volver --------
        JPanel panelSur = new JPanel(new BorderLayout());

        etiquetaEstado = new JLabel(" Listo. Seleccione un reporte para generar.");
        etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        panelSur.add(etiquetaEstado, BorderLayout.WEST);

        JButton botonVolver = new JButton("Volver");
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        panelVolver.add(botonVolver);
        panelSur.add(panelVolver, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        // -------- Eventos --------
        botonAnimales.addActionListener((ActionEvent e) -> generar(
                "Reporte de animales",
                GeneradorReportes.generarReporteAnimales(sistema)));

        botonAdopciones.addActionListener((ActionEvent e) -> generar(
                "Reporte de adopciones",
                GeneradorReportes.generarReporteAdopciones(sistema)));

        botonOcupacion.addActionListener((ActionEvent e) -> generar(
                "Reporte de ocupacion",
                GeneradorReportes.generarReporteOcupacion(sistema)));

        botonBitacora.addActionListener((ActionEvent e) -> generar(
                "Bitacora de acciones",
                GeneradorReportes.generarReporteBitacora(sistema)));

        botonVolver.addActionListener((ActionEvent e) -> dispose());
    }

    /**
     * Muestra el resultado de la generacion y ofrece abrir el archivo.
     */
    private void generar(String nombreReporte, String ruta) {
        if (ruta == null) {
            etiquetaEstado.setText(" Error al generar el " + nombreReporte + ".");
            JOptionPane.showMessageDialog(this,
                    "No se pudo generar el reporte.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        etiquetaEstado.setText(" Generado: " + ruta);

        sistema.registrarBitacora("REPORTE_GENERADO",
                "Se genero el " + nombreReporte + " en " + ruta);

        int opcion = JOptionPane.showConfirmDialog(this,
                "Reporte generado en:\n" + ruta + "\n\n¿Desea abrirlo en el navegador?",
                "Reporte generado", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            abrirEnNavegador(ruta);
        }
    }

    /**
     * Abre el archivo HTML en el navegador por defecto del sistema.
     * Usa Desktop.browse para ser multiplataforma.
     */
    private void abrirEnNavegador(String ruta) {
        try {
            File archivo = new File(ruta);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(archivo.toURI());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Su sistema no permite abrir el navegador automaticamente.\n"
                                + "Abra manualmente: " + ruta,
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo abrir el navegador: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}