package um.example.TP5.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.example.TP5.entity.Proyecto;
import um.example.TP5.exception.ProyectoNoEncontradoException;
import um.example.TP5.exception.ProyectoYaExisteException;
import um.example.TP5.repository.ProyectoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ProyectoServiceImpl implements ProyectoService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        if (proyectoRepository.findByNombre(proyecto.getNombre()).isPresent()) {
            throw new ProyectoYaExisteException("El proyecto ya ha sido creado: " + proyecto.getNombre());
        }
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto buscarPorId(Long id) {
        return proyectoRepository.findById(id)
            .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id));
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombre(nombre)
            .orElseThrow(() -> new ProyectoNoEncontradoException("Proyecto no encontrado con nombre: " + nombre));
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyecto.setId(id);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminar(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new ProyectoNoEncontradoException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> buscarProyectosActivos() {
        return proyectoRepository.findByFechaFinAfter(LocalDate.now());
    }

}

