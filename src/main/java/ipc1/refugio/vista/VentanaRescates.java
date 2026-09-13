package ipc1.refugio.vista;

import ipc1.refugio.modelo.Rescate;
import ipc1.refugio.servicios.SistemaRefugio;
import ipc1.refugio.utilidades.FechaUtil;
import ipc1.refugio.utilidades.TextoLimitado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Ventana del modulo de rescates urgentes.
// Permite registrar rescates, verlos, filtrarlos y atenderlos.
// Cada accion importante queda en la bitacora.
public class VentanaRescates extends JFrame {

    private final SistemaRefugio sistema;  // referencia al sistema compartido

    // Componentes del formulario de registro
    private JTextField campoCodigo;
    private JTextField campoUbicacion;
    private JTextField campoDescripcion;
    private JComboBox<String> comboPrioridad;

    // Componentes de la tabla y el filtro
    private JTable tabla;
    private RescateTableModel modeloTabla;

    private JComboBox<String> comboFiltro;

    public VentanaRescates(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Modulo de Rescates Urgentes");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   // solo cierra esta ventana
        setLayout(new BorderLayout());

        inicializarComponentes();
        refrescarTabla();   // carga los rescates al abrir
    }

    // Construye la interfaz: formulario arriba, tabla al centro,
    // filtro y acciones abajo.
    private void inicializarComponentes() {

        // ---------- Formulario de registro ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registrar nuevo rescate"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: codigo y prioridad
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1;
        campoCodigo = new JTextField(10);
        campoCodigo.setDocument(new TextoLimitado(10));
        panelFormulario.add(campoCodigo, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 3;
        comboPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        panelFormulario.add(comboPrioridad, gbc);

        // Fila 2: ubicacion (ocupa todo el ancho)
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Ubicacion:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        campoUbicacion = new JTextField(40);
        campoUbicacion.setDocument(new TextoLimitado(100));
        panelFormulario.add(campoUbicacion, gbc);

        // Fila 3: descripcion (ocupa todo el ancho)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Descripcion:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        campoDescripcion = new JTextField(40);
        campoDescripcion.setDocument(new TextoLimitado(200));
        panelFormulario.add(campoDescripcion, gbc);

        // Fila 4: botones del formulario
        JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton botonRegistrar = new JButton("Registrar rescate");
        JButton botonLimpiar = new JButton("Limpiar");
        panelBotonesFormulario.add(botonRegistrar);
        panelBotonesFormulario.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesFormulario, gbc);

        botonRegistrar.addActionListener((ActionEvent e) -> registrarRescate());
        botonLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());

        add(panelFormulario, BorderLayout.NORTH);

        // ---------- Tabla ----------
        modeloTabla = new RescateTableModel();
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Rescates registrados"));
        add(scroll, BorderLayout.CENTER);

        // ---------- Panel de filtro y acciones ----------
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtro"));

        comboFiltro = new JComboBox<>(new String[]{
            "Todos", "Solo activos",
            "Prioridad Alta", "Prioridad Media", "Prioridad Baja"
        });
        JButton botonAplicar = new JButton("Aplicar");
        JButton botonMostrarTodos = new JButton("Mostrar todos");

        panelFiltro.add(new JLabel("Filtro:"));
        panelFiltro.add(comboFiltro);
        panelFiltro.add(botonAplicar);
        panelFiltro.add(botonMostrarTodos);

        botonAplicar.addActionListener((ActionEvent e) -> aplicarFiltro());
        botonMostrarTodos.addActionListener((ActionEvent e) -> refrescarTabla());

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton botonAtender = new JButton("Atender rescate");
        JButton botonVolver = new JButton("Volver");

        panelAcciones.add(botonAtender);
        panelAcciones.add(botonVolver);

        botonAtender.addActionListener((ActionEvent e) -> atenderRescate());
        botonVolver.addActionListener((ActionEvent e) -> dispose());

        panelSur.add(panelFiltro, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    // ---------- ACCIONES ----------

    // Registra un rescate nuevo. Valida campos vacios y codigo duplicado.
    // Todo rescate nace con estado "Activo".
    private void registrarRescate() {
        String codigo = campoCodigo.getText().trim();
        String ubicacion = campoUbicacion.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        String prioridad = (String) comboPrioridad.getSelectedItem();

        // Validacion: campos vacios
        if (codigo.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validacion: codigo duplicado
        if (sistema.existeRescateConCodigo(codigo)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un rescate con el codigo '" + codigo + "'.",
                    "Codigo duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creamos el objeto con fecha actual
        Rescate r = new Rescate(codigo, ubicacion, descripcion,
                prioridad, "Activo", FechaUtil.ahora());

        if (!sistema.agregarRescate(r)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el rescate (arreglo lleno).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        sistema.registrarBitacora("RESCATE_REGISTRADO",
                "Rescate " + codigo + " [" + prioridad + "] en " + ubicacion);

        JOptionPane.showMessageDialog(this,
                "Rescate registrado correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
        refrescarTabla();
    }

    // Limpia los campos del formulario
    private void limpiarFormulario() {
        campoCodigo.setText("");
        campoUbicacion.setText("");
        campoDescripcion.setText("");
        comboPrioridad.setSelectedIndex(0);
        campoCodigo.requestFocus();
    }

    // Refresca la tabla con todos los rescates
    private void refrescarTabla() {
        modeloTabla.actualizar(sistema.getTodosLosRescates());
    }

    // Aplica el filtro seleccionado en el combo
    private void aplicarFiltro() {
        int filtro = comboFiltro.getSelectedIndex();
        switch (filtro) {
            case 1: // solo activos
                modeloTabla.actualizar(sistema.getRescatesActivos());
                break;
            case 2: // prioridad Alta
                modeloTabla.actualizar(sistema.getRescatesPorPrioridad("Alta"));
                break;
            case 3: // prioridad Media
                modeloTabla.actualizar(sistema.getRescatesPorPrioridad("Media"));
                break;
            case 4: // prioridad Baja
                modeloTabla.actualizar(sistema.getRescatesPorPrioridad("Baja"));
                break;
            default: // todos
                refrescarTabla();
                break;
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay rescates para el filtro seleccionado.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Marca como "Atendido" el rescate seleccionado
    private void atenderRescate() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un rescate en la tabla.",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Rescate r = modeloTabla.getRescateEnFila(fila);

        // Si ya esta atendido, avisamos
        if (r.getEstado().equalsIgnoreCase("Atendido")) {
            JOptionPane.showMessageDialog(this,
                    "Este rescate ya fue atendido.",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Marcar el rescate " + r.getCodigo() + " como Atendido?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        if (sistema.atenderRescate(r.getCodigo())) {
            sistema.registrarBitacora("RESCATE_ATENDIDO",
                    "Rescate " + r.getCodigo() + " atendido");
            JOptionPane.showMessageDialog(this,
                    "Rescate atendido correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo atender el rescate.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}