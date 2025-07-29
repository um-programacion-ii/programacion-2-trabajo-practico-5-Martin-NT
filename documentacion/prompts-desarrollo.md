# Prompts de Desarrollo

## Prompt 1: Análisis de entidad Empleado y conceptos fundamentales de JPA

### Prompt Utilizado:
```
Tengo esta entidad Empleado:

package um.example.TP5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "fecha_contratacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaContratacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToMany
    @JoinTable(
        name = "empleado_proyecto",
        joinColumns = @JoinColumn(name = "empleado_id"),
        inverseJoinColumns = @JoinColumn(name = "proyecto_id")
    )
    private Set<Proyecto> proyectos = new HashSet<>();
}

Como experto en JPA, necesito que:

1. Analices mi código: qué está bien y qué mejorar
2. Expliques cada anotación: para qué sirve y por qué es necesaria  
3. Me expliques JPA y los temas relacionados: conceptos fundamentales, diferencias con Hibernate, anotaciones principales
4. Me expliques cómo se hacen y funcionan las relaciones:
   - Empleado ↔ Departamento (ManyToOne/OneToMany)
   - Empleado ↔ Proyecto (ManyToMany)
   - Owning side vs inverse side, mappedBy
5. Mejores prácticas: nomenclatura, fetch types, cascade types, Set vs List
```

