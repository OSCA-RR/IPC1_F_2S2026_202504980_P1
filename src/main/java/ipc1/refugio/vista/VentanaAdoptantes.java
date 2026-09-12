package ipc1.refugio.vista;

import ipc1.refugio.modelo.Adoptante;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana del modulo de adoptantes.
 *
 * Permite:
 *  - Registrar un adoptante nuevo con validacion de DPI unico.
 *  - Buscar por codigo, nombre o DPI.
 *  - Ver el listado en una tabla.
 *  - Editar los datos de contacto de un adoptante.
 *  - Desactivar (eliminacion logica) un adoptante.
 *
 * Cada accion importante queda registrada en la bitacora.
 */
public class VentanaAdoptantes extends JFrame {

    private final SistemaRefugio sistema;

    // ============================================================
    // COMPONENTES DEL FORMULARIO
    // ============================================================
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoDpi;
    private JTextField campoTelefono;
    private JTextField campoDireccion;
    private JTextField campoCorreo;

    // ============================================================
    // COMPONENTES DE LA TABLA Y BUSQUEDA
    // ============================================================
    private JTable tabla;
    private AdoptanteTableModel modeloTabla;

    private JComboBox<String> comboBusqueda;
    private JTextField campoBusqueda;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    public VentanaAdoptantes(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Modulo de Adoptantes");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
        refrescarTabla();
    }

