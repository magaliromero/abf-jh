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
import py.com.abf.domain.enumeration.Niveles;

/**
 * A Cursos.
 */
@Entity
@Table(name = "cursos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "cantidad_clases")
    private Integer cantidadClases;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private Niveles nivel;

    @OneToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alumnos", "cursos" }, allowSetters = true)
    private Set<Inscripciones> inscripciones = new HashSet<>();

    @OneToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "registroClases", "cursos" }, allowSetters = true)
    private Set<Temas> temas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cursos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCurso() {
        return this.nombreCurso;
    }

    public Cursos nombreCurso(String nombreCurso) {
        this.setNombreCurso(nombreCurso);
        return this;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Cursos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Cursos fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Cursos fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCantidadClases() {
        return this.cantidadClases;
    }

    public Cursos cantidadClases(Integer cantidadClases) {
        this.setCantidadClases(cantidadClases);
        return this;
    }

    public void setCantidadClases(Integer cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public Niveles getNivel() {
        return this.nivel;
    }

    public Cursos nivel(Niveles nivel) {
        this.setNivel(nivel);
        return this;
    }

    public void setNivel(Niveles nivel) {
        this.nivel = nivel;
    }

    public Set<Inscripciones> getInscripciones() {
        return this.inscripciones;
    }

    public void setInscripciones(Set<Inscripciones> inscripciones) {
        if (this.inscripciones != null) {
            this.inscripciones.forEach(i -> i.setCursos(null));
        }
        if (inscripciones != null) {
            inscripciones.forEach(i -> i.setCursos(this));
        }
        this.inscripciones = inscripciones;
    }

    public Cursos inscripciones(Set<Inscripciones> inscripciones) {
        this.setInscripciones(inscripciones);
        return this;
    }

    public Cursos addInscripciones(Inscripciones inscripciones) {
        this.inscripciones.add(inscripciones);
        inscripciones.setCursos(this);
        return this;
    }

    public Cursos removeInscripciones(Inscripciones inscripciones) {
        this.inscripciones.remove(inscripciones);
        inscripciones.setCursos(null);
        return this;
    }

    public Set<Temas> getTemas() {
        return this.temas;
    }

    public void setTemas(Set<Temas> temas) {
        if (this.temas != null) {
            this.temas.forEach(i -> i.setCursos(null));
        }
        if (temas != null) {
            temas.forEach(i -> i.setCursos(this));
        }
        this.temas = temas;
    }

    public Cursos temas(Set<Temas> temas) {
        this.setTemas(temas);
        return this;
    }

    public Cursos addTemas(Temas temas) {
        this.temas.add(temas);
        temas.setCursos(this);
        return this;
    }

    public Cursos removeTemas(Temas temas) {
        this.temas.remove(temas);
        temas.setCursos(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cursos)) {
            return false;
        }
        return id != null && id.equals(((Cursos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cursos{" +
            "id=" + getId() +
            ", nombreCurso='" + getNombreCurso() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", cantidadClases=" + getCantidadClases() +
            ", nivel='" + getNivel() + "'" +
            "}";
    }
}
