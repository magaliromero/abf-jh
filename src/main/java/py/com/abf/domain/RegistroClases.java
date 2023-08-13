package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RegistroClases.
 */
@Entity
@Table(name = "registro_clases")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroClases implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "cantidad_horas", nullable = false)
    private Integer cantidadHoras;

    @Column(name = "asistencia_alumno")
    private Boolean asistenciaAlumno;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "registroClases", "cursos" }, allowSetters = true)
    private Temas temas;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluaciones", "pagos", "registroClases", "tipoDocumentos" }, allowSetters = true)
    private Funcionarios funcionario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "inscripciones", "evaluaciones", "matriculas", "prestamos", "registroClases", "tipoDocumentos" },
        allowSetters = true
    )
    private Alumnos alumnos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RegistroClases id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public RegistroClases fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidadHoras() {
        return this.cantidadHoras;
    }

    public RegistroClases cantidadHoras(Integer cantidadHoras) {
        this.setCantidadHoras(cantidadHoras);
        return this;
    }

    public void setCantidadHoras(Integer cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public Boolean getAsistenciaAlumno() {
        return this.asistenciaAlumno;
    }

    public RegistroClases asistenciaAlumno(Boolean asistenciaAlumno) {
        this.setAsistenciaAlumno(asistenciaAlumno);
        return this;
    }

    public void setAsistenciaAlumno(Boolean asistenciaAlumno) {
        this.asistenciaAlumno = asistenciaAlumno;
    }

    public Temas getTemas() {
        return this.temas;
    }

    public void setTemas(Temas temas) {
        this.temas = temas;
    }

    public RegistroClases temas(Temas temas) {
        this.setTemas(temas);
        return this;
    }

    public Funcionarios getFuncionario() {
        return this.funcionario;
    }

    public void setFuncionario(Funcionarios funcionarios) {
        this.funcionario = funcionarios;
    }

    public RegistroClases funcionario(Funcionarios funcionarios) {
        this.setFuncionario(funcionarios);
        return this;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public RegistroClases alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegistroClases)) {
            return false;
        }
        return id != null && id.equals(((RegistroClases) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroClases{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", cantidadHoras=" + getCantidadHoras() +
            ", asistenciaAlumno='" + getAsistenciaAlumno() + "'" +
            "}";
    }
}
