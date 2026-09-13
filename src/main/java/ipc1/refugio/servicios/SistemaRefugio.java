package ipc1.refugio.servicios;

import ipc1.refugio.modelo.*;

public class SistemaRefugio {

    // ============================================================
    // 1. CONSTANTES DE TAMAÑO
    // ============================================================
    public static final int MAX_USUARIOS     = 10;
    public static final int MAX_ANIMALES     = 100;
    public static final int MAX_ADOPTANTES   = 50;
    public static final int MAX_SOLICITUDES  = 100;
    public static final int MAX_RESCATES     = 50;
    public static final int MAX_BITACORA     = 500;

    public static final int AREAS            = 5;   // filas de la matriz
    public static final int JAULAS           = 10;  // columnas de la matriz

    // ============================================================
    // 2. ATRIBUTOS: ARREGLOS ESTÁTICOS Y MATRIZ
    // ============================================================
    private Usuario[]    usuarios;
    private Animal[]     animales;
    private Adoptante[]  adoptantes;
    private Solicitud[]  solicitudes;
    private Rescate[]    rescates;
    private Bitacora[]   bitacora;

    private EspacioRefugio[][] espacios; // matriz [AREAS][JAULAS]

    // Contadores de cuántos registros hay realmente en cada arreglo
    private int contadorUsuarios;
    private int contadorAnimales;
    private int contadorAdoptantes;
    private int contadorSolicitudes;
    private int contadorRescates;
    private int contadorBitacora;

    // Usuario que tiene la sesión activa
    private Usuario usuarioActual;

    // ============================================================
    // 3. CONSTRUCTOR
    // ============================================================
    public SistemaRefugio() {
        usuarios    = new Usuario[MAX_USUARIOS];
        animales    = new Animal[MAX_ANIMALES];
        adoptantes  = new Adoptante[MAX_ADOPTANTES];
        solicitudes = new Solicitud[MAX_SOLICITUDES];
        rescates    = new Rescate[MAX_RESCATES];
        bitacora    = new Bitacora[MAX_BITACORA];

        contadorUsuarios    = 0;
        contadorAnimales    = 0;
        contadorAdoptantes  = 0;
        contadorSolicitudes = 0;
        contadorRescates    = 0;
        contadorBitacora    = 0;

        // Inicializar la matriz de espacios
        espacios = new EspacioRefugio[AREAS][JAULAS];
        for (int i = 0; i < AREAS; i++) {
            for (int j = 0; j < JAULAS; j++) {
                // capacidad máxima por jaula (puedes variarla después)
                espacios[i][j] = new EspacioRefugio(i, j, 2);
            }
        }
    }
    
    // ============================================================
    // 4. USUARIOS
    // ============================================================
    public void agregarUsuario(Usuario u) {
        if (contadorUsuarios < MAX_USUARIOS) {
            usuarios[contadorUsuarios] = u;
            contadorUsuarios++;
        }
    }

    public Usuario[] getUsuarios() {
        return usuarios;
    }

    public int getContadorUsuarios() {
        return contadorUsuarios;
    }

    public Usuario validarLogin(String nombreUsuario, String contrasena) {
        for (int i = 0; i < contadorUsuarios; i++) {
            Usuario u = usuarios[i];
            if (u != null && u.isActivo()
                    && u.getNombreUsuario().equals(nombreUsuario)
                    && u.getContrasena().equals(contrasena)) {
                usuarioActual = u;
                return u;
            }
        }
        return null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }
    
    // ============================================================
    // 5. ANIMALES
    // ============================================================
    public boolean existeAnimalConCodigo(String codigo) {
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()
                    && animales[i].getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarAnimal(Animal a) {
        // Primero buscamos si ya existe un animal con ese codigo (activo o eliminado).
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null
                    && animales[i].getCodigo().equalsIgnoreCase(a.getCodigo())) {

                // Si ya existe un animal ACTIVO con ese codigo, no se permite.
                if (!animales[i].isEliminadoLogico()) {
                    return false;
                }

                // Si existe pero estaba eliminado, lo reactivamos con los datos nuevos.
                // Se reutiliza la MISMA posicion del arreglo, no se crea una nueva.
                a.setEliminadoLogico(false);
                a.setArea(-1);
                a.setJaula(-1);
                animales[i] = a;
                return true;
            }
        }

