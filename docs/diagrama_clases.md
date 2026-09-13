classDiagram
    class SistemaRefugio {
        -Usuario[] usuarios
        -Animal[] animales
        -Adoptante[] adoptantes
        -Solicitud[] solicitudes
        -Rescate[] rescates
        -Bitacora[] bitacora
        -EspacioRefugio[][] espacios
        +agregarAnimal(Animal) boolean
        +buscarAnimalPorCodigo(String) Animal
        +asignarEspacio(int, int, String) boolean
        +validarLogin(String, String) Usuario
        +registrarBitacora(String, String) void
    }

    class Animal {
        -String codigo
        -String nombre
        -String especie
        -String estadoClinico
        -String estadoAdopcion
        -boolean eliminadoLogico
        -int area
        -int jaula
    }

    class Adoptante {
        -String codigo
        -String nombre
        -String dpi
        -boolean activo
    }

    class Solicitud {
        -String codigo
        -String codigoAnimal
        -String codigoAdoptante
        -String estado
    }

    class Rescate {
        -String codigo
        -String prioridad
        -String estado
    }

    class EspacioRefugio {
        -int area
        -int jaula
        -String codigoAnimal
    }

    class Usuario {
        -String nombreUsuario
        -String rol
        -boolean activo
    }

    class Bitacora {
        -String fechaHora
        -String usuario
        -String accion
    }

    SistemaRefugio --> Animal
    SistemaRefugio --> Adoptante
    SistemaRefugio --> Solicitud
    SistemaRefugio --> Rescate
    SistemaRefugio --> EspacioRefugio
    SistemaRefugio --> Usuario
    SistemaRefugio --> Bitacora
