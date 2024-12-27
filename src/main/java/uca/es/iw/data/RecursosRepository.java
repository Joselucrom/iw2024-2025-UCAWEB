package uca.es.iw.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RecursosRepository extends JpaRepository<Recursos, Long>, JpaSpecificationExecutor<Recursos> {

    @Query(value = """
            SELECT presupuestoTotal
    FROM Recursos
    WHERE id = 1;
    """, nativeQuery = true)
    Double findPresupuestoTotal();

    @Query(value = """
            SELECT recursosHumanosTotal
    FROM Recursos
    WHERE id = 1;
    """, nativeQuery = true)
    Integer findRecursosHumanosTotal();

    @Query(value = """
            SELECT presupuestoRestante
    FROM Recursos
    WHERE id = 1;
    """, nativeQuery = true)
    Double findPresupuestoRestante();

    @Query(value = """
            SELECT recursosHumanosRestantes
    FROM Recursos
    WHERE id = 1;
    """, nativeQuery = true)
    Integer findRecursosHumanosRestantes();
}
