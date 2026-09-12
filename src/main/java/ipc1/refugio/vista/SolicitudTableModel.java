package ipc1.refugio.vista;

import ipc1.refugio.modelo.Solicitud;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla personalizado para mostrar solicitudes en un JTable.
 *
 * Se construye manualmente (sin DefaultTableModel) para no usar
 * colecciones dinamicas. Trabaja directamente con un arreglo de Solicitud.
 */
public class SolicitudTableModel extends AbstractTableModel {

    // Columnas que se muestran.
    private final String[] columnas = {
        "Codigo", "Fecha", "Animal", "Adoptante", "Estado", "Observaciones"
    };

    private Solicitud[] solicitudes;
    private int cantidad;

    public SolicitudTableModel() {
        this.solicitudes = new Solicitud[0];
        this.cantidad = 0;
    }

    /**
     * Reemplaza los datos de la tabla con un nuevo arreglo de solicitudes.
     * Cuenta cuantos elementos no nulos hay al inicio del arreglo.
     */
    public void actualizar(Solicitud[] solicitudes) {
        this.solicitudes = solicitudes;
        this.cantidad = 0;
        for (int i = 0; i < solicitudes.length; i++) {
            if (solicitudes[i] != null) {
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
        Solicitud s = solicitudes[fila];
        switch (col) {
            case 0: return s.getCodigo();
            case 1: return s.getFecha();
            case 2: return s.getCodigoAnimal();
            case 3: return s.getCodigoAdoptante();
            case 4: return s.getEstado();
            case 5: return s.getObservaciones();
            default: return "";
        }
    }

    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    /**
     * Devuelve la solicitud de una fila, util para saber cual esta
     * seleccionada.
     */
    public Solicitud getSolicitudEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return solicitudes[fila];
    }
}