### Respuesta Recibida:
```
Perfecto, vamos paso a paso para que tengas un análisis completo y claro de tu entidad Empleado y los conceptos de JPA relacionados.

1. Análisis de tu código: qué está bien y qué mejorar
Lo que está bien:
Uso correcto de las anotaciones básicas JPA (@Entity, @Table, @Id, @GeneratedValue, @Column).

Definición de campos con restricciones (nullable, unique, length).

Uso correcto de tipos para fechas (LocalDate con @Temporal).

Relaciones definidas (@ManyToOne y @ManyToMany).

Uso de Set para la relación ManyToMany, que es recomendable para evitar duplicados.

Uso de Lombok para evitar código boilerplate (@Data, @NoArgsConstructor, @AllArgsConstructor).

Lo que podrías mejorar:
@Temporal con LocalDate no es necesario ni válido. @Temporal solo aplica a tipos java.util.Date o java.util.Calendar. Para java.time.LocalDate, JPA 2.2+ lo mapea automáticamente sin @Temporal.

Para las relaciones, sería bueno agregar el atributo cascade si quieres que ciertas operaciones se propaguen (depende del caso de uso).

El fetch = FetchType.LAZY en @ManyToOne está bien, pero usualmente es EAGER por defecto en ManyToOne, así que si querés que sea lazy está perfecto; solo tené en cuenta el comportamiento.

Podrías agregar equals y hashCode personalizados (pero Lombok @Data los genera, ojo que puede haber problemas con entidades JPA y colecciones).

Por convención, se suele poner las clases relacionadas en el mismo paquete o en subpaquetes cercanos (entity o model).

Falta código para las entidades Departamento y Proyecto para ver si las relaciones están bien definidas desde ambos lados.

2. Explicación de cada anotación en tu código
| Anotación                                             | Descripción                                                                                                                                              |
| ----------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `@Entity`                                             | Marca la clase como una entidad JPA que será mapeada a una tabla de base de datos.                                                                       |
| `@Table(name = "empleados")`                          | Especifica el nombre de la tabla en la base de datos. Si no se pone, JPA usa el nombre de la clase por defecto.                                          |
| `@Id`                                                 | Indica que el atributo es la clave primaria de la tabla.                                                                                                 |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Define que el valor del ID se genera automáticamente por la base (auto-incremental). `IDENTITY` es la estrategia más común para bases SQL tradicionales. |
| `@Column(nullable = false, length = 100)`             | Define detalles del mapeo del campo a la columna (restricciones, tamaño, etc).                                                                           |
| `@Column(unique = true, nullable = false)`            | En este caso para `email`, indica que el valor debe ser único y no nulo en la tabla.                                                                     |
| `@Temporal(TemporalType.DATE)`                        | Sólo para tipos `java.util.Date` o `Calendar`. Indica que solo se almacena la fecha (sin hora). **No es necesaria con `LocalDate`.**                     |
| `@ManyToOne(fetch = FetchType.LAZY)`                  | Relación de muchos a uno con otra entidad. `LAZY` indica que la entidad relacionada no se carga hasta que se accede explícitamente.                      |
| `@JoinColumn(name = "departamento_id")`               | Define la columna de la tabla que actúa como clave foránea hacia `Departamento`.                                                                         |
| `@ManyToMany`                                         | Relación muchos a muchos con otra entidad.                                                                                                               |
| `@JoinTable`                                          | Tabla intermedia que mapea la relación ManyToMany. Define los nombres de la tabla y columnas de unión.                                                   |

3. Explicación general de JPA y conceptos relacionados
JPA (Java Persistence API): Es una especificación estándar de Java para manejar persistencia de datos en bases relacionales mediante mapeo objeto-relacional (ORM).

Hibernate: Es la implementación más popular de JPA, que añade muchas funcionalidades extras y mejoras. Puedes usar JPA con Hibernate o con otras implementaciones.

Entidades: Clases Java anotadas con @Entity que representan tablas en la base.

Entity Manager: Es el componente que maneja el ciclo de vida de las entidades (crear, persistir, borrar, consultar).

Transacciones: JPA maneja transacciones para asegurar que operaciones a la BD sean atómicas.

Anotaciones principales:

@Entity, @Table

@Id, @GeneratedValue

@Column

Relaciones: @OneToOne, @OneToMany, @ManyToOne, @ManyToMany

@JoinColumn, @JoinTable, mappedBy

4. Relaciones y funcionamiento en tu caso
Empleado ↔ Departamento (ManyToOne / OneToMany)
Empleado es ManyToOne respecto a Departamento: Muchos empleados pueden pertenecer a un solo departamento.

En Empleado:

java
Copiar
Editar
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "departamento_id")
private Departamento departamento;
En Departamento (lado inverso):

java
Copiar
Editar
@OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Empleado> empleados = new ArrayList<>();
Owning side: es Empleado porque tiene la columna departamento_id (la clave foránea).

Inverse side: es Departamento que usa mappedBy para indicar que no tiene la FK, solo la referencia.

Empleado ↔ Proyecto (ManyToMany)
Un empleado puede trabajar en muchos proyectos, y un proyecto puede tener muchos empleados.

En Empleado (dueño de la relación):

java
Copiar
Editar
@ManyToMany
@JoinTable(
    name = "empleado_proyecto",
    joinColumns = @JoinColumn(name = "empleado_id"),
    inverseJoinColumns = @JoinColumn(name = "proyecto_id")
)
private Set<Proyecto> proyectos = new HashSet<>();
En Proyecto (lado inverso):

java
Copiar
Editar
@ManyToMany(mappedBy = "proyectos")
private Set<Empleado> empleados = new HashSet<>();
La tabla intermedia empleado_proyecto almacena los pares (empleado_id, proyecto_id).

5. Mejores prácticas y recomendaciones
Nombres de paquetes: usar entity o model es común, mantén coherencia.

fetch:

ManyToOne es por defecto EAGER, pero LAZY ayuda a mejorar rendimiento si la entidad relacionada no siempre se necesita.

OneToMany y ManyToMany son por defecto LAZY.

cascade: Usar con cuidado, usualmente en OneToMany para que al borrar un departamento se borren empleados, o al persistir un proyecto también se persistan empleados (si tiene sentido).

Colecciones:

Set es ideal para relaciones ManyToMany para evitar duplicados.

List para relaciones donde el orden importa o puede haber duplicados.

equals y hashCode:

Para entidades es recomendable implementarlos basados en el identificador (ID) para evitar problemas con colecciones. Lombok puede generar, pero hay que tener cuidado.

@Temporal: No usar con java.time.* (como LocalDate).

Usar Lombok: ayuda a reducir código boilerplate, solo revisa que genere correctamente el código que necesitas.


```

### Modificaciones Realizadas:
- Eliminé la anotación @Temporal del campo fechaContratacion ya que no es necesaria ni válida para LocalDate
- Mantuve FetchType.LAZY en la relación ManyToOne con Departamento para optimizar performance
- Decidí no agregar cascade types por ahora hasta definir mejor el comportamiento deseado del sistema
- Conservé el uso de Set para la relación ManyToMany siguiendo las mejores prácticas

### Explicación del Prompt:
Utilicé este prompt porque necesitaba:
1. **Validación del código existente**: Quería asegurarme de que mi implementación de la entidad Empleado siguiera las mejores prácticas de JPA
2. **Comprensión profunda**: Necesitaba entender no solo cómo funciona cada anotación, sino por qué es necesaria
3. **Fundamentos teóricos**: Era importante comprender JPA como especificación y su relación con Hibernate
4. **Relaciones bidireccionales**: Quería entender claramente cómo implementar las relaciones entre entidades y el concepto de owning side

