package um.example.TP5.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Proyecto;
import um.example.TP5.exception.ProyectoNoEncontradoException;
import um.example.TP5.exception.ProyectoYaExisteException;
import um.example.TP5.repository.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProyectoServiceIntegrationTest {
    @Autowired
    private ProyectoService proyectoService;
    @Autowired
    private ProyectoRepository proyectoRepository;

    private Proyecto crearProyectoDePrueba() {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto Alpha");
        proyecto.setDescripcion("Sistema de gestión interna");
        proyecto.setFechaInicio(LocalDate.of(2024, 1, 1));
        proyecto.setFechaFin(LocalDate.of(2024, 12, 31));
        return proyecto;
    }

    @Test
    void cuandoGuardarProyectoNuevo_entoncesSePersisteCorrectamente() {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();

        // Act
        Proyecto guardado = proyectoService.guardar(proyecto);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals("Proyecto Alpha", guardado.getNombre());
        assertTrue(proyectoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoGuardarProyectoDuplicado_entoncesLanzaExcepcion() {
        // Arrange: guardar un proyecto
        Proyecto proyecto = crearProyectoDePrueba();
        proyectoRepository.save(proyecto);

        // Act + Assert: intentar guardar otro con el mismo nombre debe fallar
        Proyecto duplicado = crearProyectoDePrueba(); // mismo nombre

        assertThrows(ProyectoYaExisteException.class, () -> {
            proyectoService.guardar(duplicado);
        });
    }

    //Buscar proyecto por ID existente (debe devolver el proyecto)
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaProyecto() throws ProyectoNoEncontradoException {
        // Arrange: crear y guardar un proyecto
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act: buscar proyecto por el ID guardado
        Proyecto resultado = proyectoService.buscarPorId(proyecto.getId());

        // Assert: verificar que el proyecto no sea nulo y que los IDs coincidan
        assertNotNull(resultado);
        assertEquals(proyecto.getId(), resultado.getId());
    }

    //Buscar proyecto por ID inexistente (debe lanzar excepción)
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act: buscar por un ID que no existe
        Long idInexistente = 9999L;

        // Assert: esperar la excepción
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.buscarPorId(idInexistente);
        });
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaCompleta() {
        // Arrange
        proyectoRepository.deleteAll(); // asegurar que esté vacío

        Proyecto proyecto1 = crearProyectoDePrueba();
        proyectoRepository.save(proyecto1);

        Proyecto proyecto2 = crearProyectoDePrueba();
        proyecto2.setNombre("Proyecto Beta");
        proyecto2.setDescripcion("Sistema de gestión externa");
        proyectoRepository.save(proyecto2);

        // Act
        List<Proyecto> proyectos = proyectoService.obtenerTodos();

        // Assert
        assertEquals(2, proyectos.size());
    }

    //Buscar proyecto por nombre existente (debe devolver el proyecto)
    @Test
    void cuandoBuscarPorNombreExistente_entoncesRetornaProyecto() throws ProyectoNoEncontradoException {
        // Arrange: crear y guardar un proyecto
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act: buscar proyecto por el nombre guardado
        Proyecto resultado = proyectoService.buscarPorNombre(proyecto.getNombre());

        // Assert: verificar que el proyecto no sea nulo y que los nombres coincidan
        assertNotNull(resultado);
        assertEquals(proyecto.getNombre(), resultado.getNombre());
    }

    //Buscar proyecto por nombre inexistente (debe lanzar excepción)
    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesLanzaExcepcion() {
        // Act: buscar por un nombre que no existe
        String nombreInexistente = "Inexistente";

        // Assert: esperar la excepción
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.buscarPorNombre(nombreInexistente);
        });
    }

    //Caso éxito: proyecto existe y se actualiza
    @Test
    void cuandoActualizarProyectoExistente_entoncesSeActualiza() throws ProyectoNoEncontradoException {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        proyecto.setDescripcion("Sistema de gestión");

        // Act
        Proyecto actualizado = proyectoService.actualizar(proyecto.getId(), proyecto);

        // Assert
        assertEquals("Sistema de gestión", actualizado.getDescripcion());
    }

    //Caso error: proyecto no existe, lanza excepción
    @Test
    void cuandoActualizarDepartamentoNoExistente_entoncesLanzaExcepcion() {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();

        // Act + Assert
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.actualizar(9999L, proyecto);
        });
    }

    //Caso éxito: eliminar proyecto existente
    @Test
    void cuandoEliminarDepartamentoExistente_entoncesSeElimina() throws ProyectoNoEncontradoException {
        // Arrange
        Proyecto proyecto = crearProyectoDePrueba();
        proyecto = proyectoRepository.save(proyecto);

        // Act
        proyectoService.eliminar(proyecto.getId());

        // Assert
        assertFalse(proyectoRepository.existsById(proyecto.getId()));
    }

    //Caso error: eliminar proyecto no existente
    @Test
    void cuandoEliminarDepartamentoNoExistente_entoncesLanzaExcepcion() {
        // Act + Assert
        assertThrows(ProyectoNoEncontradoException.class, () -> {
            proyectoService.eliminar(9999L);
        });
    }

    @Test
    void cuandoBuscarProyectosActivos_entoncesRetornaSoloProyectosConFechaFinPosteriorAHoy() {
        // Arrange
        proyectoRepository.deleteAll(); // asegurar que no haya datos previos

        LocalDate hoy = LocalDate.now();

        Proyecto activo = new Proyecto();
        activo.setNombre("Proyecto Activo");
        activo.setDescripcion("Aún en curso");
        activo.setFechaInicio(hoy.minusMonths(1));
        activo.setFechaFin(hoy.plusMonths(2)); // activo
        proyectoRepository.save(activo);

        Proyecto finalizado = new Proyecto();
        finalizado.setNombre("Proyecto Finalizado");
        finalizado.setDescripcion("Ya finalizado");
        finalizado.setFechaInicio(hoy.minusMonths(6));
        finalizado.setFechaFin(hoy.minusDays(1)); // ya terminó
        proyectoRepository.save(finalizado);

        // Act
        List<Proyecto> activos = proyectoService.buscarProyectosActivos();

        // Assert
        assertEquals(1, activos.size());
        assertEquals("Proyecto Activo", activos.get(0).getNombre());
        assertTrue(activos.get(0).getFechaFin().isAfter(hoy));
    }



}
