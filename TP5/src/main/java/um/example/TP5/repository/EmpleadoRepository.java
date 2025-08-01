package um.example.TP5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import um.example.TP5.entity.Departamento;
import um.example.TP5.entity.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
