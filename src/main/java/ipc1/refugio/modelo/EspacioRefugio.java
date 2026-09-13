package ipc1.refugio.modelo;

// Representa una celda de la matriz de espacios del refugio.
// Cada objeto es una combinacion area/jaula que puede estar libre u ocupada.
public class EspacioRefugio {

    private int area;              // fila de la matriz
    private int jaula;             // columna de la matriz
    private int capacidadMaxima;   // capacidad de la jaula (informativo)
    private String codigoAnimal;   // codigo del animal asignado, o null si esta libre

    public EspacioRefugio(int area, int jaula, int capacidadMaxima) {
        this.area = area;
        this.jaula = jaula;
        this.capacidadMaxima = capacidadMaxima;
        this.codigoAnimal = null;  // nace libre
    }

    // Getters
    public int getArea()               { return area; }
    public int getJaula()              { return jaula; }
    public int getCapacidadMaxima()    { return capacidadMaxima; }
    public String getCodigoAnimal()    { return codigoAnimal; }

    // Setters
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public void setCodigoAnimal(String codigoAnimal)    { this.codigoAnimal = codigoAnimal; }

    // Una celda esta libre si no tiene animal asignado
    public boolean estaLibre() {
        return codigoAnimal == null;
    }

    // Se usa para mostrar el espacio de forma legible
    @Override
    public String toString() {
        return "Area " + area + " / Jaula " + jaula + " -> " + (estaLibre() ? "Libre" : codigoAnimal);
    }
}