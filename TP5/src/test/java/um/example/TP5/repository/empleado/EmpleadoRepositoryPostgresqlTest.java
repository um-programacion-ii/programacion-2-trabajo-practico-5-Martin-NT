package um.example.TP5.repository.empleado;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Departamento;
import um.example.TP5.entity.Empleado;
import um.example.TP5.repository.DepartamentoRepository;
import um.example.TP5.repository.EmpleadoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("postgres")
@Transactional
public class EmpleadoRepositoryPostgresqlTest {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento departamento;
    private Empleado empleado1;
    private Empleado empleado2;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        departamentoRepository.save(departamento);

        empleado1 = new Empleado();
        empleado1.setNombre("Martin");
        empleado1.setApellido("Navarro");
        empleado1.setEmail("martin.navarro@empresa.com");
        empleado1.setFechaContratacion(LocalDate.of(2024, 1, 1));
        empleado1.setSalario(new BigDecimal("50000"));
        empleado1.setDepartamento(departamento);

        empleado2 = new Empleado();
        empleado2.setNombre("Martina");
        empleado2.setApellido("Rizzotti");
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleado2.setFechaContratacion(LocalDate.of(2025, 6, 1));
        empleado2.setSalario(new BigDecimal("70000"));
        empleado2.setDepartamento(departamento);

        empleadoRepository.saveAll(List.of(empleado1, empleado2));
    }

    @Test
    void testFindByEmail() {
        Optional<Empleado> resultado = empleadoRepository.findByEmail("martin.navarro@empresa.com");
        assertTrue(resultado.isPresent());
        assertEquals("martin.navarro@empresa.com", resultado.get().getEmail());
    }

    @Test
    void testFindByDepartamento() {
        List<Empleado> resultado = empleadoRepository.findByDepartamento(departamento);
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindBySalarioBetween() {
        List<Empleado> resultado = empleadoRepository.findBySalarioBetween(
            new BigDecimal("40000"), new BigDecimal("60000"));
        assertEquals(1, resultado.size());
        assertEquals("Martin", resultado.get(0).getNombre());
        assertEquals("Navarro", resultado.get(0).getApellido());
    }

    @Test
    void testFindByFechaContratacionAfter() {
        List<Empleado> resultado = empleadoRepository.findByFechaContratacionAfter(LocalDate.of(2025, 1, 1));
        assertEquals(1, resultado.size());
        assertEquals("Martina", resultado.get(0).getNombre());
        assertEquals("Rizzotti", resultado.get(0).getApellido());
    }

    @Test
    void testFindByNombreDepartamento() {
        List<Empleado> resultado = empleadoRepository.findByNombreDepartamento("IT");
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindAverageSalarioByDepartamento() {
        Optional<BigDecimal> promedio = empleadoRepository.findAverageSalarioByDepartamento(departamento.getId());
        assertTrue(promedio.isPresent());
        assertEquals(new BigDecimal("60000.0"), promedio.get());
    }

    @Test
    void testFindById() {
        Optional<Empleado> resultado = empleadoRepository.findById(empleado1.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Martin", resultado.get().getNombre());
    }

    @Test
    void testFindAllEmpleados() {
        var empleados = empleadoRepository.findAll();
        assertEquals(2, empleados.size());
    }

    @Test
    void testSaveEmpleado() {
        Empleado nuevo = new Empleado();
        nuevo.setNombre("Valentina");
        nuevo.setApellido("Rosales");
        nuevo.setEmail("valentina.rosales@empresa.com");
        nuevo.setFechaContratacion(LocalDate.now());
        nuevo.setSalario(new BigDecimal("30000"));

        Empleado guardado = empleadoRepository.save(nuevo);

        assertTrue(empleadoRepository.findById(guardado.getId()).isPresent());
        assertEquals("Valentina", guardado.getNombre());
    }

    @Test
    void testDeleteById() {
        empleadoRepository.deleteById(empleado2.getId());
        Optional<Empleado> eliminado = empleadoRepository.findById(empleado2.getId());
        assertTrue(eliminado.isEmpty());
    }
}
