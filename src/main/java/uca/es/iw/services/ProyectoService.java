package uca.es.iw.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uca.es.iw.data.Proyecto;
import uca.es.iw.data.ProyectoRepository;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public Proyecto guardarProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public Optional<Proyecto> get(Long id) {
        return proyectoRepository.findById(id);
    }


    public Page<Proyecto> list(Pageable pageable) {
        return proyectoRepository.findAll(pageable);
    }

    public void delete(Long id) {
        proyectoRepository.deleteById(id);
    }

    public Proyecto update(Proyecto proyecto) {
        if (proyecto.getId() != null && proyectoRepository.existsById(proyecto.getId())) {
            return proyectoRepository.save(proyecto);  // Save funciona para actualizar registros existentes
        } else {
            throw new IllegalArgumentException("El proyecto no existe o no tiene ID");
        }
    }
}
