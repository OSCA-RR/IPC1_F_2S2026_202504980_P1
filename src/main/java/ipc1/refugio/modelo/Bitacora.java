package ipc1.refugio.modelo;

public class Bitacora {

    private String fechaHora;
    private String usuario;
    private String accion;
    private String detalle;

    public Bitacora(String fechaHora, String usuario, String accion, String detalle) {
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    public String getFechaHora() { return fechaHora; }
    public String getUsuario()   { return usuario; }
    public String getAccion()    { return accion; }
    public String getDetalle()   { return detalle; }

    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public void setUsuario(String usuario)     { this.usuario = usuario; }
    public void setAccion(String accion)       { this.accion = accion; }
    public void setDetalle(String detalle)     { this.detalle = detalle; }

    public String toArchivo() {
        return fechaHora + "|" + usuario + "|" + accion + "|" + detalle;
    }

    @Override
    public String toString() {
        return "[" + fechaHora + "] " + usuario + " -> " + accion;
    }
}