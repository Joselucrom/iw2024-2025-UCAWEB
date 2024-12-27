package uca.es.iw.data;

import jakarta.persistence.*;

@Entity
public class Ponderaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pon_tecnica", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double ponTecnica;

    @Column(name = "pon_disponibilidad", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double ponDisponibilidad;

    @Column(name = "pon_oportunidad", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double ponOportunidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPonTecnica() {
        return ponTecnica;
    }

    public void setPonTecnica(Double ponTecnica) {
        this.ponTecnica = ponTecnica;
    }

    public Double getPonDisponibilidad() {
        return ponDisponibilidad;
    }

    public void setPonDisponibilidad(Double ponDisponibilidad) {
        this.ponDisponibilidad = ponDisponibilidad;
    }

    public Double getPonOportunidad() {
        return ponOportunidad;
    }

    public void setPonOportunidad(Double ponOportunidad) {
        this.ponOportunidad = ponOportunidad;
    }
}
