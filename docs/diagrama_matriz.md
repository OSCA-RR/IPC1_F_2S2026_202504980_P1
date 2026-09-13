flowchart LR
    subgraph Matriz [Matriz de Espacios 5x10]
        direction TB
        A0[Area 0: A001, LIBRE, LIBRE, ..., LIBRE]
        A1[Area 1: LIBRE, LIBRE, A002, ..., LIBRE]
        A2[Area 2: LIBRE, LIBRE, LIBRE, ..., LIBRE]
        A3[Area 3: LIBRE, LIBRE, LIBRE, ..., LIBRE]
        A4[Area 4: LIBRE, LIBRE, LIBRE, ..., LIBRE]
    end

    subgraph Operaciones [Operaciones sobre la matriz]
        O1[Asignar animal a celda libre]
        O2[Liberar celda ocupada]
        O3[Consultar disponibilidad]
        O4[Contar espacios libres y ocupados]
        O5[Calcular porcentaje de ocupacion]
    end

    O1 --> Matriz
    O2 --> Matriz
    O3 --> Matriz
    O4 --> Matriz
    O5 --> Matriz

    Matriz --> R1[Actualizar Vista]
    Matriz --> R2[Guardar en espacios.csv]
