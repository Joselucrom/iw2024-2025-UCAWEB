package uca.es.iw.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uca.es.iw.data.Proyecto;
import uca.es.iw.data.ProyectoRepository;

import java.util.Optional;

@RestController
public class ProyectoController {

    private final ProyectoRepository proyectoRepository;

    public ProyectoController(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @GetMapping("/api/downloads")
    public ResponseEntity<byte[]> descargarMemoria(@RequestParam String nombreProyecto) {
        Optional<Proyecto> proyectoOptional = proyectoRepository.findByNombreCorto(nombreProyecto);

        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();

            if (proyecto.getMemoria() != null) {
                byte[] memoria = proyecto.getMemoria();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=" + nombreProyecto + "_memoria.pdf");

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(memoria);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Memoria no disponible
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Proyecto no encontrado
        }
    }
}