    // ============================================================
    // CONSTRUCCION DE LA INTERFAZ
    // ============================================================
    private void inicializarComponentes() {

        // ---------- Formulario de registro ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registrar nuevo adoptante"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: codigo y nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1;
        campoCodigo = new JTextField(10);
        panelFormulario.add(campoCodigo, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 3;
        campoNombre = new JTextField(20);
        panelFormulario.add(campoNombre, gbc);

        // Fila 2: DPI y telefono
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("DPI:"), gbc);
        gbc.gridx = 1;
        campoDpi = new JTextField(15);
        panelFormulario.add(campoDpi, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 3;
        campoTelefono = new JTextField(15);
        panelFormulario.add(campoTelefono, gbc);

        // Fila 3: direccion y correo
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Direccion:"), gbc);
        gbc.gridx = 1;
        campoDireccion = new JTextField(20);
        panelFormulario.add(campoDireccion, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 3;
        campoCorreo = new JTextField(20);
        panelFormulario.add(campoCorreo, gbc);

        // Fila 4: botones del formulario
        JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton botonRegistrar = new JButton("Registrar");
        JButton botonLimpiar = new JButton("Limpiar");
        panelBotonesFormulario.add(botonRegistrar);
        panelBotonesFormulario.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesFormulario, gbc);

        botonRegistrar.addActionListener((ActionEvent e) -> registrarAdoptante());
        botonLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());

        add(panelFormulario, BorderLayout.NORTH);

        // ---------- Tabla ----------
        modeloTabla = new AdoptanteTableModel();
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado de adoptantes"));
        add(scroll, BorderLayout.CENTER);

        // ---------- Panel de busqueda y acciones ----------
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar"));

        comboBusqueda = new JComboBox<>(new String[]{
            "Codigo (exacto)", "Nombre (parcial)", "DPI (exacto)"
        });
        campoBusqueda = new JTextField(15);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(new JLabel("Criterio:"));
        panelBusqueda.add(comboBusqueda);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(botonBuscar);
        panelBusqueda.add(botonMostrarTodos);

        botonBuscar.addActionListener((ActionEvent e) -> buscar());
        botonMostrarTodos.addActionListener((ActionEvent e) -> refrescarTabla());

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton botonEditar = new JButton("Editar datos");
        JButton botonDesactivar = new JButton("Desactivar");
        JButton botonVolver = new JButton("Volver");

        panelAcciones.add(botonEditar);
        panelAcciones.add(botonDesactivar);
        panelAcciones.add(botonVolver);

        botonEditar.addActionListener((ActionEvent e) -> editarAdoptante());
        botonDesactivar.addActionListener((ActionEvent e) -> desactivarAdoptante());
        botonVolver.addActionListener((ActionEvent e) -> dispose());

        panelSur.add(panelBusqueda, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    // ============================================================
    // ACCIONES
    // ============================================================

    /**
     * Registra un adoptante nuevo validando campos vacios, DPI/codigo
     * duplicado y formato minimo del correo.
     */
    private void registrarAdoptante() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String dpi = campoDpi.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String correo = campoCorreo.getText().trim();

        // Validacion: campos vacios.
        if (codigo.isEmpty() || nombre.isEmpty() || dpi.isEmpty()
                || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validacion: DPI debe tener solo digitos y al menos 13 caracteres.
        if (!dpi.matches("\\d{13,}")) {
            JOptionPane.showMessageDialog(this,
                    "El DPI debe contener solo digitos (13 o mas).",
                    "DPI invalido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validacion: correo debe tener formato basico usuario@dominio.
        if (!correo.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                    "El correo no tiene un formato valido.",
                    "Correo invalido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validacion: codigo duplicado.
        if (sistema.existeAdoptanteConCodigo(codigo)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un adoptante con el codigo '" + codigo + "'.",
                    "Codigo duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validacion: DPI duplicado.
        if (sistema.existeAdoptanteConDpi(dpi)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un adoptante con el DPI '" + dpi + "'.",
                    "DPI duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creamos el objeto y lo agregamos al sistema.
        Adoptante a = new Adoptante(codigo, nombre, dpi, telefono, direccion, correo);

        if (!sistema.agregarAdoptante(a)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el adoptante (arreglo lleno).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registro en bitacora.
        sistema.registrarBitacora("ADOPTANTE_REGISTRADO",
                "Se registro el adoptante " + codigo + " (" + nombre + ")");

        JOptionPane.showMessageDialog(this,
                "Adoptante registrado correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
        refrescarTabla();
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarFormulario() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoDpi.setText("");
        campoTelefono.setText("");
        campoDireccion.setText("");
        campoCorreo.setText("");
        campoCodigo.requestFocus();
    }

    /**
     * Refresca la tabla con todos los adoptantes activos.
     */
    private void refrescarTabla() {
        modeloTabla.actualizar(sistema.getAdoptantesActivos());
    }

    /**
     * Busca segun el criterio seleccionado.
     */
    private void buscar() {
        String texto = campoBusqueda.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un valor para buscar.",
                    "Busqueda vacia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int criterio = comboBusqueda.getSelectedIndex();
        Adoptante[] resultado = new Adoptante[0];

        switch (criterio) {
            case 0: // Codigo exacto
                Adoptante a = sistema.buscarAdoptantePorCodigo(texto);
                if (a != null) resultado = new Adoptante[]{a};
                break;
            case 1: // Nombre parcial
                resultado = buscarAdoptantesPorNombre(texto);
                break;
            case 2: // DPI exacto
                Adoptante b = sistema.buscarAdoptantePorDpi(texto);
                if (b != null) resultado = new Adoptante[]{b};
                break;
        }

        modeloTabla.actualizar(resultado);

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron adoptantes con ese criterio.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Busqueda auxiliar por nombre parcial. Se implementa aqui porque el
     * SistemaRefugio no tiene un metodo especifico para adoptantes por
     * nombre (solo por codigo y DPI).
     */
    private Adoptante[] buscarAdoptantesPorNombre(String nombre) {
        Adoptante[] resultado = new Adoptante[SistemaRefugio.MAX_ADOPTANTES];
        int k = 0;
        Adoptante[] todos = sistema.getAdoptantes();
        for (int i = 0; i < sistema.getContadorAdoptantes(); i++) {
            if (todos[i] != null && todos[i].isActivo()
                    && todos[i].getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado[k] = todos[i];
                k++;
            }
        }
        return resultado;
    }

    /**
     * Edita los datos de contacto del adoptante seleccionado.
     * No permite cambiar el codigo ni el DPI.
     */
    private void editarAdoptante() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un adoptante en la tabla.",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Adoptante a = modeloTabla.getAdoptanteEnFila(fila);

        // Pedimos los nuevos datos con cuadros de dialogo.
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Nombre:", a.getNombre());
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

        String nuevoTelefono = JOptionPane.showInputDialog(this,
                "Telefono:", a.getTelefono());
        if (nuevoTelefono == null || nuevoTelefono.trim().isEmpty()) return;

        String nuevaDireccion = JOptionPane.showInputDialog(this,
                "Direccion:", a.getDireccion());
        if (nuevaDireccion == null || nuevaDireccion.trim().isEmpty()) return;

        String nuevoCorreo = JOptionPane.showInputDialog(this,
                "Correo:", a.getCorreo());
        if (nuevoCorreo == null || nuevoCorreo.trim().isEmpty()) return;

        // Validamos el correo.
        if (!nuevoCorreo.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this,
                    "El correo no tiene un formato valido.",
                    "Correo invalido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (sistema.editarAdoptante(a.getCodigo(), nuevoNombre.trim(),
                nuevoTelefono.trim(), nuevaDireccion.trim(), nuevoCorreo.trim())) {

            sistema.registrarBitacora("ADOPTANTE_EDITADO",
                    "Se editaron los datos del adoptante " + a.getCodigo());

            JOptionPane.showMessageDialog(this,
                    "Datos actualizados correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el adoptante.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Desactiva (eliminacion logica) el adoptante seleccionado.
     */
    private void desactivarAdoptante() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un adoptante en la tabla.",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Adoptante a = modeloTabla.getAdoptanteEnFila(fila);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desactivar al adoptante " + a.getCodigo() + " (" + a.getNombre() + ")?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        a.setActivo(false);
        sistema.registrarBitacora("ADOPTANTE_DESACTIVADO",
                "Se desactivo el adoptante " + a.getCodigo());

        JOptionPane.showMessageDialog(this,
                "Adoptante desactivado.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        refrescarTabla();
    }
}
