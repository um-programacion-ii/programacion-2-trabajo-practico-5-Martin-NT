package um.example.TP5.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Departamento;
import um.example.TP5.entity.Empleado;
import um.example.TP5.exception.EmailDuplicadoException;
import um.example.TP5.exception.EmpleadoNoEncontradoException;
import um.example.TP5.repository.DepartamentoRepository;
import um.example.TP5.repository.EmpleadoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class EmpleadoServiceIntegrationTest {
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;

    private Empleado crearEmpleadoDePrueba() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Martin");
        empleado.setApellido("Navarro");
        empleado.setEmail("martin.navarro@empresa.com");
        empleado.setFechaContratacion(LocalDate.now());
        empleado.setSalario(new BigDecimal("50000.00"));
        return empleado;
    }

    private Departamento crearDepartamentoDePrueba() {
        Departamento departamento = new Departamento();
        departamento.setNombre("IT");
        departamento.setDescripcion("Departamento de Tecnología");
        return departamento;
    }

    @Test
    void cuandoGuardarEmpleado_entoncesSePersisteCorrectamente() {
        // Arrange
        Departamento departamento = crearDepartamentoDePrueba();
        departamento = departamentoRepository.save(departamento); // Se guarda en la base de datos.

        Empleado empleado = crearEmpleadoDePrueba();
        empleado.setDepartamento(departamento); // Se le asigna el departamento creado.

        // Act
        // Se llama al metodo del servicio para guardar el empleado en la base de datos.
        Empleado empleadoGuardado = empleadoService.guardar(empleado);

        // Assert
        assertNotNull(empleadoGuardado.getId());
        assertEquals("martin.navarro@empresa.com", empleadoGuardado.getEmail());
        assertTrue(empleadoRepository.existsById(empleadoGuardado.getId()));
    }

    @Test
    void cuandoBuscarPorEmailExistente_entoncesRetornaEmpleado() {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleadoRepository.save(empleado);

        // Act
        Optional<Empleado> resultado = empleadoRepository.findByEmail("martin.navarro@empresa.com"); //Se busca ese empleado por email.

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("martin.navarro@empresa.com", resultado.get().getEmail());
    }

    @Test
    void cuandoGuardarEmpleado_conEmailDuplicado_entoncesLanzaExcepcion() {
        // Arrange: creamos y guardamos un empleado con un email específico
        Empleado empleado1 = crearEmpleadoDePrueba();
        empleadoRepository.save(empleado1);

        // Creamos otro empleado con el mismo email
        Empleado empleadoDuplicado = crearEmpleadoDePrueba();

        // Act + Assert: intentar guardar el empleado duplicado debe lanzar excepción
        //Usamos assertThrows para asegurarnos que ese llamado lance exactamente la excepción EmailDuplicadoException.
        //Si no lanza la excepción, el test falla.
        assertThrows(EmailDuplicadoException.class, () -> {
            empleadoService.guardar(empleadoDuplicado);
        });
    }

    // Buscar empleado por ID existente (debe devolver el empleado)
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaEmpleado() throws EmpleadoNoEncontradoException {
        // Arrange: crear y guardar un empleado
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        // Act: buscar empleado por el ID guardado
        Empleado resultado = empleadoService.buscarPorId(empleado.getId());

        // Assert: verificar que el empleado no sea nulo y que los IDs coincidan
        assertNotNull(resultado);
        assertEquals(empleado.getId(), resultado.getId());
    }

    //Buscar empleado por ID inexistente (debe lanzar excepción)
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        // Act: buscar por un ID que no existe
        Long idInexistente = 9999L;

        // Assert: esperar la excepción
        assertThrows(EmpleadoNoEncontradoException.class, () -> {
            empleadoService.buscarPorId(idInexistente);
        });
    }

    @Test
    void cuandoBuscarPorDepartamento_entoncesRetornaListaEmpleados() {
        // Arrange
        Departamento deptoIT = crearDepartamentoDePrueba();
        deptoIT.setNombre("IT");
        departamentoRepository.save(deptoIT);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setDepartamento(deptoIT);
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleado2.setDepartamento(deptoIT);
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoService.buscarPorDepartamento("IT");

        // Assert
        assertEquals(2, empleados.size());
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("martin.navarro@empresa.com")));
        assertTrue(empleados.stream().anyMatch(e -> e.getEmail().equals("martina.rizzotti@empresa.com")));
    }

    @Test
    void cuandoBuscarPorRangoSalario_entoncesRetornaListaFiltrada() {
        // Arrange
        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setSalario(new BigDecimal("40000.00"));
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleado2.setSalario(new BigDecimal("60000.00"));
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoService.buscarPorRangoSalario(
            new BigDecimal("45000.00"),
            new BigDecimal("70000.00"));

        // Assert
        assertEquals(1, empleados.size());
        assertEquals("martina.rizzotti@empresa.com", empleados.get(0).getEmail());
    }

    @Test
    void cuandoObtenerSalarioPromedioPorDepartamento_entoncesCalculaCorrectamente() {
        // Arrange
        Departamento depto = crearDepartamentoDePrueba();
        depto = departamentoRepository.save(depto);

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleado1.setDepartamento(depto);
        empleado1.setSalario(new BigDecimal("40000.00"));
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleado2.setDepartamento(depto);
        empleado2.setSalario(new BigDecimal("60000.00"));
        empleadoRepository.save(empleado2);

        BigDecimal salarioPromedioEsperado = new BigDecimal("50000.00");

        // Act
        BigDecimal salarioPromedio = empleadoService.obtenerSalarioPromedioPorDepartamento(depto.getId());

        // Assert
        assertEquals(0, salarioPromedio.compareTo(salarioPromedioEsperado)); // comparar BigDecimal correctamente
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaListaCompleta() {
        // Arrange
        empleadoRepository.deleteAll(); // asegurar que esté vacío

        Empleado empleado1 = crearEmpleadoDePrueba();
        empleadoRepository.save(empleado1);

        Empleado empleado2 = crearEmpleadoDePrueba();
        empleado2.setEmail("martina.rizzotti@empresa.com");
        empleadoRepository.save(empleado2);

        // Act
        List<Empleado> empleados = empleadoService.obtenerTodos();

        // Assert
        assertEquals(2, empleados.size());
    }

    //Caso éxito: empleado existe y se actualiza
    @Test
    void cuandoActualizarEmpleadoExistente_entoncesSeActualiza() throws EmpleadoNoEncontradoException {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        empleado.setSalario(new BigDecimal("55000.00")); // cambiar salario

        // Act
        Empleado actualizado = empleadoService.actualizar(empleado.getId(), empleado);

        // Assert
        assertEquals(new BigDecimal("55000.00"), actualizado.getSalario());
    }

    //Caso error: empleado no existe, lanza excepción
    @Test
    void cuandoActualizarEmpleadoNoExistente_entoncesLanzaExcepcion() {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();

        // Act + Assert
        assertThrows(EmpleadoNoEncontradoException.class, () -> {
            empleadoService.actualizar(9999L, empleado);
        });
    }

    //Caso éxito: eliminar empleado existente
    @Test
    void cuandoEliminarEmpleadoExistente_entoncesSeElimina() throws EmpleadoNoEncontradoException {
        // Arrange
        Empleado empleado = crearEmpleadoDePrueba();
        empleado = empleadoRepository.save(empleado);

        // Act
        empleadoService.eliminar(empleado.getId());

        // Assert
        assertFalse(empleadoRepository.existsById(empleado.getId()));
    }

    //Caso error: eliminar empleado no existente
    @Test
    void cuandoEliminarEmpleadoNoExistente_entoncesLanzaExcepcion() {
        // Act + Assert
        assertThrows(EmpleadoNoEncontradoException.class, () -> {
            empleadoService.eliminar(9999L);
        });
    }
    
}
