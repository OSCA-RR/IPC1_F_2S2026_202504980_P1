package ipc1.refugio.modelo;

// Representa a un animal rescatado en el refugio.
// Guarda datos personales, estados clinico y de adopcion, su ubicacion
// en la matriz y si fue eliminado logicamente.
public class Animal {

    private String codigo;         // identificador unico, ej. A001
    private String nombre;
    private String especie;        // "Perro" | "Gato"
    private String raza;
    private String sexo;           // "Macho" | "Hembra"
    private int edadEstimada;      // en meses
    private String estadoClinico;  // "Sano" | "En tratamiento" | "Crítico"
    private String estadoAdopcion; // "Disponible" | "En proceso" | "Adoptado" | "No apto"
    private boolean eliminadoLogico; // true cuando se elimina sin borrar del arreglo
    private int area;              // fila en la matriz, -1 si no asignado
    private int jaula;             // columna en la matriz, -1 si no asignado

    public Animal(String codigo, String nombre, String especie, String raza,
                  String sexo, int edadEstimada, String estadoClinico,
                  String estadoAdopcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edadEstimada = edadEstimada;
        this.estadoClinico = estadoClinico;
        this.estadoAdopcion = estadoAdopcion;
        this.eliminadoLogico = false;  // nace activo
        this.area = -1;                // sin ubicacion asignada
        this.jaula = -1;
    }

    // Getters
    public String getCodigo()          { return codigo; }
    public String getNombre()          { return nombre; }
    public String getEspecie()         { return especie; }
    public String getRaza()            { return raza; }
    public String getSexo()            { return sexo; }
    public int getEdadEstimada()       { return edadEstimada; }
    public String getEstadoClinico()   { return estadoClinico; }
    public String getEstadoAdopcion()  { return estadoAdopcion; }
    public boolean isEliminadoLogico() { return eliminadoLogico; }
    public int getArea()               { return area; }
    public int getJaula()              { return jaula; }

    // Setters
    public void setCodigo(String codigo)                 { this.codigo = codigo; }
    public void setNombre(String nombre)                 { this.nombre = nombre; }
    public void setEspecie(String especie)               { this.especie = especie; }
    public void setRaza(String raza)                     { this.raza = raza; }
    public void setSexo(String sexo)                     { this.sexo = sexo; }
    public void setEdadEstimada(int edadEstimada)        { this.edadEstimada = edadEstimada; }
    public void setEstadoClinico(String estadoClinico)   { this.estadoClinico = estadoClinico; }
    public void setEstadoAdopcion(String estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }
    public void setEliminadoLogico(boolean eliminadoLogico) { this.eliminadoLogico = eliminadoLogico; }
    public void setArea(int area)                        { this.area = area; }
    public void setJaula(int jaula)                      { this.jaula = jaula; }

    // Linea con los 11 campos separados por "|" para guardar en animales.csv
    public String toArchivo() {
        return codigo + "|" + nombre + "|" + especie + "|" + raza + "|" + sexo + "|"
                + edadEstimada + "|" + estadoClinico + "|" + estadoAdopcion + "|"
                + eliminadoLogico + "|" + area + "|" + jaula;
    }

    // Se usa para mostrar el animal de forma legible
    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + especie + ")";
    }
}