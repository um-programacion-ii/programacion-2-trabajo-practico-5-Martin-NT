package um.example.TP5.repository.proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Proyecto;
import um.example.TP5.repository.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("mysql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProyectoRepositoryMysqlTest {
    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        proyecto1 = new Proyecto();
        proyecto1.setNombre("Proyecto Alpha");
        proyecto1.setDescripcion("Sistema de gestión interna");
        proyecto1.setFechaInicio(LocalDate.of(2024, 1, 1));
        proyecto1.setFechaFin(LocalDate.of(2024, 12, 31));
        proyectoRepository.save(proyecto1);

        proyecto2 = new Proyecto();
        proyecto2.setNombre("Proyecto Beta");
        proyecto2.setDescripcion("Sistema de gestión externa");
        proyecto2.setFechaInicio(LocalDate.of(2024, 5, 1));
        proyecto2.setFechaFin(LocalDate.of(2025, 6, 30));
        proyectoRepository.save(proyecto2);
    }

    @Test
    void testFindById() {
        Optional<Proyecto> resultado = proyectoRepository.findById(proyecto1.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Proyecto Alpha", resultado.get().getNombre());
    }

    @Test
    void testFindAllProyectos() {
        var proyectos = proyectoRepository.findAll();
        assertEquals(2, proyectos.size());
    }

    @Test
    void testSaveProyecto() {
        Proyecto nuevo = new Proyecto();
        nuevo.setNombre("Proyecto Omega");
        nuevo.setDescripcion("Sistema de gestión");

        Proyecto guardado = proyectoRepository.save(nuevo);

        assertTrue(proyectoRepository.findById(guardado.getId()).isPresent());
        assertEquals("Proyecto Omega", guardado.getNombre());
    }

    @Test
    void testDeleteById() {
        proyectoRepository.deleteById(proyecto2.getId());
        Optional<Proyecto> eliminado = proyectoRepository.findById(proyecto2.getId());
        assertTrue(eliminado.isEmpty());
    }

    @Test
    void testFindByNombre() {
        Optional<Proyecto> resultado = proyectoRepository.findByNombre(proyecto2.getNombre());
        assertTrue(resultado.isPresent());
        assertEquals("Proyecto Beta", resultado.get().getNombre());
    }

    @Test
    void testFindByFechaInicio() {
        List<Proyecto> resultados = proyectoRepository.findByFechaInicio(LocalDate.of(2024, 5, 1));
        assertEquals(1, resultados.size());
        assertEquals("Proyecto Beta", resultados.get(0).getNombre());
    }

    @Test
    void testFindByFechaFinAfter() {
        List<Proyecto> resultados = proyectoRepository.findByFechaFinAfter(LocalDate.of(2024, 12, 31));
        assertEquals(1, resultados.size());
        assertEquals("Proyecto Beta", resultados.get(0).getNombre());
    }
}
