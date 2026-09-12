package ipc1.refugio.modelo;

public class Animal {

    private String codigo;
    private String nombre;
    private String especie;        // "Perro" | "Gato"
    private String raza;
    private String sexo;           // "Macho" | "Hembra"
    private int edadEstimada;      // meses
    private String estadoClinico;  // "Sano" | "En tratamiento" | "Crítico"
    private String estadoAdopcion; // "Disponible" | "En proceso" | "Adoptado" | "No apto"
    private boolean eliminadoLogico;
    private int area;              // fila en matriz, -1 si no asignado
    private int jaula;             // columna en matriz, -1 si no asignado

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
        this.eliminadoLogico = false;
        this.area = -1;
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

    public String toArchivo() {
        return codigo + "|" + nombre + "|" + especie + "|" + raza + "|" + sexo + "|"
                + edadEstimada + "|" + estadoClinico + "|" + estadoAdopcion + "|"
                + eliminadoLogico + "|" + area + "|" + jaula;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + especie + ")";
    }
}