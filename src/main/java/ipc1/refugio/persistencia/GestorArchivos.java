package ipc1.refugio.persistencia;

import ipc1.refugio.modelo.*;
import ipc1.refugio.servicios.SistemaRefugio;
import java.io.*;

public class GestorArchivos {

    // ============================================================
    // CONSTANTES
    // ============================================================
    private static final String CARPETA = "datos";

    private static final String ARCH_USUARIOS    = CARPETA + "/usuarios.txt";
    private static final String ARCH_ANIMALES    = CARPETA + "/animales.csv";
    private static final String ARCH_ADOPTANTES  = CARPETA + "/adoptantes.csv";
    private static final String ARCH_SOLICITUDES = CARPETA + "/solicitudes.csv";
    private static final String ARCH_RESCATES    = CARPETA + "/rescates.csv";
    private static final String ARCH_BITACORA    = CARPETA + "/bitacora.txt";
    private static final String ARCH_ESPACIOS    = CARPETA + "/espacios.csv";

    private static final String SEPARADOR = "\\|";

    // ============================================================
    // CREAR CARPETA SI NO EXISTE
    // ============================================================
    private static void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    // ============================================================
    // GUARDAR TODO
    // ============================================================
    public static void guardarTodo(SistemaRefugio sistema) {
        crearCarpetaSiNoExiste();
        guardarUsuarios(sistema);
        guardarAnimales(sistema);
        guardarAdoptantes(sistema);
        guardarSolicitudes(sistema);
        guardarRescates(sistema);
        guardarBitacora(sistema);
        guardarEspacios(sistema);
    }

    // ============================================================
    // CARGAR TODO
    // ============================================================
    public static void cargarTodo(SistemaRefugio sistema) {
        crearCarpetaSiNoExiste();
        cargarUsuarios(sistema);
        cargarAnimales(sistema);
        cargarAdoptantes(sistema);
        cargarSolicitudes(sistema);
        cargarRescates(sistema);
        cargarBitacora(sistema);
        cargarEspacios(sistema);
    }

    // ============================================================
    // USUARIOS
    // ============================================================
    private static void guardarUsuarios(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_USUARIOS))) {
            for (int i = 0; i < s.getContadorUsuarios(); i++) {
                Usuario u = s.getUsuarios()[i];
                if (u != null) pw.println(u.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    private static void cargarUsuarios(SistemaRefugio s) {
        File f = new File(ARCH_USUARIOS);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 4) {
                    Usuario u = new Usuario(p[0], p[1], p[2], Boolean.parseBoolean(p[3]));
                    s.agregarUsuario(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    // ============================================================
    // ANIMALES
    // ============================================================
    private static void guardarAnimales(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_ANIMALES))) {
            for (int i = 0; i < s.getContadorAnimales(); i++) {
                Animal a = s.getAnimales()[i];
                if (a != null) pw.println(a.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar animales: " + e.getMessage());
        }
    }

    private static void cargarAnimales(SistemaRefugio s) {
        File f = new File(ARCH_ANIMALES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 11) {
                    Animal a = new Animal(p[0], p[1], p[2], p[3], p[4],
                            Integer.parseInt(p[5]), p[6], p[7]);
                    a.setEliminadoLogico(Boolean.parseBoolean(p[8]));
                    a.setArea(Integer.parseInt(p[9]));
                    a.setJaula(Integer.parseInt(p[10]));
                    s.agregarAnimal(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar animales: " + e.getMessage());
        }
    }

    // ============================================================
    // ADOPTANTES
    // ============================================================
    private static void guardarAdoptantes(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_ADOPTANTES))) {
            for (int i = 0; i < s.getContadorAdoptantes(); i++) {
                Adoptante a = s.getAdoptantes()[i];
                if (a != null) pw.println(a.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar adoptantes: " + e.getMessage());
        }
    }

    private static void cargarAdoptantes(SistemaRefugio s) {
        File f = new File(ARCH_ADOPTANTES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 7) {
                    Adoptante a = new Adoptante(p[0], p[1], p[2], p[3], p[4], p[5]);
                    a.setActivo(Boolean.parseBoolean(p[6]));
                    s.agregarAdoptante(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar adoptantes: " + e.getMessage());
        }
    }

    // ============================================================
    // SOLICITUDES
    // ============================================================
    private static void guardarSolicitudes(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_SOLICITUDES))) {
            for (int i = 0; i < s.getContadorSolicitudes(); i++) {
                Solicitud so = s.getTodasLasSolicitudes()[i];
                if (so != null) pw.println(so.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar solicitudes: " + e.getMessage());
        }
    }

    private static void cargarSolicitudes(SistemaRefugio s) {
        File f = new File(ARCH_SOLICITUDES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 6) {
                    Solicitud so = new Solicitud(p[0], p[1], p[2], p[3], p[4], p[5]);
                    s.agregarSolicitud(so);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar solicitudes: " + e.getMessage());
        }
    }

    // ============================================================
    // RESCATES
    // ============================================================
    private static void guardarRescates(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_RESCATES))) {
            for (int i = 0; i < s.getContadorRescates(); i++) {
                Rescate r = s.getTodosLosRescates()[i];
                if (r != null) pw.println(r.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar rescates: " + e.getMessage());
        }
    }

    private static void cargarRescates(SistemaRefugio s) {
        File f = new File(ARCH_RESCATES);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 6) {
                    Rescate r = new Rescate(p[0], p[1], p[2], p[3], p[4], p[5]);
                    s.agregarRescate(r);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar rescates: " + e.getMessage());
        }
    }

    // ============================================================
    // BITÁCORA
    // ============================================================
    private static void guardarBitacora(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_BITACORA))) {
            for (int i = 0; i < s.getContadorBitacora(); i++) {
                Bitacora b = s.getBitacora()[i];
                if (b != null) pw.println(b.toArchivo());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar bitácora: " + e.getMessage());
        }
    }

    private static void cargarBitacora(SistemaRefugio s) {
        // La bitácora normalmente no se recarga al inicio, pero la dejamos por completitud
        File f = new File(ARCH_BITACORA);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                // Solo contamos las líneas para referencia, no las volvemos a registrar
                // para no duplicar la bitácora en cada arranque
            }
        } catch (IOException e) {
            System.out.println("Error al leer bitácora: " + e.getMessage());
        }
    }

    // ============================================================
    // ESPACIOS (MATRIZ)
    // ============================================================
    private static void guardarEspacios(SistemaRefugio s) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCH_ESPACIOS))) {
            EspacioRefugio[][] m = s.getEspacios();
            for (int i = 0; i < SistemaRefugio.AREAS; i++) {
                for (int j = 0; j < SistemaRefugio.JAULAS; j++) {
                    String codigo = m[i][j].getCodigoAnimal();
                    if (codigo == null) codigo = "LIBRE";
                    pw.println(i + "|" + j + "|" + codigo);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar espacios: " + e.getMessage());
        }
    }

    private static void cargarEspacios(SistemaRefugio s) {
        File f = new File(ARCH_ESPACIOS);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(SEPARADOR);
                if (p.length >= 3) {
                    int area = Integer.parseInt(p[0]);
                    int jaula = Integer.parseInt(p[1]);
                    String codigo = p[2];
                    if (!codigo.equals("LIBRE")) {
                        s.asignarEspacio(area, jaula, codigo);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar espacios: " + e.getMessage());
        }
    }
}