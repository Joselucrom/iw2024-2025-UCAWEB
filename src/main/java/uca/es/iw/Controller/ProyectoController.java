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
    public ResponseEntity<byte[]> descargarMemoria(@RequestParam String nombreProyecto, int option) {
        Optional<Proyecto> proyectoOptional = proyectoRepository.findByNombreCorto(nombreProyecto);

        if (proyectoOptional.isPresent()) {
            Proyecto proyecto = proyectoOptional.get();
            byte[] archivo = null;
            String tipoArchivo = "";

            // Seleccionar el archivo según la opción
            if (option == 1) {
                archivo = proyecto.getMemoria();
                tipoArchivo = "memoria";
            } else if (option == 2) {
                archivo = proyecto.getEspecificaciones();
                tipoArchivo = "especificaciones";
            } else if (option == 3) {
                archivo = proyecto.getPresupuestos();
                tipoArchivo = "presupuestos";
            }

            if (archivo != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment; filename=" + nombreProyecto + "_" + tipoArchivo + ".pdf");

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(archivo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Archivo no disponible
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Proyecto no encontrado
        }
    }
}