### Aprendizajes Obtenidos:
- **@Temporal es obsoleto con java.time**: Aprendí que @Temporal solo se usa con java.util.Date/Calendar, no con LocalDate
- **JPA vs Hibernate**: Comprendí que JPA es la especificación estándar y Hibernate es la implementación más popular
- **Owning side en relaciones**: El lado que tiene la foreign key es el "owning side" y controla la relación
- **FetchType por defecto**: ManyToOne es EAGER por defecto, OneToMany y ManyToMany son LAZY
- **Set vs List en ManyToMany**: Set es preferible para evitar duplicados y mejorar performance
- **Cascade types**: Deben usarse cuidadosamente según la lógica de negocio del sistema
- **Nomenclatura**: Convención snake_case para tablas y camelCase para propiedades Java

---

## Prompt 2: Validación y Optimización de Entidad JPA - Departamento

### Prompt Utilizado:
```
Revisa mi entidad Departamento y su relación con Empleado:

package um.example.TP5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Empleado> empleados = new ArrayList<>();
}

Como experto en JPA, verifica:

Relación bidireccional: ¿Está bien configurada la relación OneToMany en Departamento con la ManyToOne en Empleado?
Anotaciones JPA: ¿Todas las anotaciones son correctas y necesarias?
Performance: ¿Los FetchType y CascadeType son apropiados?
Consistencia: ¿El diseño es coherente con las mejores prácticas?
Problemas potenciales: ¿Hay algo que pueda causar errores en runtime?

Dame feedback específico y código corregido si es necesario.
```

### Respuesta Recibida:
```
✅ 1. RELACIÓN BIDIRECCIONAL: OneToMany / ManyToOne
En Departamento:
@OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Empleado> empleados = new ArrayList<>();

En Empleado:
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "departamento_id")
private Departamento departamento;

✔️ Veredicto: Relación bien definida
- mappedBy = "departamento" está correctamente apuntando al atributo de tipo Departamento en Empleado.
- ManyToOne es el lado propietario de la relación (owning side), mientras que OneToMany es el lado inverso (inverse side). Eso está bien definido.

✅ 2. ANOTACIONES JPA
✔️ Todas las anotaciones son correctas y necesarias.

Sugerencia menor:
Agregar @ToString.Exclude a la colección para evitar ciclos infinitos con Lombok:
@ToString.Exclude
@OneToMany(...)
private List<Empleado> empleados = new ArrayList<>();

✅ 3. PERFORMANCE: FetchType y Cascade
✅ fetch = FetchType.LAZY
Perfecto: evita traer toda la lista de empleados cuando no es necesario.

✅ cascade = CascadeType.ALL
Adecuado si cuando guardás/borrás un Departamento, también querés persistir/borrar sus Empleados. Si no querés eliminar empleados al eliminar el departamento, podrías usar solo:
cascade = {CascadeType.PERSIST, CascadeType.MERGE}
Pero CascadeType.ALL es válido si te asegurás de manejar bien la lógica de borrado.

✅ 4. CONSISTENCIA Y NOMENCLATURA
- El uso de nombres (empleados, departamento) es claro y estándar.
- Los nombres de columnas, longitud y restricciones están bien.
- La lista está inicializada correctamente (new ArrayList<>()), lo cual previene NullPointerException.

⚠️ 5. POTENCIALES PROBLEMAS EN RUNTIME
🔁 Peligro: ciclo infinito con @Data
Con relaciones bidireccionales, el uso de @Data puede causar:
- Ciclos en toString()
- Ciclos en equals() y hashCode() (si la relación se incluye)
📌 Solución recomendada:
@ToString.Exclude
@EqualsAndHashCode.Exclude
@OneToMany(...)
private List<Empleado> empleados = new ArrayList<>();
Esto evita que toString(), equals() y hashCode() entren en bucles infinitos.

✅ CONCLUSIÓN FINAL
✔️ Tu clase está bien estructurada y funcional.
🔁 Agregar @ToString.Exclude y @EqualsAndHashCode.Exclude si usás Lombok.
🧹 Todo el resto está conforme a buenas prácticas y es consistente.
```

### Modificaciones Realizadas:
- No hice ninguna modificación, el codigo que le pase estaba correcto
- Se tendrá en cuenta para mas adelante el uso de anotaciones de Lombok para evitar ciclos infinitos:

### Explicación del Prompt:
Este prompt fue diseñado estratégicamente para obtener una revisión exhaustiva de una entidad JPA compleja. Se estructuró de la siguiente manera:

- Contexto específico: Se proporcionó el código completo de la entidad para permitir un análisis concreto
- Rol definido: Se solicitó actuar "como experto en JPA" para obtener respuestas técnicas especializadas
- Puntos de revisión estructurados: Se definieron 5 áreas clave de análisis (relación bidireccional, anotaciones, performance, consistencia, problemas potenciales)
- Solicitud de feedback específico: Se pidió tanto análisis como código corregido cuando fuera necesario
- Enfoque en problemas prácticos: Se priorizó la identificación de errores en runtime y problemas de rendimiento

