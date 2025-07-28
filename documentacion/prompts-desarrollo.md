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
[... resto de la respuesta completa ...]

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
