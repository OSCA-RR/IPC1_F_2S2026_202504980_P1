package ipc1.refugio.vista;

import ipc1.refugio.modelo.EspacioRefugio;
import ipc1.refugio.servicios.SistemaRefugio;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla que representa la matriz de espacios del refugio.
 *
 * La matriz real vive en SistemaRefugio.espacios y tiene AREAS filas
 * por JAULAS columnas. Este modelo la expone como una tabla con una
 * columna extra a la izquierda para el nombre del area.
 *
 * Cada celda de jaula muestra el codigo del animal asignado o "LIBRE".
 */
public class EspacioTableModel extends AbstractTableModel {

    private final SistemaRefugio sistema;

    public EspacioTableModel(SistemaRefugio sistema) {
        this.sistema = sistema;
    }

    /**
     * La primera columna es el nombre del area. Las siguientes
     * corresponden a las jaulas 0..JAULAS-1.
     */
    @Override
    public int getColumnCount() {
        return SistemaRefugio.JAULAS + 1;
    }

    @Override
    public int getRowCount() {
        return SistemaRefugio.AREAS;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) return "Area";
        return "Jaula " + (col - 1);
    }

    /**
     * Devuelve el contenido de una celda.
     *  - Columna 0: "Area N".
     *  - Otras columnas: codigo del animal o "LIBRE".
     */
    @Override
    public Object getValueAt(int fila, int col) {
        if (col == 0) {
            return "Area " + fila;
        }
        EspacioRefugio e = sistema.getEspacio(fila, col - 1);
        if (e == null) return "";
        if (e.estaLibre()) return "LIBRE";
        return e.getCodigoAnimal();
    }

    /**
     * Las celdas no son editables directamente. Los cambios se hacen
     * desde los botones de la ventana.
     */
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    /**
     * Avisa a la tabla que los datos cambiaron, para que se repinte.
     * Se llama tras asignar o liberar un espacio.
     */
    public void refrescar() {
        fireTableDataChanged();
    }
}