package ipc1.refugio.modelo;

// Representa una solicitud de adopcion. Vincula un animal con un adoptante.
// El cambio de estado y el efecto sobre el animal los maneja SistemaRefugio.
public class Solicitud {

    private String codigo;
    private String codigoAnimal;      // codigo del animal solicitado
    private String codigoAdoptante;   // codigo del adoptante que solicita
    private String fecha;             // fecha y hora en que se creo
    private String estado;            // "Pendiente" | "Aprobada" | "Rechazada" | "Cancelada"
    private String observaciones;     // comentarios opcionales

    public Solicitud(String codigo, String codigoAnimal, String codigoAdoptante,
                     String fecha, String estado, String observaciones) {
        this.codigo = codigo;
        this.codigoAnimal = codigoAnimal;
        this.codigoAdoptante = codigoAdoptante;
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters
    public String getCodigo()           { return codigo; }
    public String getCodigoAnimal()     { return codigoAnimal; }
    public String getCodigoAdoptante()  { return codigoAdoptante; }
    public String getFecha()            { return fecha; }
    public String getEstado()           { return estado; }
    public String getObservaciones()    { return observaciones; }

    // Setters
    public void setCodigo(String codigo)                  { this.codigo = codigo; }
    public void setCodigoAnimal(String codigoAnimal)      { this.codigoAnimal = codigoAnimal; }
    public void setCodigoAdoptante(String c)              { this.codigoAdoptante = c; }
    public void setFecha(String fecha)                    { this.fecha = fecha; }
    public void setEstado(String estado)                  { this.estado = estado; }
    public void setObservaciones(String observaciones)    { this.observaciones = observaciones; }

    // Linea con los datos separados por "|" para guardar en solicitudes.csv
    public String toArchivo() {
        return codigo + "|" + codigoAnimal + "|" + codigoAdoptante + "|"
                + fecha + "|" + estado + "|" + observaciones;
    }

    // Se usa para mostrar la solicitud de forma legible
    @Override
    public String toString() {
        return codigo + " -> " + estado;
    }
}