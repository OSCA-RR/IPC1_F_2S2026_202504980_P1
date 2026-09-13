package ipc1.refugio.vista;

import ipc1.refugio.modelo.EspacioRefugio;
import ipc1.refugio.servicios.SistemaRefugio;
import javax.swing.table.AbstractTableModel;

// Modelo de tabla que muestra la matriz de espacios del refugio.
// La matriz real vive en SistemaRefugio.espacios, este modelo solo
// la expone como tabla con una columna extra para el nombre del area.
public class EspacioTableModel extends AbstractTableModel {

    private final SistemaRefugio sistema;  // referencia al sistema compartido

    public EspacioTableModel(SistemaRefugio sistema) {
        this.sistema = sistema;
    }

    // Una columna para el nombre del area + las columnas de jaulas
    @Override
    public int getColumnCount() {
        return SistemaRefugio.JAULAS + 1;
    }

    // Una fila por cada area del refugio
    @Override
    public int getRowCount() {
        return SistemaRefugio.AREAS;
    }

    // Columna 0 es "Area", las demas son "Jaula 0", "Jaula 1", etc.
    @Override
    public String getColumnName(int col) {
        if (col == 0) return "Area";
        return "Jaula " + (col - 1);
    }

    // Devuelve el contenido de cada celda.
    // La columna 0 muestra "Area N". Las otras muestran el codigo
    // del animal asignado o "LIBRE" si no hay nada.
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

    // Las celdas no se editan directo, los cambios se hacen con botones
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    // Avisa al JTable que los datos cambiaron para que se repinte.
    // Se llama despues de asignar o liberar un espacio.
    public void refrescar() {
        fireTableDataChanged();
    }
}