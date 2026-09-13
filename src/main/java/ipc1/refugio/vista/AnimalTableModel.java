package ipc1.refugio.vista;

import ipc1.refugio.modelo.Animal;
import javax.swing.table.AbstractTableModel;

// Modelo de tabla para mostrar animales en un JTable.
// Se hace a mano (sin DefaultTableModel) para no usar colecciones
// y para trabajar directo sobre el arreglo de animales.
public class AnimalTableModel extends AbstractTableModel {

    // Nombres de las columnas que se muestran
    private final String[] columnas = {
        "Codigo", "Nombre", "Especie", "Raza", "Sexo",
        "Edad (meses)", "Estado Clinico", "Estado Adopcion", "Ubicacion"
    };

    private Animal[] animales;  // arreglo que se esta mostrando
    private int cantidad;       // cuantos elementos no nulos hay

    public AnimalTableModel() {
        this.animales = new Animal[0];
        this.cantidad = 0;
    }

    // Reemplaza los datos actuales por un nuevo arreglo.
    // Cuenta cuantos no son nulos para saber cuantas filas dibujar.
    public void actualizar(Animal[] animales) {
        this.animales = animales;
        this.cantidad = 0;
        for (int i = 0; i < animales.length; i++) {
            if (animales[i] != null) {
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

    // Devuelve el valor de cada celda segun la columna.
    // El switch mapea cada columna al campo correspondiente del Animal.
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
                // Si no, se muestra "Sin asignar".
                if (a.getArea() >= 0 && a.getJaula() >= 0) {
                    return "Area " + a.getArea() + " - Jaula " + a.getJaula();
                }
                return "Sin asignar";
            default:
                return "";
        }
    }

    // Las celdas no se editan directo, los cambios se hacen con botones
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    // Devuelve el animal de una fila. Sirve para saber cual esta seleccionado.
    public Animal getAnimalEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return animales[fila];
    }
}