package ipc1.refugio.vista;

import ipc1.refugio.modelo.Adoptante;
import ipc1.refugio.modelo.Animal;
import ipc1.refugio.modelo.Solicitud;
import ipc1.refugio.servicios.SistemaRefugio;
import ipc1.refugio.utilidades.FechaUtil;
import ipc1.refugio.utilidades.TextoLimitado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Ventana del modulo de solicitudes de adopcion.
// Permite registrar solicitudes, cambiar su estado, filtrar pendientes
// y consultar el historial de solicitudes por animal.
// Al aprobar una solicitud, el animal pasa a "Adoptado" automaticamente.
public class VentanaSolicitudes extends JFrame {

    private final SistemaRefugio sistema;  // referencia al sistema compartido

    // Componentes del formulario de registro
    private JTextField campoCodigo;
    private JComboBox<String> comboAnimales;    // "A001 - Firulais"
    private JComboBox<String> comboAdoptantes;  // "AD001 - Juan"
    private JTextField campoObservaciones;

    // Componentes de la tabla y busqueda
    private JTable tabla;
    private SolicitudTableModel modeloTabla;

    private JComboBox<String> comboFiltro;
    private JTextField campoBusquedaAnimal;

    public VentanaSolicitudes(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Modulo de Solicitudes de Adopcion");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   // solo cierra esta ventana
        setLayout(new BorderLayout());

        inicializarComponentes();
        refrescarTabla();   // carga las solicitudes al abrir
    }

