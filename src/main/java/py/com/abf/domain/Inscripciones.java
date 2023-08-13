package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscripciones.
 */
@Entity
@Table(name = "inscripcion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inscripciones implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "inscripciones", "evaluaciones", "matriculas", "prestamos", "registroClases", "tipoDocumentos" },
        allowSetters = true
    )
    private Alumnos alumnos;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "inscripciones", "temas" }, allowSetters = true)
    private Cursos cursos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscripciones id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInscripcion() {
        return this.fechaInscripcion;
    }

    public Inscripciones fechaInscripcion(LocalDate fechaInscripcion) {
        this.setFechaInscripcion(fechaInscripcion);
        return this;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public Inscripciones alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public Cursos getCursos() {
        return this.cursos;
    }

    public void setCursos(Cursos cursos) {
        this.cursos = cursos;
    }

    public Inscripciones cursos(Cursos cursos) {
        this.setCursos(cursos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscripciones)) {
            return false;
        }
        return id != null && id.equals(((Inscripciones) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscripciones{" +
            "id=" + getId() +
            ", fechaInscripcion='" + getFechaInscripcion() + "'" +
            "}";
    }
}
