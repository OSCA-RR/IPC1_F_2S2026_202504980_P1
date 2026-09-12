package ipc1.refugio.modelo;

public class Rescate {

    private String codigo;
    private String ubicacion;
    private String descripcion;
    private String prioridad;   // "Alta" | "Media" | "Baja"
    private String estado;      // "Activo" | "Atendido"
    private String fecha;

    public Rescate(String codigo, String ubicacion, String descripcion,
                   String prioridad, String estado, String fecha) {
        this.codigo = codigo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getCodigo()       { return codigo; }
    public String getUbicacion()    { return ubicacion; }
    public String getDescripcion()  { return descripcion; }
    public String getPrioridad()    { return prioridad; }
    public String getEstado()       { return estado; }
    public String getFecha()        { return fecha; }

    public void setCodigo(String codigo)            { this.codigo = codigo; }
    public void setUbicacion(String ubicacion)      { this.ubicacion = ubicacion; }
    public void setDescripcion(String descripcion)  { this.descripcion = descripcion; }
    public void setPrioridad(String prioridad)      { this.prioridad = prioridad; }
    public void setEstado(String estado)            { this.estado = estado; }
    public void setFecha(String fecha)              { this.fecha = fecha; }

    public String toArchivo() {
        return codigo + "|" + ubicacion + "|" + descripcion + "|" + prioridad + "|" + estado + "|" + fecha;
    }

    @Override
    public String toString() {
        return codigo + " [" + prioridad + "] - " + estado;
    }
}
