package ipc1.refugio.modelo;

public class EspacioRefugio {

    private int area;
    private int jaula;
    private int capacidadMaxima;
    private String codigoAnimal; // null si está libre

    public EspacioRefugio(int area, int jaula, int capacidadMaxima) {
        this.area = area;
        this.jaula = jaula;
        this.capacidadMaxima = capacidadMaxima;
        this.codigoAnimal = null;
    }

    public int getArea()               { return area; }
    public int getJaula()              { return jaula; }
    public int getCapacidadMaxima()    { return capacidadMaxima; }
    public String getCodigoAnimal()    { return codigoAnimal; }

    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public void setCodigoAnimal(String codigoAnimal)    { this.codigoAnimal = codigoAnimal; }

    public boolean estaLibre() {
        return codigoAnimal == null;
    }

    @Override
    public String toString() {
        return "Area " + area + " / Jaula " + jaula + " -> " + (estaLibre() ? "Libre" : codigoAnimal);
    }
}