package ipc1.refugio.vista;

import ipc1.refugio.modelo.Animal;
import ipc1.refugio.servicios.SistemaRefugio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana del modulo de animales.
 *
 * Permite:
 *  - Registrar un animal nuevo.
 *  - Buscar por codigo, nombre, especie o estado de adopcion.
 *  - Ver los animales en una tabla.
 *  - Editar el estado clinico y de adopcion.
 *  - Eliminar logicamente un animal.
 *
 * Toda accion queda registrada en la bitacora del sistema.
 */
public class VentanaAnimales extends JFrame {

    // Referencia al sistema compartido.
    private final SistemaRefugio sistema;

    // ============================================================
    // COMPONENTES DEL FORMULARIO DE REGISTRO
    // ============================================================
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JComboBox<String> comboEspecie;
    private JTextField campoRaza;
    private JComboBox<String> comboSexo;
    private JTextField campoEdad;
    private JComboBox<String> comboEstadoClinico;
    private JComboBox<String> comboEstadoAdopcion;

    // ============================================================
    // COMPONENTES DE LA TABLA Y BUSQUEDA
    // ============================================================
    private JTable tabla;
    private AnimalTableModel modeloTabla;

    private JComboBox<String> comboBusqueda;
    private JTextField campoBusqueda;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    public VentanaAnimales(SistemaRefugio sistema) {
        this.sistema = sistema;

        setTitle("Modulo de Animales");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Solo cierra esta ventana.
        setLayout(new BorderLayout());

        inicializarComponentes();
        registrarEventos();
        refrescarTabla(); // Carga los animales al abrir la ventana.
    }