La efectividad del prompt radica en su especificidad y estructura clara, lo que permitió obtener una respuesta organizada y práctica.

### Aprendizajes Obtenidos:
- Gestión de relaciones bidireccionales: Confirmé que el lado @ManyToOne es el propietario (owning side) y controla la relación, mientras que @OneToMany con mappedBy es el lado inverso
- Problemas con Lombok en relaciones JPA: Descubrí que @Data puede causar ciclos infinitos en toString(), equals() y hashCode() cuando hay relaciones bidireccionales, requiriendo el uso de @ToString.Exclude y @EqualsAndHashCode.Exclude
- Estrategias de cascada: Aprendí que CascadeType.ALL puede ser riesgoso ya que incluye REMOVE, y que para casos más conservadores es mejor usar combinaciones específicas como {CascadeType.PERSIST, CascadeType.MERGE}
- Importancia del FetchType.LAZY: Confirmé que es la mejor práctica para colecciones grandes para evitar problemas de rendimiento (N+1 queries)
- Inicialización de colecciones: Validé que inicializar listas con new ArrayList<>() previene NullPointerException y es una buena práctica
- Naming conventions: Reforcé la importancia de nombres descriptivos y consistentes tanto en atributos Java como en nombres de columnas de base de datos
- Configuración de constraints: Aprendí sobre el uso efectivo de nullable = false, unique = true y length para garantizar integridad de datos

---

## Prompt 3: Análisis y Explicación de EmpleadoRepository - Spring Data JPA

### Prompt Utilizado:
```
Revisa mi EmpleadoRepository y explica los conceptos de Spring Data JPA:

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    List<Empleado> findByDepartamento(Departamento departamento);
    List<Empleado> findBySalarioBetween(BigDecimal salarioMin, BigDecimal salarioMax);
    List<Empleado> findByFechaContratacionAfter(LocalDate fecha);

    // Consulta personalizada con @Query (JPQL)
    @Query("SELECT e FROM Empleado e WHERE e.departamento.nombre = :nombreDepartamento")
    List<Empleado> findByNombreDepartamento(@Param("nombreDepartamento") String nombreDepartamento);

    @Query("SELECT AVG(e.salario) FROM Empleado e WHERE e.departamento.id = :departamentoId")
    Optional<BigDecimal> findAverageSalarioByDepartamento(@Param("departamentoId") Long departamentoId);
}


Como experto en Spring Data JPA, explica:

1. **Conceptos básicos**: ¿Qué es un Repository y por qué extendemos JpaRepository?
2. **Query Methods**: Brevd explicacion de cada método findBy para su mayor comprensión
3. **Custom Queries**: Analiza las consultas @Query y cuándo usarlas
4. **Mejores prácticas**: ¿Está bien implementado? ¿Qué mejorarías?

Dame explicaciones concisas con ejemplos de código cuando sea necesario.
```

