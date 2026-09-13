package ipc1.refugio.vista;

import ipc1.refugio.modelo.Rescate;
import javax.swing.table.AbstractTableModel;

// Modelo de tabla para mostrar rescates en un JTable.
// Se hace a mano (sin DefaultTableModel) para no usar colecciones
// y trabajar directo sobre el arreglo de rescates.
public class RescateTableModel extends AbstractTableModel {

    // Nombres de las columnas que se muestran
    private final String[] columnas = {
        "Codigo", "Fecha", "Ubicacion", "Descripcion", "Prioridad", "Estado"
    };

    private Rescate[] rescates;  // arreglo que se esta mostrando
    private int cantidad;        // cuantos elementos no nulos hay

    public RescateTableModel() {
        this.rescates = new Rescate[0];
        this.cantidad = 0;
    }

    // Reemplaza los datos actuales por un nuevo arreglo.
    // Cuenta cuantos no son nulos para saber cuantas filas dibujar.
    public void actualizar(Rescate[] rescates) {
        this.rescates = rescates;
        this.cantidad = 0;
        for (int i = 0; i < rescates.length; i++) {
            if (rescates[i] != null) {
                cantidad++;
            } else {
                break;   // los datos estan al inicio del arreglo
            }
        }
        fireTableDataChanged();   // avisa al JTable que se repinte
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

    // Devuelve el valor de cada celda segun la columna
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

    // Las celdas no se editan directo, los cambios se hacen con botones
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    // Devuelve el rescate de una fila. Sirve para saber cual esta seleccionado.
    public Rescate getRescateEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return rescates[fila];
    }
}