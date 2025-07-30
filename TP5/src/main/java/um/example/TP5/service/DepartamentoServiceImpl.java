package um.example.TP5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Departamento;
import um.example.TP5.entity.Empleado;
import um.example.TP5.exception.DepartamentoNoEncontradoException;
import um.example.TP5.exception.EmpleadoNoEncontradoException;
import um.example.TP5.exception.ProyectoYaExisteException;
import um.example.TP5.repository.DepartamentoRepository;

import java.util.List;

@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public Departamento guardar(Departamento departamento) {
        if (departamentoRepository.findByNombre(departamento.getNombre()).isPresent()) {
            throw new DepartamentoYaExisteException("El departamento ya ha sido creado: " + departamento.getNombre());
        }
        return departamentoRepository.save(departamento);
    }


    @Override
    public Departamento buscarPorId(Long id) {
        return departamentoRepository.findById(id)
            .orElseThrow(() -> new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id));
    }

    @Override
    public List<Departamento> obtenerTodos() {
        return departamentoRepository.findAll();
    }

    @Override
    public Departamento buscarPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre)
            .orElseThrow(() -> new DepartamentoNoEncontradoException("Departamento no encontrado con nombre: " + nombre));
    }

    @Override
    public Departamento actualizar(Long id, Departamento departamento) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamento.setId(id);
        return departamentoRepository.save(departamento);
    }

    @Override
    public void eliminar(Long id) {
        if (!departamentoRepository.existsById(id)) {
            throw new DepartamentoNoEncontradoException("Departamento no encontrado con ID: " + id);
        }
        departamentoRepository.deleteById(id);
    }
}