    // ============================================================
    // CONSTRUCCION DE LA INTERFAZ
    // ============================================================
    private void inicializarComponentes() {

        // ---------- Formulario de registro (parte superior) ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "Registrar nuevo animal"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Codigo y Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1;
        campoCodigo = new JTextField(10);
        panelFormulario.add(campoCodigo, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3;
        campoNombre = new JTextField(15);
        panelFormulario.add(campoNombre, gbc);

        // Fila 2: Especie y Raza
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        comboEspecie = new JComboBox<>(new String[]{"Perro", "Gato"});
        panelFormulario.add(comboEspecie, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 3;
        campoRaza = new JTextField(15);
        panelFormulario.add(campoRaza, gbc);

        // Fila 3: Sexo y Edad
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        comboSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        panelFormulario.add(comboSexo, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Edad estimada (meses):"), gbc);
        gbc.gridx = 3;
        campoEdad = new JTextField(10);
        panelFormulario.add(campoEdad, gbc);

        // Fila 4: Estado clinico y estado de adopcion
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Estado clinico:"), gbc);
        gbc.gridx = 1;
        comboEstadoClinico = new JComboBox<>(
                new String[]{"Sano", "En tratamiento", "Critico"});
        panelFormulario.add(comboEstadoClinico, gbc);

        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Estado adopcion:"), gbc);
        gbc.gridx = 3;
        comboEstadoAdopcion = new JComboBox<>(
                new String[]{"Disponible", "En proceso", "Adoptado", "No apto"});
        panelFormulario.add(comboEstadoAdopcion, gbc);

        // Fila 5: Botones del formulario
        JPanel panelBotonesFormulario = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton botonRegistrar = new JButton("Registrar");
        JButton botonLimpiar = new JButton("Limpiar");
        panelBotonesFormulario.add(botonRegistrar);
        panelBotonesFormulario.add(botonLimpiar);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 4;
        panelFormulario.add(panelBotonesFormulario, gbc);

        // Conectamos los botones del formulario.
        botonRegistrar.addActionListener((ActionEvent e) -> registrarAnimal());
        botonLimpiar.addActionListener((ActionEvent e) -> limpiarFormulario());

        add(panelFormulario, BorderLayout.NORTH);

        // ---------- Tabla (parte central) ----------
        modeloTabla = new AnimalTableModel();
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado de animales"));
        add(scroll, BorderLayout.CENTER);

        // ---------- Panel de busqueda + acciones (parte inferior) ----------
        JPanel panelSur = new JPanel(new BorderLayout());

        // Sub-panel: busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar"));

        comboBusqueda = new JComboBox<>(new String[]{
            "Codigo (exacto)", "Nombre (parcial)",
            "Especie", "Estado adopcion"
        });
        campoBusqueda = new JTextField(15);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(new JLabel("Criterio:"));
        panelBusqueda.add(comboBusqueda);
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(botonBuscar);
        panelBusqueda.add(botonMostrarTodos);

        // Conectamos los botones de busqueda.
        botonBuscar.addActionListener((ActionEvent e) -> buscar());
        botonMostrarTodos.addActionListener((ActionEvent e) -> refrescarTabla());

        // Sub-panel: acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton botonEditar = new JButton("Editar estado");
        JButton botonEliminar = new JButton("Eliminar logico");
        JButton botonVolver = new JButton("Volver");

        panelAcciones.add(botonEditar);
        panelAcciones.add(botonEliminar);
        panelAcciones.add(botonVolver);

        botonEditar.addActionListener((ActionEvent e) -> editarEstado());
        botonEliminar.addActionListener((ActionEvent e) -> eliminarLogico());
        botonVolver.addActionListener((ActionEvent e) -> dispose());

        panelSur.add(panelBusqueda, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);
    }

    // ============================================================
    // EVENTOS DE VENTANA
    // ============================================================
    private void registrarEventos() {
        // No se requiere nada extra por ahora. Todos los eventos se
        // conectaron directamente en inicializarComponentes.
    }

    // ============================================================
    // ACCIONES
    // ============================================================

    /**
     * Registra un animal nuevo en el sistema.
     * Valida campos vacios, edad numerica y duplicados de codigo.
     */
    private void registrarAnimal() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String especie = (String) comboEspecie.getSelectedItem();
        String raza = campoRaza.getText().trim();
        String sexo = (String) comboSexo.getSelectedItem();
        String edadTexto = campoEdad.getText().trim();
        String estadoClinico = (String) comboEstadoClinico.getSelectedItem();
        String estadoAdopcion = (String) comboEstadoAdopcion.getSelectedItem();

        // Validacion: campos vacios.
        if (codigo.isEmpty() || nombre.isEmpty() || raza.isEmpty() || edadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validacion: edad debe ser numero entero positivo.
        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
            if (edad < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un numero entero positivo.",
                    "Edad invalida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validacion: codigo duplicado.
        if (sistema.existeAnimalConCodigo(codigo)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un animal con el codigo '" + codigo + "'.",
                    "Codigo duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creacion del objeto Animal.
        Animal a = new Animal(codigo, nombre, especie, raza, sexo,
                edad, estadoClinico, estadoAdopcion);

        // Intento de agregar al sistema.
        if (!sistema.agregarAnimal(a)) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el animal (arreglo lleno).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registro en bitacora.
        sistema.registrarBitacora("ANIMAL_REGISTRADO",
                "Se registro el animal " + codigo + " (" + nombre + ")");

        JOptionPane.showMessageDialog(this,
                "Animal registrado correctamente.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
        refrescarTabla();
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoRaza.setText("");
        campoEdad.setText("");
        comboEspecie.setSelectedIndex(0);
        comboSexo.setSelectedIndex(0);
        comboEstadoClinico.setSelectedIndex(0);
        comboEstadoAdopcion.setSelectedIndex(0);
        campoCodigo.requestFocus();
    }

    /**
     * Refresca la tabla con todos los animales activos del sistema.
     */
    private void refrescarTabla() {
        modeloTabla.actualizar(sistema.getAnimalesActivos());
    }

    /**
     * Realiza la busqueda segun el criterio seleccionado.
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
        Animal[] resultado = new Animal[0];

        switch (criterio) {
            case 0: // Codigo exacto
                Animal a = sistema.buscarAnimalPorCodigo(texto);
                if (a != null) {
                    resultado = new Animal[]{a};
                }
                break;
            case 1: // Nombre parcial
                resultado = sistema.buscarAnimalesPorNombre(texto);
                break;
            case 2: // Especie
                resultado = sistema.buscarAnimalesPorEspecie(texto);
                break;
            case 3: // Estado de adopcion
                resultado = sistema.buscarAnimalesPorEstado(texto);
                break;
        }

        modeloTabla.actualizar(resultado);

        // Si no hay resultados, avisamos al usuario.
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron animales con ese criterio.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Edita el estado clinico y de adopcion del animal seleccionado
     * en la tabla (o de uno buscado por codigo si no hay seleccion).
     */
    private void editarEstado() {
        int fila = tabla.getSelectedRow();
        Animal a;

        if (fila >= 0) {
            a = modeloTabla.getAnimalEnFila(fila);
        } else {
            // Si no hay fila seleccionada, se pide el codigo.
            String codigo = JOptionPane.showInputDialog(this,
                    "Ingrese el codigo del animal a editar:");
            if (codigo == null || codigo.trim().isEmpty()) return;
            a = sistema.buscarAnimalPorCodigo(codigo.trim());
            if (a == null) {
                JOptionPane.showMessageDialog(this,
                        "No existe un animal con ese codigo.",
                        "No encontrado", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Pedimos los nuevos estados con dos cuadros de dialogo.
        String[] opcionesClinico = {"Sano", "En tratamiento", "Critico"};
        String nuevoClinico = (String) JOptionPane.showInputDialog(
                this, "Nuevo estado clinico:", "Editar estado",
                JOptionPane.QUESTION_MESSAGE, null,
                opcionesClinico, a.getEstadoClinico());
        if (nuevoClinico == null) return;

        String[] opcionesAdopcion = {"Disponible", "En proceso", "Adoptado", "No apto"};
        String nuevoAdopcion = (String) JOptionPane.showInputDialog(
                this, "Nuevo estado de adopcion:", "Editar estado",
                JOptionPane.QUESTION_MESSAGE, null,
                opcionesAdopcion, a.getEstadoAdopcion());
        if (nuevoAdopcion == null) return;

        // Aplicamos el cambio.
        if (sistema.editarEstadoAnimal(a.getCodigo(), nuevoClinico, nuevoAdopcion)) {
            sistema.registrarBitacora("ANIMAL_EDITADO",
                    "Animal " + a.getCodigo() + " -> " + nuevoClinico + " / " + nuevoAdopcion);
            JOptionPane.showMessageDialog(this,
                    "Estado actualizado correctamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el estado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina logicamente el animal seleccionado (o el ingresado por codigo).
     * La eliminacion logica significa marcarlo como inactivo, no borrarlo
     * del arreglo.
     */
    private void eliminarLogico() {
        int fila = tabla.getSelectedRow();
        String codigo;

        if (fila >= 0) {
            Animal a = modeloTabla.getAnimalEnFila(fila);
            codigo = a.getCodigo();
        } else {
            codigo = JOptionPane.showInputDialog(this,
                    "Ingrese el codigo del animal a eliminar:");
            if (codigo == null || codigo.trim().isEmpty()) return;
            codigo = codigo.trim();
        }

        // Confirmacion antes de eliminar.
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar logicamente el animal " + codigo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;

        if (sistema.eliminarAnimalLogico(codigo)) {
            sistema.registrarBitacora("ANIMAL_ELIMINADO",
                    "Se elimino logicamente el animal " + codigo);
            JOptionPane.showMessageDialog(this,
                    "Animal eliminado logicamente.",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se encontro el animal.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}