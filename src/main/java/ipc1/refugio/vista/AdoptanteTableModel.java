package ipc1.refugio.vista;

import ipc1.refugio.modelo.Adoptante;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla personalizado para mostrar adoptantes en un JTable.
 *
 * Se construye manualmente (sin DefaultTableModel) para cumplir con la
 * restriccion de no usar colecciones dinamicas.
 */
public class AdoptanteTableModel extends AbstractTableModel {

    // Nombres de las columnas que se mostraran.
    private final String[] columnas = {
        "Codigo", "Nombre", "DPI", "Telefono", "Direccion", "Correo"
    };

    // Arreglo de adoptantes que se estan mostrando actualmente.
    private Adoptante[] adoptantes;

    // Cantidad de elementos no nulos al inicio del arreglo.
    private int cantidad;

    public AdoptanteTableModel() {
        this.adoptantes = new Adoptante[0];
        this.cantidad = 0;
    }

    /**
     * Reemplaza los datos de la tabla con un nuevo arreglo de adoptantes.
     */
    public void actualizar(Adoptante[] adoptantes) {
        this.adoptantes = adoptantes;
        this.cantidad = 0;
        for (int i = 0; i < adoptantes.length; i++) {
            if (adoptantes[i] != null) {
                cantidad++;
            } else {
                break;
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return cantidad;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnas[col];
    }

    @Override
    public Object getValueAt(int fila, int col) {
        Adoptante a = adoptantes[fila];
        switch (col) {
            case 0: return a.getCodigo();
            case 1: return a.getNombre();
            case 2: return a.getDpi();
            case 3: return a.getTelefono();
            case 4: return a.getDireccion();
            case 5: return a.getCorreo();
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    /**
     * Devuelve el Adoptante de la fila indicada, util para saber cual
     * esta seleccionado.
     */
    public Adoptante getAdoptanteEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return adoptantes[fila];
    }
}