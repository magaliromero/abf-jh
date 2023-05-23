package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EvaluacionesDetalle.
 */
@Entity
@Table(name = "evaluaciones_detalle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluacionesDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "comentarios", nullable = false)
    private String comentarios;

    @NotNull
    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "alumnos", "funcionarios" }, allowSetters = true)
    private Evaluaciones evaluaciones;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "registroClases" }, allowSetters = true)
    private Temas temas;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EvaluacionesDetalle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public EvaluacionesDetalle comentarios(String comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getPuntaje() {
        return this.puntaje;
    }

    public EvaluacionesDetalle puntaje(Integer puntaje) {
        this.setPuntaje(puntaje);
        return this;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Evaluaciones getEvaluaciones() {
        return this.evaluaciones;
    }

    public void setEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public EvaluacionesDetalle evaluaciones(Evaluaciones evaluaciones) {
        this.setEvaluaciones(evaluaciones);
        return this;
    }

    public Temas getTemas() {
        return this.temas;
    }

    public void setTemas(Temas temas) {
        this.temas = temas;
    }

    public EvaluacionesDetalle temas(Temas temas) {
        this.setTemas(temas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluacionesDetalle)) {
            return false;
        }
        return id != null && id.equals(((EvaluacionesDetalle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluacionesDetalle{" +
            "id=" + getId() +
            ", comentarios='" + getComentarios() + "'" +
            ", puntaje=" + getPuntaje() +
            "}";
    }
}
