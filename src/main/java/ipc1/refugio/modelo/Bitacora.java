package ipc1.refugio.modelo;

// Representa una entrada de la bitacora del sistema.
// Se guarda una por cada accion importante (login, registro, cambio de estado, etc.).
public class Bitacora {

    private String fechaHora;   // fecha y hora en que ocurrio la accion
    private String usuario;     // quien la hizo
    private String accion;      // tipo de accion (LOGIN, ANIMAL_REGISTRADO, etc.)
    private String detalle;     // descripcion adicional

    public Bitacora(String fechaHora, String usuario, String accion, String detalle) {
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    // Getters
    public String getFechaHora() { return fechaHora; }
    public String getUsuario()   { return usuario; }
    public String getAccion()    { return accion; }
    public String getDetalle()   { return detalle; }

    // Setters
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public void setUsuario(String usuario)     { this.usuario = usuario; }
    public void setAccion(String accion)       { this.accion = accion; }
    public void setDetalle(String detalle)     { this.detalle = detalle; }

    // Linea con los datos separados por "|" para guardar en bitacora.txt
    public String toArchivo() {
        return fechaHora + "|" + usuario + "|" + accion + "|" + detalle;
    }

    // Se usa para mostrar la entrada de forma legible
    @Override
    public String toString() {
        return "[" + fechaHora + "] " + usuario + " -> " + accion;
    }
}