package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.EstadosPagos;

/**
 * A Matricula.
 */
@Entity
@Table(name = "matricula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "concepto", nullable = false)
    private String concepto;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Integer monto;

    @NotNull
    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadosPagos estado;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "inscripciones", "evaluaciones", "matriculas", "prestamos", "registroClases", "tipoDocumentos" },
        allowSetters = true
    )
    private Alumnos alumno;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matricula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConcepto() {
        return this.concepto;
    }

    public Matricula concepto(String concepto) {
        this.setConcepto(concepto);
        return this;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Integer getMonto() {
        return this.monto;
    }

    public Matricula monto(Integer monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public LocalDate getFechaInscripcion() {
        return this.fechaInscripcion;
    }

    public Matricula fechaInscripcion(LocalDate fechaInscripcion) {
        this.setFechaInscripcion(fechaInscripcion);
        return this;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Matricula fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaPago() {
        return this.fechaPago;
    }

    public Matricula fechaPago(LocalDate fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadosPagos getEstado() {
        return this.estado;
    }

    public Matricula estado(EstadosPagos estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadosPagos estado) {
        this.estado = estado;
    }

    public Alumnos getAlumno() {
        return this.alumno;
    }

    public void setAlumno(Alumnos alumnos) {
        this.alumno = alumnos;
    }

    public Matricula alumno(Alumnos alumnos) {
        this.setAlumno(alumnos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matricula)) {
            return false;
        }
        return id != null && id.equals(((Matricula) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matricula{" +
            "id=" + getId() +
            ", concepto='" + getConcepto() + "'" +
            ", monto=" + getMonto() +
            ", fechaInscripcion='" + getFechaInscripcion() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaPago='" + getFechaPago() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
