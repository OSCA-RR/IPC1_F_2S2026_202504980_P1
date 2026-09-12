package ipc1.refugio.modelo;

public class Adoptante {

    private String codigo;
    private String nombre;
    private String dpi;
    private String telefono;
    private String direccion;
    private String correo;
    private boolean activo;

    public Adoptante(String codigo, String nombre, String dpi,
                     String telefono, String direccion, String correo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dpi = dpi;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.activo = true;
    }

    public String getCodigo()      { return codigo; }
    public String getNombre()      { return nombre; }
    public String getDpi()         { return dpi; }
    public String getTelefono()    { return telefono; }
    public String getDireccion()   { return direccion; }
    public String getCorreo()      { return correo; }
    public boolean isActivo()      { return activo; }

    public void setCodigo(String codigo)       { this.codigo = codigo; }
    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setDpi(String dpi)             { this.dpi = dpi; }
    public void setTelefono(String telefono)   { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setCorreo(String correo)       { this.correo = correo; }
    public void setActivo(boolean activo)      { this.activo = activo; }

    public String toArchivo() {
        return codigo + "|" + nombre + "|" + dpi + "|" + telefono + "|" + direccion + "|" + correo + "|" + activo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}