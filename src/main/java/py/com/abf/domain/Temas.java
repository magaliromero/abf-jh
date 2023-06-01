package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Temas.
 */
@Entity
@Table(name = "temas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Temas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "temas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluaciones", "temas" }, allowSetters = true)
    private Set<EvaluacionesDetalle> evaluacionesDetalles = new HashSet<>();

    @OneToMany(mappedBy = "temas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "temas", "funcionario", "alumnos" }, allowSetters = true)
    private Set<RegistroClases> registroClases = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "inscripciones", "temas" }, allowSetters = true)
    private Cursos cursos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Temas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Temas titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Temas descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<EvaluacionesDetalle> getEvaluacionesDetalles() {
        return this.evaluacionesDetalles;
    }

    public void setEvaluacionesDetalles(Set<EvaluacionesDetalle> evaluacionesDetalles) {
        if (this.evaluacionesDetalles != null) {
            this.evaluacionesDetalles.forEach(i -> i.setTemas(null));
        }
        if (evaluacionesDetalles != null) {
            evaluacionesDetalles.forEach(i -> i.setTemas(this));
        }
        this.evaluacionesDetalles = evaluacionesDetalles;
    }

    public Temas evaluacionesDetalles(Set<EvaluacionesDetalle> evaluacionesDetalles) {
        this.setEvaluacionesDetalles(evaluacionesDetalles);
        return this;
    }

    public Temas addEvaluacionesDetalle(EvaluacionesDetalle evaluacionesDetalle) {
        this.evaluacionesDetalles.add(evaluacionesDetalle);
        evaluacionesDetalle.setTemas(this);
        return this;
    }

    public Temas removeEvaluacionesDetalle(EvaluacionesDetalle evaluacionesDetalle) {
        this.evaluacionesDetalles.remove(evaluacionesDetalle);
        evaluacionesDetalle.setTemas(null);
        return this;
    }

    public Set<RegistroClases> getRegistroClases() {
        return this.registroClases;
    }

    public void setRegistroClases(Set<RegistroClases> registroClases) {
        if (this.registroClases != null) {
            this.registroClases.forEach(i -> i.setTemas(null));
        }
        if (registroClases != null) {
            registroClases.forEach(i -> i.setTemas(this));
        }
        this.registroClases = registroClases;
    }

    public Temas registroClases(Set<RegistroClases> registroClases) {
        this.setRegistroClases(registroClases);
        return this;
    }

    public Temas addRegistroClases(RegistroClases registroClases) {
        this.registroClases.add(registroClases);
        registroClases.setTemas(this);
        return this;
    }

    public Temas removeRegistroClases(RegistroClases registroClases) {
        this.registroClases.remove(registroClases);
        registroClases.setTemas(null);
        return this;
    }

    public Cursos getCursos() {
        return this.cursos;
    }

    public void setCursos(Cursos cursos) {
        this.cursos = cursos;
    }

    public Temas cursos(Cursos cursos) {
        this.setCursos(cursos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temas)) {
            return false;
        }
        return id != null && id.equals(((Temas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Temas{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
