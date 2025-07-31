package um.example.TP5.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import um.example.TP5.entity.Departamento;
import java.util.Optional;

// Extiende JpaRepository (ya tiene métodos listos: findAll(), findById(), save(), deleteById()…)
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByNombre(String nombre);

}
