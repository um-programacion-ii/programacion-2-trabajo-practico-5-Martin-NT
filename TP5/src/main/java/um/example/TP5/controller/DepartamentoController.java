package um.example.TP5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import um.example.TP5.entity.Departamento;
import um.example.TP5.service.DepartamentoService;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@Validated
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    // Inyección del servicio de departamentoS en el controlador
    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * GET /api/departamentos
     * Obtiene una lista con todos los departamentos existentes.
     * @return Lista de objetos Departamento
     */
    @GetMapping
    public List<Departamento> obtenerTodos() {
        return departamentoService.obtenerTodos();
    }

    /**
     * GET /api/departamentos/{id}
     * Busca un departamento por su ID.
     * @param id ID del departamento
     * @return Objeto Departamento correspondiente
     */
    @GetMapping("/{id}")
    public Departamento obtenerPorId(@PathVariable Long id) {
        return departamentoService.buscarPorId(id);
    }

    /**
     * GET /api/departamentos/nombre/{nombre}
     * Busca un departamento por su nombre.
     * @param nombre Nombre del departamento
     * @return Objeto Departamento correspondiente
     */
    @GetMapping("/nombre/{nombre}")
    public Departamento obtenerPorNombre(@PathVariable String nombre) {
        return departamentoService.buscarPorNombre(nombre);
    }

    /**
     * POST /api/departamentos
     * Crea un nuevo departamento.
     * @param departamento Objeto Departamento en el cuerpo de la petición
     * @return Departamento creado
     * @status 201 CREATED
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Departamento crear(@RequestBody Departamento departamento) {
        return departamentoService.guardar(departamento);
    }

    /**
     * PUT /api/departamentos/{id}
     * Actualiza un departamento existente por ID.
     * @param id ID del departamento a actualizar
     * @param departamento Datos actualizados en el cuerpo de la petición
     * @return Departamento actualizado
     */
    @PutMapping("/{id}")
    public Departamento actualizar(@PathVariable Long id, @RequestBody Departamento departamento) {
        return departamentoService.actualizar(id, departamento);
    }

    /**
     * DELETE /api/departamentos/{id}
     * Elimina un departamento por su ID.
     * @param id ID del departamento a eliminar
     * @status 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        departamentoService.eliminar(id);
    }
}

