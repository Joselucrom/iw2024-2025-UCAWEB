
package uca.es.iw.services;

import org.springframework.stereotype.Service;
import uca.es.iw.data.Proyecto;
import uca.es.iw.data.ProyectoRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public Proyecto guardarProyecto(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }
}
