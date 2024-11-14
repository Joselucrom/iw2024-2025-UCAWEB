package uca.es.iw.data;

import org.springframework.data.jpa.repository.JpaRepository;
import uca.es.iw.data.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // No se necesita definir métodos adicionales, JpaRepository proporciona métodos CRUD por defecto
}
