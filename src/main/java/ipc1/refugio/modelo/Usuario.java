package ipc1.refugio.modelo;

// Representa a un usuario del sistema (admin o auxiliar).
// Solo guarda datos; la validacion del login esta en SistemaRefugio.
public class Usuario {

    private String nombreUsuario;
    private String contrasena;
    private String rol;        // "ADMIN" o "AUXILIAR"
    private boolean activo;    // si es false, no puede iniciar sesion

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

    // Devuelve los datos en una linea separada por "|" para guardarlos en usuarios.txt
    public String toArchivo() {
        return nombreUsuario + "|" + contrasena + "|" + rol + "|" + activo;
    }

    // Se usa para mostrar el usuario de forma legible
    @Override
    public String toString() {
        return nombreUsuario + " (" + rol + ")";
    }
}