package ipc1.refugio.vista;

import ipc1.refugio.modelo.Animal;
import ipc1.refugio.modelo.EspacioRefugio;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Ventana del panel de ubicaciones.
// Muestra la matriz de espacios del refugio en una tabla.
// Permite asignar animales, liberar espacios y ver la disponibilidad.
public class VentanaUbicaciones extends JFrame {

    private final SistemaRefugio sistema;  // referencia al sistema compartido

    // Componentes de la tabla
    private JTable tabla;
    private EspacioTableModel modeloTabla;

    // Etiqueta inferior que muestra libres, ocupados y porcentaje
    private JLabel etiquetaDisponibilidad;

    // Ultima celda seleccionada en la tabla (-1 = ninguna)
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;

    public VentanaUbicaciones(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Panel de Ubicaciones del Refugio");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   // solo cierra esta ventana
        setLayout(new BorderLayout());

        inicializarComponentes();
        actualizarDisponibilidad();
    }

    // Construye la interfaz: encabezado arriba, tabla al centro,
    // disponibilidad y botones abajo.
    private void inicializarComponentes() {

        // ---------- Encabezado ----------
        JLabel encabezado = new JLabel(
                "Distribucion de espacios (Area x Jaula)",
                SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 16));
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(encabezado, BorderLayout.NORTH);

        // ---------- Tabla con la matriz ----------
        modeloTabla = new EspacioTableModel(sistema);
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setCellSelectionEnabled(true);   // permite seleccionar una sola celda

        // Guardamos la ultima celda sobre la que se hizo clic
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filaSeleccionada = tabla.getSelectedRow();
                colSeleccionada = tabla.getSelectedColumn();
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "Matriz de espacios del refugio"));
        add(scroll, BorderLayout.CENTER);

        // ---------- Panel inferior: disponibilidad + botones ----------
        JPanel panelSur = new JPanel(new BorderLayout());

        // Etiqueta con el resumen de disponibilidad
        etiquetaDisponibilidad = new JLabel(" ");
        etiquetaDisponibilidad.setFont(new Font("Arial", Font.PLAIN, 13));
        etiquetaDisponibilidad.setBorder(
                BorderFactory.createEmptyBorder(8, 10, 8, 10));
        panelSur.add(etiquetaDisponibilidad, BorderLayout.WEST);

        // Botones de accion
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));

        JButton botonAsignar = new JButton("Asignar animal");
        JButton botonLiberar = new JButton("Liberar espacio");
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonVolver = new JButton("Volver");

        panelAcciones.add(botonAsignar);
        panelAcciones.add(botonLiberar);
        panelAcciones.add(botonActualizar);
        panelAcciones.add(botonVolver);

        botonAsignar.addActionListener((ActionEvent e) -> asignarAnimal());
        botonLiberar.addActionListener((ActionEvent e) -> liberarEspacio());
        botonActualizar.addActionListener((ActionEvent e) -> {
            modeloTabla.refrescar();
            actualizarDisponibilidad();
        });
        botonVolver.addActionListener((ActionEvent e) -> dispose());

        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    // Actualiza la etiqueta inferior con los conteos de espacios
    private void actualizarDisponibilidad() {
        int libres = sistema.contarEspaciosLibres();
        int ocupados = sistema.contarEspaciosOcupados();
        int total = SistemaRefugio.AREAS * SistemaRefugio.JAULAS;
        double porcentaje = sistema.porcentajeOcupacion();

        etiquetaDisponibilidad.setText(String.format(
                "Disponibilidad: %d libres / %d ocupados / %d totales   (%.1f%% ocupacion)",
                libres, ocupados, total, porcentaje));
    }

    // ---------- ACCIONES ----------

    // Asigna un animal a la celda seleccionada.
    // La celda debe estar libre y el animal debe existir y no estar asignado.
    private void asignarAnimal() {
        // Validacion: debe haber una celda seleccionada (y no la columna "Area")
        if (filaSeleccionada < 0 || colSeleccionada < 1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una celda de jaula (no la columna Area).",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int area = filaSeleccionada;
        int jaula = colSeleccionada - 1;   // columna 0 es "Area"

        EspacioRefugio e = sistema.getEspacio(area, jaula);
        if (e == null) return;

        // Validacion: la celda debe estar libre
        if (!e.estaLibre()) {
            JOptionPane.showMessageDialog(this,
                    "Esta celda ya esta ocupada por el animal " + e.getCodigoAnimal() + ".",
                    "Espacio ocupado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pedimos el codigo del animal
        String codigo = JOptionPane.showInputDialog(this,
                "Codigo del animal a asignar en Area " + area + " / Jaula " + jaula + ":");
        if (codigo == null || codigo.trim().isEmpty()) return;
        codigo = codigo.trim();

        // Validacion: el animal debe existir
        Animal a = sistema.buscarAnimalPorCodigo(codigo);
        if (a == null) {
            JOptionPane.showMessageDialog(this,
                    "No existe un animal con ese codigo.",
                    "No encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validacion: el animal no debe estar ya asignado en otro espacio
        if (a.getArea() >= 0 && a.getJaula() >= 0) {
            JOptionPane.showMessageDialog(this,
                    "El animal " + codigo + " ya esta asignado en Area "
                            + a.getArea() + " / Jaula " + a.getJaula() + ".",
                    "Animal ya asignado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Intentamos asignar el espacio
        if (sistema.asignarEspacio(area, jaula, codigo)) {
            sistema.registrarBitacora("ESPACIO_ASIGNADO",
                    "Animal " + codigo + " -> Area " + area + " / Jaula " + jaula);
            JOptionPane.showMessageDialog(this,
                    "Animal asignado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            modeloTabla.refrescar();
            actualizarDisponibilidad();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo asignar el animal.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Libera el espacio seleccionado. El animal queda sin ubicacion.
    private void liberarEspacio() {
        // Validacion: debe haber una celda seleccionada
        if (filaSeleccionada < 0 || colSeleccionada < 1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una celda de jaula (no la columna Area).",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int area = filaSeleccionada;
        int jaula = colSeleccionada - 1;

        EspacioRefugio e = sistema.getEspacio(area, jaula);
        if (e == null) return;

        // Validacion: la celda debe estar ocupada
        if (e.estaLibre()) {
            JOptionPane.showMessageDialog(this,
                    "Esta celda ya esta libre.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String codigo = e.getCodigoAnimal();

        // Confirmacion
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Liberar el espacio Area " + area + " / Jaula " + jaula
                        + " (animal " + codigo + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        if (sistema.liberarEspacio(area, jaula)) {
            sistema.registrarBitacora("ESPACIO_LIBERADO",
                    "Area " + area + " / Jaula " + jaula
                            + " liberada (era " + codigo + ")");
            JOptionPane.showMessageDialog(this,
                    "Espacio liberado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            modeloTabla.refrescar();
            actualizarDisponibilidad();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo liberar el espacio.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}