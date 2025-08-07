package um.example.TP5.repository.departamento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Departamento;
import um.example.TP5.repository.DepartamentoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("postgres")
@Transactional
public class DepartamentoRepositoryPostgresqlTest {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento1;
    private Departamento departamento2;

    @BeforeEach
    void setUp() {
        departamento1 = new Departamento();
        departamento1.setNombre("IT");
        departamento1.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento1);

        departamento2 = new Departamento();
        departamento2.setNombre("RRHH");
        departamento2.setDescripcion("Departamento de Recursos Humanos");
        departamentoRepository.save(departamento2);
    }

    @Test
    void testFindByNombre() {
        Optional<Departamento> resultado = departamentoRepository.findByNombre("IT");
        assertTrue(resultado.isPresent());
        assertEquals("IT", resultado.get().getNombre());
    }

    @Test
    void testFindById() {
        Optional<Departamento> resultado = departamentoRepository.findById(departamento1.getId());
        assertTrue(resultado.isPresent());
        assertEquals("IT", resultado.get().getNombre());
    }

    @Test
    void testFindAllDepartamentos() {
        var departamentos = departamentoRepository.findAll();
        assertEquals(2, departamentos.size());
    }

    @Test
    void testSaveDepartamento() {
        Departamento nuevo = new Departamento();
        nuevo.setNombre("Marketing");
        nuevo.setDescripcion("Departamento de Marketing");

        Departamento guardado = departamentoRepository.save(nuevo);

        assertTrue(departamentoRepository.findById(guardado.getId()).isPresent());
        assertEquals("Marketing", guardado.getNombre());
    }

    @Test
    void testDeleteById() {
        departamentoRepository.deleteById(departamento2.getId());
        Optional<Departamento> eliminado = departamentoRepository.findById(departamento2.getId());
        assertTrue(eliminado.isEmpty());
    }
}
