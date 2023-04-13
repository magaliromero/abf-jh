package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.TiposEvaluaciones;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evaluacion", nullable = false)
    private TiposEvaluaciones tipoEvaluacion;

    @Column(name = "id_examen")
    private Integer idExamen;

    @Column(name = "id_acta")
    private Integer idActa;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "puntos_logrados")
    private Integer puntosLogrados;

    @Column(name = "porcentaje")
    private Integer porcentaje;

    @Column(name = "comentarios")
    private String comentarios;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "matriculas", "registroClases", "cobros", "evaluaciones", "inscripciones", "tipoDocumentos" },
        allowSetters = true
    )
    private Alumnos alumnos;

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

    public TiposEvaluaciones getTipoEvaluacion() {
        return this.tipoEvaluacion;
    }

    public Evaluaciones tipoEvaluacion(TiposEvaluaciones tipoEvaluacion) {
        this.setTipoEvaluacion(tipoEvaluacion);
        return this;
    }

    public void setTipoEvaluacion(TiposEvaluaciones tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public Integer getIdExamen() {
        return this.idExamen;
    }

    public Evaluaciones idExamen(Integer idExamen) {
        this.setIdExamen(idExamen);
        return this;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public Integer getIdActa() {
        return this.idActa;
    }

    public Evaluaciones idActa(Integer idActa) {
        this.setIdActa(idActa);
        return this;
    }

    public void setIdActa(Integer idActa) {
        this.idActa = idActa;
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

    public Integer getPuntosLogrados() {
        return this.puntosLogrados;
    }

    public Evaluaciones puntosLogrados(Integer puntosLogrados) {
        this.setPuntosLogrados(puntosLogrados);
        return this;
    }

    public void setPuntosLogrados(Integer puntosLogrados) {
        this.puntosLogrados = puntosLogrados;
    }

    public Integer getPorcentaje() {
        return this.porcentaje;
    }

    public Evaluaciones porcentaje(Integer porcentaje) {
        this.setPorcentaje(porcentaje);
        return this;
    }

    public void setPorcentaje(Integer porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public Evaluaciones comentarios(String comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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
            ", tipoEvaluacion='" + getTipoEvaluacion() + "'" +
            ", idExamen=" + getIdExamen() +
            ", idActa=" + getIdActa() +
            ", fecha='" + getFecha() + "'" +
            ", puntosLogrados=" + getPuntosLogrados() +
            ", porcentaje=" + getPorcentaje() +
            ", comentarios='" + getComentarios() + "'" +
            "}";
    }
}
