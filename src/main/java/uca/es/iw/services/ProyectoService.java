package uca.es.iw.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;

@Service
public class ProyectoService {

    private final DataSource dataSource;

    public ProyectoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void guardarProyecto(String titulo, String descripcion, String justificacion, String metas,
                                String impacto, String alcance, String reqFuncionales, String reqNoFuncionales,
                                String comentarios) {
        String sql = "INSERT INTO proyectos (titulo_proyecto, descripcion_proyecto, justificacion_proyecto, metas_objetivo, "
                + "impacto_esperado, alcance_proyecto, requisitos_funcionales, requisitos_no_funcionales, comentarios_adicionales) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setString(3, justificacion);
            stmt.setString(4, metas);
            stmt.setString(5, impacto);
            stmt.setString(6, alcance);
            stmt.setString(7, reqFuncionales);
            stmt.setString(8, reqNoFuncionales);
            stmt.setString(9, comentarios);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
