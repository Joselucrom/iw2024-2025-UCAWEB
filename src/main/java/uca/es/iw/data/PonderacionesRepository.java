package uca.es.iw.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PonderacionesRepository extends JpaRepository<Ponderaciones, Long>, JpaSpecificationExecutor<Ponderaciones> {
    /*@Query(value = """
            SELECT pon_tecnica
    FROM ponderaciones
    WHERE id = 1;
    """, nativeQuery = true)
    Integer findPonTecnica();

    @Query(value = """
            SELECT pon_disponibilidad
    FROM ponderaciones
    WHERE id = 1;
    """, nativeQuery = true)
    Integer findPonDisponibilidad();

    @Query(value = """
            SELECT pon_oportunidad
    FROM ponderaciones
    WHERE id = 1;
    """, nativeQuery = true)
    Integer findPonOportunidad();*/

    Optional<Ponderaciones> findById(int id);
}
