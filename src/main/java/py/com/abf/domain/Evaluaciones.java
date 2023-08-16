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

/**
 * A Evaluaciones.
 */
@Entity
@Table(name = "evaluaciones")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evaluaciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nro_evaluacion", nullable = false)
    private Integer nroEvaluacion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "evaluaciones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluaciones", "temas" }, allowSetters = true)
    private Set<EvaluacionesDetalle> evaluacionesDetalles = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluaciones", "matriculas", "prestamos", "tipoDocumentos" }, allowSetters = true)
    private Alumnos alumnos;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluaciones", "pagos", "registroClases", "tipoDocumentos" }, allowSetters = true)
    private Funcionarios funcionarios;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaluaciones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNroEvaluacion() {
        return this.nroEvaluacion;
    }

    public Evaluaciones nroEvaluacion(Integer nroEvaluacion) {
        this.setNroEvaluacion(nroEvaluacion);
        return this;
    }

    public void setNroEvaluacion(Integer nroEvaluacion) {
        this.nroEvaluacion = nroEvaluacion;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Evaluaciones fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Set<EvaluacionesDetalle> getEvaluacionesDetalles() {
        return this.evaluacionesDetalles;
    }

    public void setEvaluacionesDetalles(Set<EvaluacionesDetalle> evaluacionesDetalles) {
        if (this.evaluacionesDetalles != null) {
            this.evaluacionesDetalles.forEach(i -> i.setEvaluaciones(null));
        }
        if (evaluacionesDetalles != null) {
            evaluacionesDetalles.forEach(i -> i.setEvaluaciones(this));
        }
        this.evaluacionesDetalles = evaluacionesDetalles;
    }

    public Evaluaciones evaluacionesDetalles(Set<EvaluacionesDetalle> evaluacionesDetalles) {
        this.setEvaluacionesDetalles(evaluacionesDetalles);
        return this;
    }

    public Evaluaciones addEvaluacionesDetalle(EvaluacionesDetalle evaluacionesDetalle) {
        this.evaluacionesDetalles.add(evaluacionesDetalle);
        evaluacionesDetalle.setEvaluaciones(this);
        return this;
    }

    public Evaluaciones removeEvaluacionesDetalle(EvaluacionesDetalle evaluacionesDetalle) {
        this.evaluacionesDetalles.remove(evaluacionesDetalle);
        evaluacionesDetalle.setEvaluaciones(null);
        return this;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public Evaluaciones alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public Funcionarios getFuncionarios() {
        return this.funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Evaluaciones funcionarios(Funcionarios funcionarios) {
        this.setFuncionarios(funcionarios);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaluaciones)) {
            return false;
        }
        return id != null && id.equals(((Evaluaciones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaluaciones{" +
            "id=" + getId() +
            ", nroEvaluacion=" + getNroEvaluacion() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
