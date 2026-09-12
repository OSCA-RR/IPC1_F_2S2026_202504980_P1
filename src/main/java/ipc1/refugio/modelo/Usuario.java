package ipc1.refugio.modelo;

public class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String rol;       // "ADMIN" o "AUXILIAR"
    private boolean activo;

    public Usuario(String nombreUsuario, String contrasena, String rol, boolean activo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena()    { return contrasena; }
    public String getRol()           { return rol; }
    public boolean isActivo()        { return activo; }

    // Setters
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setContrasena(String contrasena)       { this.contrasena = contrasena; }
    public void setRol(String rol)                     { this.rol = rol; }
    public void setActivo(boolean activo)              { this.activo = activo; }

    // Útil para guardar en archivo con separador '|'
    public String toArchivo() {
        return nombreUsuario + "|" + contrasena + "|" + rol + "|" + activo;
    }

    @Override
    public String toString() {
        return nombreUsuario + " (" + rol + ")";
    }
}
