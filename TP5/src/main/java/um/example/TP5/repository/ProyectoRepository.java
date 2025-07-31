package um.example.TP5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import um.example.TP5.entity.Proyecto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    Optional<Proyecto> findByNombre(String nombre);
    List<Proyecto> findByFechaInicio(LocalDate fechaInicio);
    List<Proyecto> findByFechaFinAfter(LocalDate fechaFin);
}
