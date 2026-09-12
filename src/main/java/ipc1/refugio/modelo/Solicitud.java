package ipc1.refugio.modelo;

public class Solicitud {

    private String codigo;
    private String codigoAnimal;
    private String codigoAdoptante;
    private String fecha;
    private String estado;      // "Pendiente" | "Aprobada" | "Rechazada" | "Cancelada"
    private String observaciones;

    public Solicitud(String codigo, String codigoAnimal, String codigoAdoptante,
                     String fecha, String estado, String observaciones) {
        this.codigo = codigo;
        this.codigoAnimal = codigoAnimal;
        this.codigoAdoptante = codigoAdoptante;
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public String getCodigo()           { return codigo; }
    public String getCodigoAnimal()     { return codigoAnimal; }
    public String getCodigoAdoptante()  { return codigoAdoptante; }
    public String getFecha()            { return fecha; }
    public String getEstado()           { return estado; }
    public String getObservaciones()    { return observaciones; }

    public void setCodigo(String codigo)                  { this.codigo = codigo; }
    public void setCodigoAnimal(String codigoAnimal)      { this.codigoAnimal = codigoAnimal; }
    public void setCodigoAdoptante(String c)              { this.codigoAdoptante = c; }
    public void setFecha(String fecha)                    { this.fecha = fecha; }
    public void setEstado(String estado)                  { this.estado = estado; }
    public void setObservaciones(String observaciones)    { this.observaciones = observaciones; }

    public String toArchivo() {
        return codigo + "|" + codigoAnimal + "|" + codigoAdoptante + "|"
                + fecha + "|" + estado + "|" + observaciones;
    }

    @Override
    public String toString() {
        return codigo + " -> " + estado;
    }
}