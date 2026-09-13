package ipc1.refugio.vista;

import ipc1.refugio.modelo.Solicitud;
import javax.swing.table.AbstractTableModel;

// Modelo de tabla para mostrar solicitudes en un JTable.
// Se hace a mano (sin DefaultTableModel) para no usar colecciones
// y trabajar directo sobre el arreglo de solicitudes.
public class SolicitudTableModel extends AbstractTableModel {

    // Nombres de las columnas que se muestran
    private final String[] columnas = {
        "Codigo", "Fecha", "Animal", "Adoptante", "Estado", "Observaciones"
    };

    private Solicitud[] solicitudes;  // arreglo que se esta mostrando
    private int cantidad;             // cuantos elementos no nulos hay

    public SolicitudTableModel() {
        this.solicitudes = new Solicitud[0];
        this.cantidad = 0;
    }

    // Reemplaza los datos actuales por un nuevo arreglo.
    // Cuenta cuantos no son nulos para saber cuantas filas dibujar.
    public void actualizar(Solicitud[] solicitudes) {
        this.solicitudes = solicitudes;
        this.cantidad = 0;
        for (int i = 0; i < solicitudes.length; i++) {
            if (solicitudes[i] != null) {
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

    // Las celdas no se editan directo, los cambios se hacen con botones
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    // Devuelve la solicitud de una fila. Sirve para saber cual esta seleccionada.
    public Solicitud getSolicitudEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return solicitudes[fila];
    }
}