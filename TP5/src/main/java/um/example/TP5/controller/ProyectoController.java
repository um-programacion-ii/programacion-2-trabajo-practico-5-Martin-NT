package um.example.TP5.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import um.example.TP5.entity.Proyecto;
import um.example.TP5.service.ProyectoService;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@Validated
public class ProyectoController {
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    /**
     * GET /api/proyectos
     * Obtiene todos los proyectos registrados.
     * @return Lista de proyectos
     */
    @GetMapping
    public List<Proyecto> obtenerTodos() {
        return proyectoService.obtenerTodos();
    }

    /**
     * POST /api/proyectos
     * Crea un nuevo proyecto.
     * @param proyecto Proyecto a crear (en el body de la solicitud)
     * @return Proyecto creado con su ID asignado
     * @status 201 CREATED
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proyecto crear(@RequestBody Proyecto proyecto) {
        return proyectoService.guardar(proyecto);
    }

    /**
     * PUT /api/proyectos/{id}
     * Actualiza un proyecto existente por su ID.
     * @param id ID del proyecto a actualizar
     * @param proyecto Datos nuevos del proyecto
     * @return Proyecto actualizado
     */
    @PutMapping("/{id}")
    public Proyecto actualizar(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        return proyectoService.actualizar(id, proyecto);
    }

    /**
     * DELETE /api/proyectos/{id}
     * Elimina un proyecto por su ID.
     * @param id ID del proyecto a eliminar
     * @status 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }

    /**
     * GET /api/proyectos/{id}
     * Obtiene un proyecto por su ID.
     * @param id ID del proyecto a buscar
     * @return Proyecto encontrado
     */
    @GetMapping("/{id}")
    public Proyecto obtenerPorId(@PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    /**
     * GET /api/proyectos/nombre/{nombre}
     * Obtiene un proyecto por su nombre.
     * @param nombre Nombre del proyecto a buscar
     * @return Proyecto encontrado
     */
    @GetMapping("/nombre/{nombre}")
    public Proyecto obtenerPorNombre(@PathVariable String nombre) {
        return proyectoService.buscarPorNombre(nombre);
    }

    /**
     * GET /api/proyectos/activos
     * Lista todos los proyectos activos.
     * @return Lista de proyectos con estado activo
     */
    @GetMapping("/activos")
    public List<Proyecto> obtenerPorProyectosActivos() {
        return proyectoService.buscarProyectosActivos();
    }
}
