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

    @GetMapping
    public List<Proyecto> obtenerTodos() {
        return proyectoService.obtenerTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proyecto crear(@RequestBody Proyecto proyecto) {
        return proyectoService.guardar(proyecto);
    }

    @PutMapping("/{id}")
    public Proyecto actualizar(@PathVariable Long id, @RequestBody Proyecto proyecto) {
        return proyectoService.actualizar(id, proyecto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
    }

    @GetMapping("id/{id}")
    public Proyecto obtenerPorId(@PathVariable Long id) {
        return proyectoService.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public Proyecto obtenerPorNombre(@PathVariable String nombre) {
        return proyectoService.buscarPorNombre(nombre);
    }

    @GetMapping("/activos")
    public List<Proyecto> obtenerPorProyectosActivos() {
        return proyectoService.buscarProyectosActivos();
    }
}
