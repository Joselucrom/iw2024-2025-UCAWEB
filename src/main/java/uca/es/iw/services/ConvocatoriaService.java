package uca.es.iw.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uca.es.iw.data.Convocatoria;
import uca.es.iw.data.ConvocatoriaRepository;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConvocatoriaService {

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    // Crear una nueva convocatoria
    public Convocatoria createConvocatoria(String nombre, String objetivo, LocalDate fechaApertura,
                                           LocalDate fechaCierre, double presupuestoTotal, int cupoRecursosHumanos) {
        Convocatoria convocatoria = new Convocatoria();
        convocatoria.setNombre(nombre);
        convocatoria.setObjetivo(objetivo);
        convocatoria.setFechaApertura(fechaApertura);
        convocatoria.setFechaCierre(fechaCierre);
        convocatoria.setPresupuestoTotal(presupuestoTotal);
        convocatoria.setCupoRecursosHumanos(cupoRecursosHumanos);

        return convocatoriaRepository.save(convocatoria);
    }

    // Obtener todas las convocatorias
    public List<Convocatoria> getAllConvocatorias() {
        return convocatoriaRepository.findAll();
    }

    // Obtener una convocatoria por ID
    public Convocatoria getConvocatoriaById(Long id) {
        Optional<Convocatoria> convocatoria = convocatoriaRepository.findById(id);
        return convocatoria.orElseThrow(() -> new IllegalArgumentException("Convocatoria no encontrada"));
    }


    // Actualizar una convocatoria
    public Convocatoria updateConvocatoria(Long id, String nombre, String objetivo, LocalDate fechaApertura,
                                           LocalDate fechaCierre, double presupuestoTotal, int cupoRecursosHumanos) {
        Convocatoria convocatoria = getConvocatoriaById(id);
        convocatoria.setNombre(nombre);
        convocatoria.setObjetivo(objetivo);
        convocatoria.setFechaApertura(fechaApertura);
        convocatoria.setFechaCierre(fechaCierre);
        convocatoria.setPresupuestoTotal(presupuestoTotal);
        convocatoria.setCupoRecursosHumanos(cupoRecursosHumanos);

        return convocatoriaRepository.save(convocatoria);
    }

    // Eliminar una convocatoria
    public void deleteConvocatoria(Long id) {
        Convocatoria convocatoria = getConvocatoriaById(id);
        convocatoriaRepository.delete(convocatoria);
    }
}