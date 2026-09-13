package ipc1.refugio.vista;

import ipc1.refugio.modelo.Adoptante;
import javax.swing.table.AbstractTableModel;

// Modelo de tabla para mostrar adoptantes en un JTable.
// Se hace a mano (sin DefaultTableModel) para no usar colecciones
// y para tener control directo sobre el arreglo de adoptantes.
public class AdoptanteTableModel extends AbstractTableModel {

    // Nombres de las columnas que se muestran
    private final String[] columnas = {
        "Codigo", "Nombre", "DPI", "Telefono", "Direccion", "Correo"
    };

    private Adoptante[] adoptantes;  // arreglo que se esta mostrando
    private int cantidad;            // cuantos elementos no nulos hay

    public AdoptanteTableModel() {
        this.adoptantes = new Adoptante[0];
        this.cantidad = 0;
    }

    // Reemplaza los datos actuales por un nuevo arreglo.
    // Cuenta cuantos no son nulos para saber cuantas filas dibujar.
    public void actualizar(Adoptante[] adoptantes) {
        this.adoptantes = adoptantes;
        this.cantidad = 0;
        for (int i = 0; i < adoptantes.length; i++) {
            if (adoptantes[i] != null) {
                cantidad++;
            } else {
                break;   // los datos estan al inicio
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

    // Las celdas no se editan directo, los cambios se hacen con botones
    @Override
    public boolean isCellEditable(int fila, int col) {
        return false;
    }

    // Devuelve el adoptante de una fila. Sirve para saber cual esta seleccionado.
    public Adoptante getAdoptanteEnFila(int fila) {
        if (fila < 0 || fila >= cantidad) return null;
        return adoptantes[fila];
    }
}