package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.TiposTorneos;

/**
 * A Torneos.
 */
@Entity
@Table(name = "torneos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Torneos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_torneo", nullable = false)
    private String nombreTorneo;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Column(name = "lugar", nullable = false)
    private String lugar;

    @Column(name = "tiempo")
    private String tiempo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_torneo", nullable = false)
    private TiposTorneos tipoTorneo;

    @NotNull
    @Column(name = "torneo_evaluado", nullable = false)
    private Boolean torneoEvaluado;

    @NotNull
    @Column(name = "federado", nullable = false)
    private Boolean federado;

    @OneToMany(mappedBy = "torneos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "torneos", "alumnos" }, allowSetters = true)
    private Set<FichaPartidasTorneos> fichaPartidasTorneos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Torneos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTorneo() {
        return this.nombreTorneo;
    }

    public Torneos nombreTorneo(String nombreTorneo) {
        this.setNombreTorneo(nombreTorneo);
        return this;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Torneos fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Torneos fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return this.lugar;
    }

    public Torneos lugar(String lugar) {
        this.setLugar(lugar);
        return this;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getTiempo() {
        return this.tiempo;
    }

    public Torneos tiempo(String tiempo) {
        this.setTiempo(tiempo);
        return this;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public TiposTorneos getTipoTorneo() {
        return this.tipoTorneo;
    }

    public Torneos tipoTorneo(TiposTorneos tipoTorneo) {
        this.setTipoTorneo(tipoTorneo);
        return this;
    }

    public void setTipoTorneo(TiposTorneos tipoTorneo) {
        this.tipoTorneo = tipoTorneo;
    }

    public Boolean getTorneoEvaluado() {
        return this.torneoEvaluado;
    }

    public Torneos torneoEvaluado(Boolean torneoEvaluado) {
        this.setTorneoEvaluado(torneoEvaluado);
        return this;
    }

    public void setTorneoEvaluado(Boolean torneoEvaluado) {
        this.torneoEvaluado = torneoEvaluado;
    }

    public Boolean getFederado() {
        return this.federado;
    }

    public Torneos federado(Boolean federado) {
        this.setFederado(federado);
        return this;
    }

    public void setFederado(Boolean federado) {
        this.federado = federado;
    }

    public Set<FichaPartidasTorneos> getFichaPartidasTorneos() {
        return this.fichaPartidasTorneos;
    }

    public void setFichaPartidasTorneos(Set<FichaPartidasTorneos> fichaPartidasTorneos) {
        if (this.fichaPartidasTorneos != null) {
            this.fichaPartidasTorneos.forEach(i -> i.setTorneos(null));
        }
        if (fichaPartidasTorneos != null) {
            fichaPartidasTorneos.forEach(i -> i.setTorneos(this));
        }
        this.fichaPartidasTorneos = fichaPartidasTorneos;
    }

    public Torneos fichaPartidasTorneos(Set<FichaPartidasTorneos> fichaPartidasTorneos) {
        this.setFichaPartidasTorneos(fichaPartidasTorneos);
        return this;
    }

    public Torneos addFichaPartidasTorneos(FichaPartidasTorneos fichaPartidasTorneos) {
        this.fichaPartidasTorneos.add(fichaPartidasTorneos);
        fichaPartidasTorneos.setTorneos(this);
        return this;
    }

    public Torneos removeFichaPartidasTorneos(FichaPartidasTorneos fichaPartidasTorneos) {
        this.fichaPartidasTorneos.remove(fichaPartidasTorneos);
        fichaPartidasTorneos.setTorneos(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Torneos)) {
            return false;
        }
        return id != null && id.equals(((Torneos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Torneos{" +
            "id=" + getId() +
            ", nombreTorneo='" + getNombreTorneo() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", lugar='" + getLugar() + "'" +
            ", tiempo='" + getTiempo() + "'" +
            ", tipoTorneo='" + getTipoTorneo() + "'" +
            ", torneoEvaluado='" + getTorneoEvaluado() + "'" +
            ", federado='" + getFederado() + "'" +
            "}";
    }
}
