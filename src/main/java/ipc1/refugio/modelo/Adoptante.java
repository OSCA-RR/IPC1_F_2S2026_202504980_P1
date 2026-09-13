package ipc1.refugio.modelo;

// Representa a una persona interesada en adoptar un animal.
// El campo "activo" funciona como eliminacion logica:
// si es false, el adoptante ya no aparece en los listados.
public class Adoptante {

    private String codigo;      // identificador unico, ej. AD001
    private String nombre;      // nombre completo
    private String dpi;         // 13 digitos, unico
    private String telefono;
    private String direccion;
    private String correo;
    private boolean activo;     // true = habilitado, false = desactivado

    public Adoptante(String codigo, String nombre, String dpi,
                     String telefono, String direccion, String correo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dpi = dpi;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.activo = true;     // nace activo
    }

    // Getters
    public String getCodigo()      { return codigo; }
    public String getNombre()      { return nombre; }
    public String getDpi()         { return dpi; }
    public String getTelefono()    { return telefono; }
    public String getDireccion()   { return direccion; }
    public String getCorreo()      { return correo; }
    public boolean isActivo()      { return activo; }

    // Setters
    public void setCodigo(String codigo)       { this.codigo = codigo; }
    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setDpi(String dpi)             { this.dpi = dpi; }
    public void setTelefono(String telefono)   { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setCorreo(String correo)       { this.correo = correo; }
    public void setActivo(boolean activo)      { this.activo = activo; }

    // Linea con los 7 campos separados por "|" para guardar en adoptantes.csv
    public String toArchivo() {
        return codigo + "|" + nombre + "|" + dpi + "|" + telefono + "|" + direccion + "|" + correo + "|" + activo;
    }

    // Se usa para mostrar el adoptante de forma legible
    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}