### Respuesta Recibida:
```
1. 🧩 Conceptos básicos: ¿Qué es un Repository y por qué extendemos JpaRepository?
🔹 ¿Qué es un Repository?
Es una interfaz que actúa como capa de acceso a datos (DAO). Permite interactuar con la base de datos sin necesidad de escribir SQL explícito.

🔹 ¿Por qué extends JpaRepository<Empleado, Long>?
- Porque Spring Data JPA:
- Genera automáticamente métodos CRUD (save, findById, deleteById, etc.).
- Soporta paginación, ordenamiento y consultas derivadas de nombres de métodos.

Ejemplo:
empleadoRepository.findById(1L); // ya viene implementado

2. 🔍 Query Methods: explicación breve de cada findBy
✅ Optional<Empleado> findByEmail(String email)
Busca un empleado con un email específico. Devuelve Optional por si no lo encuentra.
empleadoRepository.findByEmail("juan@mail.com");

✅ List<Empleado> findByDepartamento(Departamento departamento)
Busca todos los empleados que pertenecen a un objeto Departamento.
empleadoRepository.findByDepartamento(dep); // `dep` es un objeto `Departamento`

✅ List<Empleado> findBySalarioBetween(BigDecimal salarioMin, BigDecimal salarioMax)
Busca empleados cuyo salario esté en un rango dado.
empleadoRepository.findBySalarioBetween(
    new BigDecimal("1000"), new BigDecimal("3000")
);

✅ List<Empleado> findByFechaContratacionAfter(LocalDate fecha)
Devuelve los empleados contratados después de una fecha.
empleadoRepository.findByFechaContratacionAfter(LocalDate.of(2023, 1, 1));

3. 🛠 Custom Queries con @Query
✅ @Query("SELECT e FROM Empleado e WHERE e.departamento.nombre = :nombreDepartamento")
Consulta personalizada usando JPQL (Java Persistence Query Language), más expresiva y flexible.

🔎 ¿Por qué usar @Query?
- Cuando el método es muy complejo para derivar automáticamente.
- Cuando necesitas usar propiedades de relaciones (como departamento.nombre).
empleadoRepository.findByNombreDepartamento("Finanzas");

✅ @Query("SELECT AVG(e.salario) FROM Empleado e WHERE e.departamento.id = :departamentoId")
Devuelve el salario promedio de un departamento. Útil para cálculos agregados.
empleadoRepository.findAverageSalarioByDepartamento(2L);

4. 🧠 Mejores prácticas y revisión
✅ Lo que hiciste bien:
Uso correcto de @Repository.

Uso limpio y claro de JpaRepository.

Optional donde corresponde.

Métodos derivados (findByX) bien nombrados y expresivos.

Consultas personalizadas bien estructuradas con @Query.

⚠️ Sugerencias menores:
Para mantener claridad, podés mover las consultas personalizadas a una clase EmpleadoCustomRepository si se hacen muy numerosas (→ Separación de responsabilidades).

Añadir @Param en métodos con varios parámetros, incluso cuando Spring los deduce, mejora la legibilidad.

✅ Conclusión
Tu EmpleadoRepository está muy bien implementado y sigue las buenas prácticas de Spring Data JPA. Las consultas están bien definidas, usando una combinación efectiva de métodos derivados y @Query personalizados.

Si seguís esta estructura para DepartamentoRepository y ProyectoRepository, estás en un muy buen camino. ¿Querés que te ayude a construirlos de forma similar?
```

### Modificaciones Realizadas:
- No se realizaron modificaciones al código: La respuesta confirmó que el repositorio estaba bien implementado tal como estaba
- Validación de estructura: Se confirmó que la combinación de query methods derivados y consultas personalizadas @Query era la apropiada

### Explicación del Prompt:
Este prompt fue diseñado para obtener una explicación educativa y práctica del repositorio. La estrategia utilizada incluye:

- Enfoque didáctico: Se solicitó explicación de conceptos fundamentales para repasar el aprendizaje teórico
- Análisis método por método: Se pidió explicación específica de cada query method para entender su funcionamiento
- Comparación de enfoques: Se incluyó análisis de query methods vs consultas personalizadas para entender cuándo usar cada uno
- Evaluación práctica: Se solicitó revisión de mejores prácticas para validar la implementación actual

La efectividad del prompt radica en su equilibrio entre explicación teórica y aplicación práctica.

### Aprendizajes Obtenidos:
- Patrón Repository: Comprendí que el Repository actúa como capa de abstracción entre la lógica de negocio y el acceso a datos, eliminando la necesidad de escribir SQL explícito
- JpaRepository como superinterface: Aprendí que extender de JpaRepository<Empleado, Long> proporciona automáticamente métodos CRUD, paginación y ordenamiento sin implementación adicional
- Query Methods derivados: Entendí cómo Spring Data JPA genera automáticamente consultas SQL basándose en los nombres de los métodos (findBy, Between, After, etc.)
- Uso de Optional: Reforcé la importancia de usar Optional<T> para métodos que pueden retornar null, como findByEmail(), mejorando la seguridad del código
- JPQL vs SQL nativo: Aprendí que JPQL (Java Persistence Query Language) trabaja con entidades y sus propiedades, no directamente con tablas SQL
- Navegación de relaciones en @Query: Descubrí cómo acceder a propiedades de entidades relacionadas usando notación de punto (e.departamento.nombre)
- Funciones agregadas: Comprendí el uso de funciones como AVG() en JPQL para cálculos estadísticos
- Parámetros nombrados: Aprendí la importancia de @Param para vincular parámetros del método con placeholders en las consultas JPQL

---

## Prompt n: [Descripción breve]

### Prompt Utilizado:
```
[Prompt completo aquí]
```

### Respuesta Recibida:
```
[Respuesta completa aquí]
```

### Modificaciones Realizadas:
- [Lista de cambios realizados al código generado]
- [Explicación de por qué se modificó]

### Explicación del Prompt:
[Explicar por qué se usó este prompt específico]

### Aprendizajes Obtenidos:
- [Lista de aprendizajes]
- [Conceptos nuevos comprendidos]
- [Buenas prácticas identificadas]

---