    // Construye la interfaz: formulario arriba, tabla al centro,
    // filtros y acciones abajo.
    private void inicializarComponentes() {

        // ---------- Formulario de registro ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registrar nueva solicitud"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: codigo y fecha (fecha solo informativa)
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Codigo solicitud:"), gbc);
        gbc.gridx = 1;
        campoCodigo = new JTextField(10);
        campoCodigo.setDocument(new TextoLimitado(10));
        panelFormulario.add(campoCodigo, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Fecha (automatica):"), gbc);
        gbc.gridx = 3;
        JTextField campoFecha = new JTextField(FechaUtil.ahora(), 18);
        campoFecha.setEditable(false);   // solo lectura
        panelFormulario.add(campoFecha, gbc);

        // Fila 2: animal y adoptante
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Animal:"), gbc);
        gbc.gridx = 1;
        comboAnimales = new JComboBox<>();
        panelFormulario.add(comboAnimales, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Adoptante:"), gbc);
        gbc.gridx = 3;
        comboAdoptantes = new JComboBox<>();
        panelFormulario.add(comboAdoptantes, gbc);

        // Fila 3: observaciones
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        campoObservaciones = new JTextField(40);
        campoObservaciones.setDocument(new TextoLimitado(200));
        panelFormulario.add(campoObservaciones, gbc);

        // Fila 4: botones del formulario
        JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton botonRegistrar = new JButton("Registrar solicitud");
        JButton botonLimpiar = new JButton("Limpiar");
        JButton botonRefrescarCombos = new JButton("Refrescar listas");
        panelBotonesFormulario.add(botonRegistrar);
        panelBotonesFormulario.add(botonLimpiar);
        panelBotonesFormulario.add(botonRefrescarCombos);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesFormulario, gbc);

        botonRegistrar.addActionListener((ActionEvent e) -> registrarSolicitud());
        botonLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());
        botonRefrescarCombos.addActionListener((ActionEvent e) -> cargarCombos());

        add(panelFormulario, BorderLayout.NORTH);

        // ---------- Tabla ----------
        modeloTabla = new SolicitudTableModel();
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Solicitudes registradas"));
        add(scroll, BorderLayout.CENTER);

        // ---------- Panel de filtro/busqueda y acciones ----------
        JPanel panelSur = new JPanel(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Filtrar / Buscar"));

        comboFiltro = new JComboBox<>(new String[]{"Todas", "Pendientes"});
        campoBusquedaAnimal = new JTextField(10);
        JButton botonFiltrar = new JButton("Aplicar");
        JButton botonHistorial = new JButton("Historial por animal");

        panelBusqueda.add(new JLabel("Filtro:"));
        panelBusqueda.add(comboFiltro);
        panelBusqueda.add(botonFiltrar);
        panelBusqueda.add(new JLabel("   Codigo animal:"));
        panelBusqueda.add(campoBusquedaAnimal);
        panelBusqueda.add(botonHistorial);

        botonFiltrar.addActionListener((ActionEvent e) -> aplicarFiltro());
        botonHistorial.addActionListener((ActionEvent e) -> historialPorAnimal());

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton botonCambiarEstado = new JButton("Cambiar estado");
        JButton botonVolver = new JButton("Volver");

        panelAcciones.add(botonCambiarEstado);
        panelAcciones.add(botonVolver);

        botonCambiarEstado.addActionListener((ActionEvent e) -> cambiarEstado());
        botonVolver.addActionListener((ActionEvent e) -> dispose());

        panelSur.add(panelBusqueda, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        // Cargamos los combos al abrir la ventana
        cargarCombos();
    }

    // ---------- CARGA DE COMBOS ----------

    // Llena los combos con los animales DISPONIBLES y los adoptantes activos.
    // Solo se cargan animales "Disponible" porque un animal ya en proceso
    // o adoptado no debe poder recibir otra solicitud.
    private void cargarCombos() {
        // Limpiamos los combos
        comboAnimales.removeAllItems();
        comboAdoptantes.removeAllItems();

        // Cargamos animales que se pueden solicitar
        Animal[] animales = sistema.getAnimalesActivos();
        for (int i = 0; i < animales.length; i++) {
            Animal a = animales[i];
            if (a == null) break;
            // Solo se pueden solicitar animales con estado "Disponible"
            if (a.getEstadoAdopcion().equalsIgnoreCase("Disponible")) {
                comboAnimales.addItem(a.getCodigo() + " - " + a.getNombre());
            }
        }

        // Cargamos adoptantes activos
        Adoptante[] adoptantes = sistema.getAdoptantesActivos();
        for (int i = 0; i < adoptantes.length; i++) {
            Adoptante ad = adoptantes[i];
            if (ad == null) break;
            comboAdoptantes.addItem(ad.getCodigo() + " - " + ad.getNombre());
        }

        // Si alguno quedo vacio, mostramos un aviso en el combo
        if (comboAnimales.getItemCount() == 0) {
            comboAnimales.addItem("(Sin animales disponibles)");
        }
        if (comboAdoptantes.getItemCount() == 0) {
            comboAdoptantes.addItem("(Sin adoptantes activos)");
        }
    }

    // Extrae el codigo de un item del combo tipo "A001 - Firulais".
    // Devuelve null si el texto no tiene el formato esperado.
    private String extraerCodigoDeCombo(JComboBox<String> combo) {
        Object sel = combo.getSelectedItem();
        if (sel == null) return null;
        String texto = sel.toString();
        int guion = texto.indexOf(" - ");
        if (guion < 0) return null;
        return texto.substring(0, guion).trim();
    }

    // ---------- ACCIONES ----------

    // Registra una nueva solicitud. Valida campos y que exista
    // animal disponible y adoptante activo.
    private void registrarSolicitud() {
        String codigo = campoCodigo.getText().trim();
        String observaciones = campoObservaciones.getText().trim();

        // Validacion: codigo obligatorio
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar el codigo de la solicitud.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validacion: debe haber animal y adoptante seleccionados
        String codigoAnimal = extraerCodigoDeCombo(comboAnimales);
        String codigoAdoptante = extraerCodigoDeCombo(comboAdoptantes);

        if (codigoAnimal == null || codigoAdoptante == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un animal disponible y un adoptante activo.",
                    "Datos insuficientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validacion: codigo duplicado
        if (sistema.existeSolicitudConCodigo(codigo)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe una solicitud con el codigo '" + codigo + "'.",
                    "Codigo duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creamos la solicitud con fecha actual y estado inicial "Pendiente"
        Solicitud s = new Solicitud(codigo, codigoAnimal, codigoAdoptante,
                FechaUtil.ahora(), "Pendiente", observaciones);

        if (!sistema.agregarSolicitud(s)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la solicitud (arreglo lleno).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Marcamos el animal como "En proceso" para que no aparezca en
        // futuras solicitudes
        Animal animal = sistema.buscarAnimalPorCodigo(codigoAnimal);
        if (animal != null) {
            animal.setEstadoAdopcion("En proceso");
        }

        sistema.registrarBitacora("SOLICITUD_REGISTRADA",
                "Solicitud " + codigo + " para animal " + codigoAnimal
                        + " por adoptante " + codigoAdoptante);

        JOptionPane.showMessageDialog(this,
                "Solicitud registrada correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
        cargarCombos();
        refrescarTabla();
    }

    // Limpia los campos del formulario
    private void limpiarFormulario() {
        campoCodigo.setText("");
        campoObservaciones.setText("");
        if (comboAnimales.getItemCount() > 0) comboAnimales.setSelectedIndex(0);
        if (comboAdoptantes.getItemCount() > 0) comboAdoptantes.setSelectedIndex(0);
        campoCodigo.requestFocus();
    }

    // Refresca la tabla con todas las solicitudes
    private void refrescarTabla() {
        modeloTabla.actualizar(sistema.getTodasLasSolicitudes());
    }

    // Aplica el filtro seleccionado (Todas / Pendientes)
    private void aplicarFiltro() {
        int filtro = comboFiltro.getSelectedIndex();
        if (filtro == 1) {
            // Pendientes
            modeloTabla.actualizar(sistema.getSolicitudesPendientes());
        } else {
            // Todas
            refrescarTabla();
        }

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay solicitudes para el filtro seleccionado.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Muestra el historial de solicitudes de un animal especifico
    private void historialPorAnimal() {
        String codigo = campoBusquedaAnimal.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el codigo del animal.",
                    "Campo vacio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificamos que el animal exista
        if (sistema.buscarAnimalPorCodigo(codigo) == null) {
            JOptionPane.showMessageDialog(this,
                    "No existe un animal con ese codigo.",
                    "No encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.actualizar(sistema.getSolicitudesPorAnimal(codigo));

        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Ese animal no tiene solicitudes registradas.",
                    "Sin historial", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Cambia el estado de la solicitud seleccionada.
    // Si se aprueba, el animal pasa a "Adoptado".
    // Si se rechaza o cancela, el animal vuelve a "Disponible".
    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una solicitud en la tabla.",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Solicitud s = modeloTabla.getSolicitudEnFila(fila);

        // Opciones de estado para el usuario
        String[] opciones = {"Pendiente", "Aprobada", "Rechazada", "Cancelada"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(
                this,
                "Nuevo estado para la solicitud " + s.getCodigo() + ":",
                "Cambiar estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                s.getEstado());

        if (nuevoEstado == null) return;   // el usuario cancelo

        // Llamamos al sistema para cambiar el estado.
        // El sistema maneja el paso a "Adoptado" si es aprobada.
        if (!sistema.cambiarEstadoSolicitud(s.getCodigo(), nuevoEstado)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cambiar el estado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si se rechaza o cancela, el animal vuelve a "Disponible"
        Animal animal = sistema.buscarAnimalPorCodigo(s.getCodigoAnimal());
        if (animal != null) {
            if (nuevoEstado.equalsIgnoreCase("Rechazada")
                    || nuevoEstado.equalsIgnoreCase("Cancelada")) {
                animal.setEstadoAdopcion("Disponible");
            }
        }

        sistema.registrarBitacora("SOLICITUD_ESTADO",
                "Solicitud " + s.getCodigo() + " -> " + nuevoEstado);

        JOptionPane.showMessageDialog(this,
                "Estado actualizado a '" + nuevoEstado + "'.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);

        cargarCombos();
        refrescarTabla();
    }
}