        // Si no existe, lo agregamos al final como antes.
        if (contadorAnimales >= MAX_ANIMALES) return false;
        animales[contadorAnimales] = a;
        contadorAnimales++;
        return true;
    }

    public Animal buscarAnimalPorCodigo(String codigo) {
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()
                    && animales[i].getCodigo().equalsIgnoreCase(codigo)) {
                return animales[i];
            }
        }
        return null;
    }

    // Búsqueda por nombre (parcial, sin distinguir mayúsculas)
    public Animal[] buscarAnimalesPorNombre(String nombre) {
        Animal[] resultado = new Animal[MAX_ANIMALES];
        int k = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()
                    && animales[i].getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado[k] = animales[i];
                k++;
            }
        }
        return resultado;
    }

    public Animal[] buscarAnimalesPorEspecie(String especie) {
        Animal[] resultado = new Animal[MAX_ANIMALES];
        int k = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()
                    && animales[i].getEspecie().equalsIgnoreCase(especie)) {
                resultado[k] = animales[i];
                k++;
            }
        }
        return resultado;
    }

    public Animal[] buscarAnimalesPorEstado(String estadoAdopcion) {
        Animal[] resultado = new Animal[MAX_ANIMALES];
        int k = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()
                    && animales[i].getEstadoAdopcion().equalsIgnoreCase(estadoAdopcion)) {
                resultado[k] = animales[i];
                k++;
            }
        }
        return resultado;
    }

    public Animal[] getAnimalesActivos() {
        Animal[] resultado = new Animal[MAX_ANIMALES];
        int k = 0;
        for (int i = 0; i < contadorAnimales; i++) {
            if (animales[i] != null && !animales[i].isEliminadoLogico()) {
                resultado[k] = animales[i];
                k++;
            }
        }
        return resultado;
    }

    public int getContadorAnimales() {
        return contadorAnimales;
    }

    public Animal[] getAnimales() {
        return animales;
    }

    public boolean editarEstadoAnimal(String codigo, String nuevoEstadoClinico, String nuevoEstadoAdopcion) {
        Animal a = buscarAnimalPorCodigo(codigo);
        if (a == null) return false;
        a.setEstadoClinico(nuevoEstadoClinico);
        a.setEstadoAdopcion(nuevoEstadoAdopcion);
        return true;
    }

    public boolean eliminarAnimalLogico(String codigo) {
        Animal a = buscarAnimalPorCodigo(codigo);
        if (a == null) return false;
        a.setEliminadoLogico(true);

        // Si estaba asignado a un espacio, lo liberamos
        if (a.getArea() >= 0 && a.getJaula() >= 0) {
            liberarEspacio(a.getArea(), a.getJaula());
            a.setArea(-1);
            a.setJaula(-1);
        }
        return true;
    }
    
    // ============================================================
    // 6. ADOPTANTES
    // ============================================================
    public boolean existeAdoptanteConDpi(String dpi) {
        for (int i = 0; i < contadorAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].isActivo()
                    && adoptantes[i].getDpi().equals(dpi)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeAdoptanteConCodigo(String codigo) {
        for (int i = 0; i < contadorAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].isActivo()
                    && adoptantes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarAdoptante(Adoptante a) {
        if (contadorAdoptantes >= MAX_ADOPTANTES) return false;
        if (existeAdoptanteConCodigo(a.getCodigo())) return false;
        if (existeAdoptanteConDpi(a.getDpi())) return false;
        adoptantes[contadorAdoptantes] = a;
        contadorAdoptantes++;
        return true;
    }

    public Adoptante buscarAdoptantePorCodigo(String codigo) {
        for (int i = 0; i < contadorAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].isActivo()
                    && adoptantes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return adoptantes[i];
            }
        }
        return null;
    }

    public Adoptante buscarAdoptantePorDpi(String dpi) {
        for (int i = 0; i < contadorAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].isActivo()
                    && adoptantes[i].getDpi().equals(dpi)) {
                return adoptantes[i];
            }
        }
        return null;
    }

    public Adoptante[] getAdoptantesActivos() {
        Adoptante[] resultado = new Adoptante[MAX_ADOPTANTES];
        int k = 0;
        for (int i = 0; i < contadorAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].isActivo()) {
                resultado[k] = adoptantes[i];
                k++;
            }
        }
        return resultado;
    }

    public int getContadorAdoptantes() {
        return contadorAdoptantes;
    }

    public Adoptante[] getAdoptantes() {
        return adoptantes;
    }

    public boolean editarAdoptante(String codigo, String nombre, String telefono,
                                   String direccion, String correo) {
        Adoptante a = buscarAdoptantePorCodigo(codigo);
        if (a == null) return false;
        a.setNombre(nombre);
        a.setTelefono(telefono);
        a.setDireccion(direccion);
        a.setCorreo(correo);
        return true;
    }
    
    // ============================================================
    // 7. SOLICITUDES
    // ============================================================
    public boolean existeSolicitudConCodigo(String codigo) {
        for (int i = 0; i < contadorSolicitudes; i++) {
            if (solicitudes[i] != null
                    && solicitudes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarSolicitud(Solicitud s) {
        if (contadorSolicitudes >= MAX_SOLICITUDES) return false;
        if (existeSolicitudConCodigo(s.getCodigo())) return false;
        solicitudes[contadorSolicitudes] = s;
        contadorSolicitudes++;
        return true;
    }

    public Solicitud buscarSolicitudPorCodigo(String codigo) {
        for (int i = 0; i < contadorSolicitudes; i++) {
            if (solicitudes[i] != null
                    && solicitudes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return solicitudes[i];
            }
        }
        return null;
    }

    public boolean cambiarEstadoSolicitud(String codigo, String nuevoEstado) {
        Solicitud s = buscarSolicitudPorCodigo(codigo);
        if (s == null) return false;
        s.setEstado(nuevoEstado);

        // Si la solicitud se aprueba, cambiamos el animal a "Adoptado"
        if (nuevoEstado.equalsIgnoreCase("Aprobada")) {
            Animal a = buscarAnimalPorCodigo(s.getCodigoAnimal());
            if (a != null) {
                a.setEstadoAdopcion("Adoptado");
            }
        }
        return true;
    }

    public Solicitud[] getSolicitudesPendientes() {
        Solicitud[] resultado = new Solicitud[MAX_SOLICITUDES];
        int k = 0;
        for (int i = 0; i < contadorSolicitudes; i++) {
            if (solicitudes[i] != null
                    && solicitudes[i].getEstado().equalsIgnoreCase("Pendiente")) {
                resultado[k] = solicitudes[i];
                k++;
            }
        }
        return resultado;
    }

    public Solicitud[] getSolicitudesPorAnimal(String codigoAnimal) {
        Solicitud[] resultado = new Solicitud[MAX_SOLICITUDES];
        int k = 0;
        for (int i = 0; i < contadorSolicitudes; i++) {
            if (solicitudes[i] != null
                    && solicitudes[i].getCodigoAnimal().equalsIgnoreCase(codigoAnimal)) {
                resultado[k] = solicitudes[i];
                k++;
            }
        }
        return resultado;
    }

    public Solicitud[] getTodasLasSolicitudes() {
        return solicitudes;
    }

    public int getContadorSolicitudes() {
        return contadorSolicitudes;
    }
    
    // ============================================================
    // 8. RESCATES
    // ============================================================
    public boolean existeRescateConCodigo(String codigo) {
        for (int i = 0; i < contadorRescates; i++) {
            if (rescates[i] != null
                    && rescates[i].getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    public boolean agregarRescate(Rescate r) {
        if (contadorRescates >= MAX_RESCATES) return false;
        if (existeRescateConCodigo(r.getCodigo())) return false;
        rescates[contadorRescates] = r;
        contadorRescates++;
        return true;
    }

    public Rescate buscarRescatePorCodigo(String codigo) {
        for (int i = 0; i < contadorRescates; i++) {
            if (rescates[i] != null
                    && rescates[i].getCodigo().equalsIgnoreCase(codigo)) {
                return rescates[i];
            }
        }
        return null;
    }

    public boolean atenderRescate(String codigo) {
        Rescate r = buscarRescatePorCodigo(codigo);
        if (r == null) return false;
        r.setEstado("Atendido");
        return true;
    }

    public Rescate[] getRescatesActivos() {
        Rescate[] resultado = new Rescate[MAX_RESCATES];
        int k = 0;
        for (int i = 0; i < contadorRescates; i++) {
            if (rescates[i] != null
                    && rescates[i].getEstado().equalsIgnoreCase("Activo")) {
                resultado[k] = rescates[i];
                k++;
            }
        }
        return resultado;
    }

    public Rescate[] getRescatesPorPrioridad(String prioridad) {
        Rescate[] resultado = new Rescate[MAX_RESCATES];
        int k = 0;
        for (int i = 0; i < contadorRescates; i++) {
            if (rescates[i] != null
                    && rescates[i].getPrioridad().equalsIgnoreCase(prioridad)) {
                resultado[k] = rescates[i];
                k++;
            }
        }
        return resultado;
    }

    public Rescate[] getTodosLosRescates() {
        return rescates;
    }

    public int getContadorRescates() {
        return contadorRescates;
    }
    
    // ============================================================
    // 9. ESPACIOS (MATRIZ)
    // ============================================================
    public EspacioRefugio[][] getEspacios() {
        return espacios;
    }

    public EspacioRefugio getEspacio(int area, int jaula) {
        if (area < 0 || area >= AREAS || jaula < 0 || jaula >= JAULAS) return null;
        return espacios[area][jaula];
    }
    
     // Asigna un animal a una celda libre. Valida que la celda este libre,
    // que el animal exista y que no este ya asignado en otro espacio.
    public boolean asignarEspacio(int area, int jaula, String codigoAnimal) {
        if (area < 0 || area >= AREAS || jaula < 0 || jaula >= JAULAS) return false;

        EspacioRefugio e = espacios[area][jaula];
        if (!e.estaLibre()) return false; // ya ocupado

        Animal a = buscarAnimalPorCodigo(codigoAnimal);
        if (a == null) return false; // el animal no existe

        if (a.getArea() >= 0 && a.getJaula() >= 0) return false; // ya está asignado

        e.setCodigoAnimal(codigoAnimal);
        a.setArea(area);
        a.setJaula(jaula);
        return true;
    }
     // Libera una celda ocupada y resetea la ubicacion del animal.
    public boolean liberarEspacio(int area, int jaula) {
        if (area < 0 || area >= AREAS || jaula < 0 || jaula >= JAULAS) return false;

        EspacioRefugio e = espacios[area][jaula];
        if (e.estaLibre()) return false;

        String codigo = e.getCodigoAnimal();
        Animal a = buscarAnimalPorCodigo(codigo);
        if (a != null) {
            a.setArea(-1);
            a.setJaula(-1);
        }
        e.setCodigoAnimal(null);
        return true;
    }

    public int contarEspaciosLibres() {
        int libres = 0;
        for (int i = 0; i < AREAS; i++) {
            for (int j = 0; j < JAULAS; j++) {
                if (espacios[i][j].estaLibre()) libres++;
            }
        }
        return libres;
    }

    public int contarEspaciosOcupados() {
        return (AREAS * JAULAS) - contarEspaciosLibres();
    }

    public double porcentajeOcupacion() {
        int total = AREAS * JAULAS;
        if (total == 0) return 0.0;
        return (contarEspaciosOcupados() * 100.0) / total;
    }
    
    // ============================================================
    // 10. BITÁCORA
    // ============================================================
    public void registrarBitacora(String accion, String detalle) {
        if (contadorBitacora >= MAX_BITACORA) return;
        String usuario = (usuarioActual != null) ? usuarioActual.getNombreUsuario() : "SISTEMA";
        String fechaHora = ipc1.refugio.utilidades.FechaUtil.ahora();
        bitacora[contadorBitacora] = new Bitacora(fechaHora, usuario, accion, detalle);
        contadorBitacora++;
    }

    public Bitacora[] getBitacora() {
        return bitacora;
    }

    public int getContadorBitacora() {
        return contadorBitacora;
    }
}