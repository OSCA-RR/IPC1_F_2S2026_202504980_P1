package ipc1.refugio.vista;

import ipc1.refugio.modelo.Rescate;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla personalizado para mostrar rescates en un JTable.
 *
 * Se construye manualmente (sin DefaultTableModel) para no usar
 * colecciones dinamicas. Trabaja directamente con un arreglo de Rescate.
 */
public class RescateTableModel extends AbstractTableModel {

    // Columnas que se muestran.
    private final String[] columnas = {
        "Codigo", "Fecha", "Ubicacion", "Descripcion", "Prioridad", "Estado"
    };

    private Rescate[] rescates;
    private int cantidad;

    public RescateTableModel() {
        this.rescates = new Rescate[0];
        this.cantidad = 0;
    }

    /**
     * Reemplaza los datos de la tabla con un nuevo arreglo de rescates.
     * Cuenta cuantos elementos no nulos hay al inicio del arreglo.
     */
    public void actualizar(Rescate[] rescates) {
        this.rescates = rescates;
        this.cantidad = 0;
        for (int i = 0; i < rescates.length; i++) {
            if (rescates[i] != null) {
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
        Rescate r = rescates[fila];
        switch (col) {
            case 0: return r.getCodigo();
            case 1: return r.getFecha();
            case 2: return r.getUbicacion();
            case 3: return r.getDescripcion();
            case 4: return r.getPrioridad();
            case 5: return r.getEstado();
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    /**
     * Devuelve el rescate de la fila indicada, util para saber cual
     * esta seleccionado.
     */
    public Rescate getRescateEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return rescates[fila];
    }
}