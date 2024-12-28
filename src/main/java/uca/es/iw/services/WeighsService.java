package uca.es.iw.services;

import org.springframework.stereotype.Service;
import uca.es.iw.data.Ponderaciones;
import uca.es.iw.data.PonderacionesRepository;

@Service
public class WeighsService {
    private final PonderacionesRepository ponderacionesRepository;

    public WeighsService(PonderacionesRepository ponderacionesRepository) {
        this.ponderacionesRepository = ponderacionesRepository;
    }

    public Ponderaciones save(Ponderaciones ponderaciones) {
        return ponderacionesRepository.save(ponderaciones);
    }

    public Ponderaciones findById(int id) {
        return ponderacionesRepository.findById(id).orElseGet(Ponderaciones::new);
    }

    public void updateWeighs(double ponTecnica, double ponDisponibilidad, double ponOportunidad) {
        Ponderaciones ponderaciones = findById(1);
        ponderaciones.setPonTecnica(ponTecnica);
        ponderaciones.setPonDisponibilidad(ponDisponibilidad);
        ponderaciones.setPonOportunidad(ponOportunidad);

        save(ponderaciones);
    }

}
