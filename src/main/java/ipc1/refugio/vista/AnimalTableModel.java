package ipc1.refugio.vista;

import ipc1.refugio.modelo.Animal;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla personalizado para mostrar animales en un JTable.
 *
 * Se construye manualmente (sin DefaultTableModel) para cumplir con la
 * restriccion de no usar colecciones dinamicas. El modelo trabaja
 * directamente sobre un arreglo de Animal.
 */
public class AnimalTableModel extends AbstractTableModel {

    // Nombres de las columnas que se mostraran en la tabla.
    private final String[] columnas = {
        "Codigo", "Nombre", "Especie", "Raza", "Sexo",
        "Edad (meses)", "Estado Clinico", "Estado Adopcion", "Ubicacion"
    };

    // Arreglo con los animales que se estan mostrando actualmente.
    // Puede venir del sistema o de una busqueda filtrada.
    private Animal[] animales;

    // Cantidad de elementos no nulos al inicio del arreglo.
    private int cantidad;

    public AnimalTableModel() {
        this.animales = new Animal[0];
        this.cantidad = 0;
    }

    /**
     * Reemplaza los datos de la tabla con un nuevo arreglo de animales.
     * Cuenta cuantos elementos no nulos hay para saber cuantas filas pintar.
     */
    public void actualizar(Animal[] animales) {
        this.animales = animales;
        this.cantidad = 0;
        for (int i = 0; i < animales.length; i++) {
            if (animales[i] != null) {
                cantidad++;
            } else {
                break; // Los arreglos del sistema tienen los datos al inicio.
            }
        }
        // Notifica al JTable que los datos cambiaron y debe repintarse.
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

    /**
     * Devuelve el valor de una celda especifica.
     * El switch mapea cada columna al campo correspondiente del Animal.
     */
    @Override
    public Object getValueAt(int fila, int col) {
        Animal a = animales[fila];
        switch (col) {
            case 0: return a.getCodigo();
            case 1: return a.getNombre();
            case 2: return a.getEspecie();
            case 3: return a.getRaza();
            case 4: return a.getSexo();
            case 5: return a.getEdadEstimada();
            case 6: return a.getEstadoClinico();
            case 7: return a.getEstadoAdopcion();
            case 8:
                // Si tiene area y jaula asignadas, se muestra la ubicacion.
                if (a.getArea() >= 0 && a.getJaula() >= 0) {
                    return "Area " + a.getArea() + " - Jaula " + a.getJaula();
                }
                return "Sin asignar";
            default:
                return "";
        }
    }

    /**
     * Las celdas no son editables directamente, la edicion se hace
     * mediante los botones de la ventana.
     */
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    /**
     * Devuelve el Animal correspondiente a una fila de la tabla.
     * Util para saber que animal esta seleccionado.
     */
    public Animal getAnimalEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return animales[fila];
    }
}