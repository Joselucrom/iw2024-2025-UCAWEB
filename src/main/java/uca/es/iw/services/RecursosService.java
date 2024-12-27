package uca.es.iw.services;

import org.springframework.stereotype.Service;
import uca.es.iw.data.RecursosRepository;

@Service
public class RecursosService {
    private final RecursosRepository recursosRepository;

    public RecursosService(RecursosRepository recursosRepository) {
        this.recursosRepository = recursosRepository;
    }

    public Double getPresupuestoTotal() {
        return recursosRepository.findPresupuestoTotal();
    }




}
