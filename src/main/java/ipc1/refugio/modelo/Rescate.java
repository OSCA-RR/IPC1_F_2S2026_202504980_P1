package ipc1.refugio.modelo;

// Representa un caso de rescate urgente reportado.
// Nace siempre en estado "Activo" y puede pasar a "Atendido".
public class Rescate {

    private String codigo;
    private String ubicacion;      // lugar donde se reporto el rescate
    private String descripcion;    // detalle del caso
    private String prioridad;      // "Alta" | "Media" | "Baja"
    private String estado;         // "Activo" | "Atendido"
    private String fecha;          // fecha y hora del reporte

    public Rescate(String codigo, String ubicacion, String descripcion,
                   String prioridad, String estado, String fecha) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
    }

    // Getters
    public String getCodigo()       { return codigo; }
    public String getUbicacion()    { return ubicacion; }
    public String getDescripcion()  { return descripcion; }
    public String getPrioridad()    { return prioridad; }
    public String getEstado()       { return estado; }
    public String getFecha()        { return fecha; }

    // Setters
    public void setCodigo(String codigo)            { this.codigo = codigo; }
    public void setUbicacion(String ubicacion)      { this.ubicacion = ubicacion; }
    public void setDescripcion(String descripcion)  { this.descripcion = descripcion; }
    public void setPrioridad(String prioridad)      { this.prioridad = prioridad; }
    public void setEstado(String estado)            { this.estado = estado; }
    public void setFecha(String fecha)              { this.fecha = fecha; }

    // Linea con los datos separados por "|" para guardar en rescates.csv
    public String toArchivo() {
        return codigo + "|" + ubicacion + "|" + descripcion + "|" + prioridad + "|" + estado + "|" + fecha;
    }

    // Se usa para mostrar el rescate de forma legible
    @Override
    public String toString() {
        return codigo + " [" + prioridad + "] - " + estado;
    }
}