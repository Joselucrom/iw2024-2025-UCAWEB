package uca.es.iw.data;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProyectoId;

    @Column(name = "titulo_proyecto", nullable = false, length = 255)
    private String titulo;

    @Column(name = "descripcion_proyecto", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "justificacion_proyecto", nullable = false, columnDefinition = "TEXT")
    private String justificacion;

    @Column(name = "metas_objetivo", nullable = false, columnDefinition = "TEXT")
    private String metasObjetivo;

    @Column(name = "impacto_esperado", nullable = false, columnDefinition = "TEXT")
    private String impactoEsperado;

    @Column(name = "comentarios_adicionales", columnDefinition = "TEXT")
    private String ComentariosAdicionales;

    @Column(name = "alcance_proyecto", nullable = false, columnDefinition = "TEXT")
    private String alcance;

    @Column(name = "requisitos_funcionales", nullable = false, columnDefinition = "TEXT")
    private String requisitosFuncionales;

    @Column(name = "requisitos_no_funcionales", nullable = false, columnDefinition = "TEXT")
    private String requisitosNoFuncionales;

    @Column(name = "estado", nullable = false)
    private String estado; // Este campo es un String, pero representará un ENUM en la base de datos

    //@Enumerated(EnumType.STRING)
    //@ElementCollection(fetch = FetchType.EAGER)
    //private Set<Estado> estados;

    @Column(name = "nombre_solicitante", length = 100)
    private String nombreSolicitante;

    @Column(name = "correo_solicitante", length = 100)
    private String correoSolicitante;

    @Column(name = "rol_solicitante", length = 100)
    private String rolSolicitante;

    @Column(name = "departamento_solicitante", length = 100)
    private String departamentoSolicitante;

    @Column(name = "recursos_humanos", columnDefinition = "TEXT")
    private String recursosHumanos;

    @Column(name = "recursos_financieros_estimados", precision = 10, scale = 2)
    private BigDecimal recursosFinancierosEstimados;

    @Column(name = "recursos_tiempo_estimado", length = 50)
    private String recursosTiempoEstimado;

    @Column(name = "impacto_estrategico")
    private Integer impactoEstrategico; // Valores entre 1 y 10

    @Column(name = "alineacion_estrategica")
    private Integer alineacionEstrategica; // Valores entre 1 y 10

    @Column(name = "prioridad")
    private Integer prioridad; // Valores entre 1 y 10

    @Column(name = "idoneidad_tecnica")
    private Integer idoneidadTecnica; // Valores entre 1 y 10

    @Column(name = "idoneidad_financiera")
    private Integer idoneidadFinanciera; // Valores entre 1 y 10

    @Column(name = "idoneidad_temporal")
    private Integer idoneidadTemporal; // Valores entre 1 y 10

    //@Column(name = "fecha_solicitud", nullable = false)
    //@JsonFormat(pattern = "EEEE d 'de' MMMM 'de' yyyy")
    //private String fechaSolicitud; // Este campo almacena la fecha de solicitud

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;



    // Getters y Setters
    // O puedes generarlos automáticamente en tu IDE como IntelliJ


    public Long getId() {
        return ProyectoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public String getMetasObjetivo() {
        return metasObjetivo;
    }

    public String getImpactoEsperado() {
        return impactoEsperado;
    }

    public String getAlcance() {
        return alcance;
    }

    public String getRequisitosFuncionales() {
        return requisitosFuncionales;
    }

    public String getRequisitosNoFuncionales() {
        return requisitosNoFuncionales;
    }

    //public Set<Estado> getEstado() {
    //    return estados;
    //}

    public String getEstado() {
        return estado;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public String getCorreoSolicitante() {
        return correoSolicitante;
    }

    public String getRolSolicitante() {
        return rolSolicitante;
    }

    public String getDepartamentoSolicitante() {
        return departamentoSolicitante;
    }

    public String getRecursosHumanos() {
        return recursosHumanos;
    }

    public BigDecimal getRecursosFinancierosEstimados() {
        return recursosFinancierosEstimados;
    }

    public String getRecursosTiempoEstimado() {
        return recursosTiempoEstimado;
    }

    public Integer getImpactoEstrategico() {
        return impactoEstrategico;
    }

    public Integer getAlineacionEstrategica() {
        return alineacionEstrategica;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public Integer getIdoneidadTecnica() {
        return idoneidadTecnica;
    }

    public Integer getIdoneidadFinanciera() {
        return idoneidadFinanciera;
    }

    public Integer getIdoneidadTemporal() {
        return idoneidadTemporal;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setId(Long ProyectoId) {
        this.ProyectoId = ProyectoId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public void setMetasObjetivo(String metasObjetivo) {
        this.metasObjetivo = metasObjetivo;
    }

    public void setImpactoEsperado(String impactoEsperado) {
        this.impactoEsperado = impactoEsperado;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public void setRequisitosFuncionales(String requisitosFuncionales) {
        this.requisitosFuncionales = requisitosFuncionales;
    }

    public void setRequisitosNoFuncionales(String requisitosNoFuncionales) {
        this.requisitosNoFuncionales = requisitosNoFuncionales;
    }

    //public void setEstado(Set<Estado> estado) {
    //    this.estados = estado;
    //}

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public void setCorreoSolicitante(String correoSolicitante) {
        this.correoSolicitante = correoSolicitante;
    }

    public void setRolSolicitante(String rolSolicitante) {
        this.rolSolicitante = rolSolicitante;
    }

    public void setDepartamentoSolicitante(String departamentoSolicitante) {
        this.departamentoSolicitante = departamentoSolicitante;
    }

    public void setRecursosHumanos(String recursosHumanos) {
        this.recursosHumanos = recursosHumanos;
    }

    public void setRecursosFinancierosEstimados(BigDecimal recursosFinancierosEstimados) {
        this.recursosFinancierosEstimados = recursosFinancierosEstimados;
    }

    public void setRecursosTiempoEstimado(String recursosTiempoEstimado) {
        this.recursosTiempoEstimado = recursosTiempoEstimado;
    }

    public void setImpactoEstrategico(Integer impactoEstrategico) {
        this.impactoEstrategico = impactoEstrategico;
    }

    public void setAlineacionEstrategica(Integer alineacionEstrategica) {
        this.alineacionEstrategica = alineacionEstrategica;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public void setIdoneidadTecnica(Integer idoneidadTecnica) {
        this.idoneidadTecnica = idoneidadTecnica;
    }

    public void setIdoneidadFinanciera(Integer idoneidadFinanciera) {
        this.idoneidadFinanciera = idoneidadFinanciera;
    }

    public void setIdoneidadTemporal(Integer idoneidadTemporal) {
        this.idoneidadTemporal = idoneidadTemporal;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getComentariosAdicionales() {
        return ComentariosAdicionales;
    }

    public void setComentariosAdicionales(String ComentariosAdicionales) {
        this.ComentariosAdicionales = ComentariosAdicionales;
    }
}
