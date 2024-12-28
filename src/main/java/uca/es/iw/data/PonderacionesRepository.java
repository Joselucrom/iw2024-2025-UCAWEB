package uca.es.iw.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PonderacionesRepository extends JpaRepository<Ponderaciones, Long>, JpaSpecificationExecutor<Ponderaciones> {
    Optional<Ponderaciones> findById(int id);
}
