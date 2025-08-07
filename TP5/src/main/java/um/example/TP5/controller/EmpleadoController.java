package um.example.TP5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import um.example.TP5.entity.Empleado;
import um.example.TP5.service.EmpleadoService;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Validated
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    // Inyección del servicio de empleados en el controlador
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /**
     * GET /api/empleados
     * Obtener todos los empleados.
     * @return Lista de todos los empleados registrados.
     */
    @GetMapping
    public List<Empleado> obtenerTodos() {
        return empleadoService.obtenerTodos();
    }

    /**
     * GET /api/empleados/{id}
     * Obtener un empleado por su ID.
     * @param id Identificador del empleado.
     * @return Empleado con el ID proporcionado.
     * @throws RuntimeException si no se encuentra el empleado.
     */
    @GetMapping("/{id}")
    public Empleado obtenerPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    /**
     * POST /api/empleados
     * Crear un nuevo empleado.
     * @param empleado Objeto Empleado recibido en el cuerpo de la solicitud.
     * @return El empleado creado.
     * @status 201 CREATED
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado crear(@RequestBody Empleado empleado) {
        return empleadoService.guardar(empleado);
    }

    /**
     * PUT /api/empleados/{id}
     * Actualizar un empleado existente.
     * @param id Identificador del empleado a actualizar.
     * @param empleado Datos nuevos del empleado.
     * @return Empleado actualizado.
     * @throws RuntimeException si no se encuentra el empleado.
     */
    @PutMapping("/{id}")
    public Empleado actualizar(@PathVariable Long id, @RequestBody Empleado empleado) {
        return empleadoService.actualizar(id, empleado);
    }

    /**
     * DELETE /api/empleados/{id}
     * Eliminar un empleado por su ID.
     * @param id Identificador del empleado a eliminar.
     * @status 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
    }

    /**
     * GET /api/empleados/departamento/{nombre}
     * Obtener empleados por nombre de departamento.
     * @param nombre Nombre del departamento.
     * @return Lista de empleados que pertenecen al departamento.
     */
    @GetMapping("/departamento/{nombre}")
    public List<Empleado> obtenerPorDepartamento(@PathVariable String nombre) {
        return empleadoService.buscarPorDepartamento(nombre);
    }

    /**
     * GET /api/empleados/salario?min={min}&max={max}
     * Obtener empleados dentro de un rango de salario.
     * @param min Salario mínimo.
     * @param max Salario máximo.
     * @return Lista de empleados cuyo salario esté dentro del rango indicado.
     */
    @GetMapping("/salario")
    public List<Empleado> obtenerPorRangoSalario(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max) {
        return empleadoService.buscarPorRangoSalario(min, max);
    }

    /**
     * GET /api/empleados/salario/promedio/{departamentoId}
     * Obtener el salario promedio de un departamento.
     * @param departamentoId ID del departamento.
     * @return Salario promedio del departamento.
     */
    @GetMapping("/salario/promedio/{departamentoId}")
    public BigDecimal obtenerSalarioPromedioPorDepartamento(@PathVariable Long departamentoId) {
        return empleadoService.obtenerSalarioPromedioPorDepartamento(departamentoId);
    }

}

