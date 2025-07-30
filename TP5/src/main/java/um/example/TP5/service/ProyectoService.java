package um.example.TP5.service;
import um.example.TP5.entity.Proyecto;

import java.util.List;

public interface ProyectoService {
    Proyecto guardar(Proyecto proyecto);
    Proyecto buscarPorId(Long id);
    List<Proyecto> obtenerTodos();
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
    Proyecto buscarPorNombre(String nombre);
    List<Proyecto> buscarProyectosActivos();
}
