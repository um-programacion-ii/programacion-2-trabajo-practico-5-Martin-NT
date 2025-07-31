package um.example.TP5.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Departamento;
import um.example.TP5.exception.DepartamentoNoEncontradoException;
import um.example.TP5.exception.DepartamentoYaExisteException;
import um.example.TP5.repository.DepartamentoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DepartamentoServiceIntegrationTest {
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Departamento crearDepartamentoDePrueba() {
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        return departamento;
    }
    @Test
    void cuandoGuardarDepartamentoNuevo_entoncesSePersisteCorrectamente() {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();

        // Act
        Departamento guardado = departamentoService.guardar(departamento);

        // Assert
        assertNotNull(guardado.getId());
        assertEquals("IT", guardado.getNombre());
        assertTrue(departamentoRepository.existsById(guardado.getId()));
    }

    @Test
    void cuandoGuardarDepartamentoDuplicado_entoncesLanzaExcepcion() {
        // Arrange: guardar un departamento con nombre "IT"
        Departamento departamento = crearDepartamentoDePrueba();
        departamentoRepository.save(departamento);

        // Act + Assert: intentar guardar otro con el mismo nombre debe fallar
        Departamento duplicado = crearDepartamentoDePrueba(); // mismo nombre "IT"

        assertThrows(DepartamentoYaExisteException.class, () -> {
            departamentoService.guardar(duplicado);
        });
    }

    //Buscar departamento por ID existente (debe devolver el departamento)
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaDepartamento() throws DepartamentoNoEncontradoException {
        // Arrange: crear y guardar un departamento
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act: buscar departamento por el ID guardado
        Departamento resultado = departamentoService.buscarPorId(departamento.getId());

        // Assert: verificar que el departamento no sea nulo y que los IDs coincidan
        assertNotNull(resultado);
        assertEquals(departamento.getId(), resultado.getId());
    }

    //Buscar departamento por ID inexistente (debe lanzar excepción)
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act: buscar por un ID que no existe
        Long idInexistente = 9999L;

        // Assert: esperar la excepción
        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.buscarPorId(idInexistente);
        });
    }

    //Buscar departamento por nombre existente (debe devolver el departamento)
    @Test
    void cuandoBuscarPorNombreExistente_entoncesRetornaDepartamento() throws DepartamentoNoEncontradoException {
        // Arrange: crear y guardar un departamento
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act: buscar departamento por el nombre guardado
        Departamento resultado = departamentoService.buscarPorNombre(departamento.getNombre());

        // Assert: verificar que el empleado no sea nulo y que los nombres coincidan
        assertNotNull(resultado);
        assertEquals(departamento.getNombre(), resultado.getNombre());
    }

    //Buscar departamento por nombre inexistente (debe lanzar excepción)
    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesLanzaExcepcion() {
        // Act: buscar por un nombre que no existe
        String nombreInexistente = "Inexistente";

        // Assert: esperar la excepción
        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.buscarPorNombre(nombreInexistente);
        });
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaCompleta() {
        // Arrange
        departamentoRepository.deleteAll(); // asegurar que esté vacío

        Departamento depto1 = crearDepartamentoDePrueba();
        departamentoRepository.save(depto1);

        Departamento depto2 = crearDepartamentoDePrueba();
        depto2.setNombre("RRHH");
        depto2.setDescripcion("Departamento de Recursos Humanos");
        departamentoRepository.save(depto2);

        // Act
        List<Departamento> departamentos = departamentoService.obtenerTodos();

        // Assert
        assertEquals(2, departamentos.size());
    }

    //Caso éxito: departamento existe y se actualiza
    @Test
    void cuandoActualizarDepartamentoExistente_entoncesSeActualiza() throws DepartamentoNoEncontradoException {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        departamento.setDescripcion("Departamento de Tecno"); //cambio la descripcion

        // Act
        Departamento actualizado = departamentoService.actualizar(departamento.getId(), departamento);

        // Assert
        assertEquals("Departamento de Tecno", actualizado.getDescripcion());
    }

    //Caso error: departamento no existe, lanza excepción
    @Test
    void cuandoActualizarDepartamentoNoExistente_entoncesLanzaExcepcion() {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();

        // Act + Assert
        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.actualizar(9999L, departamento);
        });
    }

    //Caso éxito: eliminar departamento existente
    @Test
    void cuandoEliminarDepartamentoExistente_entoncesSeElimina() throws DepartamentoNoEncontradoException {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento);

        // Act
        departamentoService.eliminar(departamento.getId());

        // Assert
        assertFalse(departamentoRepository.existsById(departamento.getId()));
    }

    //Caso error: eliminar departamento no existente
    @Test
    void cuandoEliminarDepartamentoNoExistente_entoncesLanzaExcepcion() {
        // Act + Assert
        assertThrows(DepartamentoNoEncontradoException.class, () -> {
            departamentoService.eliminar(9999L);
        });
    }
}
