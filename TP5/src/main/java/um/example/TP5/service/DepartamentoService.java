package um.example.TP5.service;
import um.example.TP5.entity.Departamento;
import java.util.List;

public interface DepartamentoService {
    Departamento guardar(Departamento departamento);
    Departamento buscarPorId(Long id);
    List<Departamento> obtenerTodos();
    Departamento actualizar(Long id, Departamento departamento);
    void eliminar(Long id);
    Departamento buscarPorNombre(String nombre);
}
