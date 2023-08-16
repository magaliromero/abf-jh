package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.EstadosPrestamos;

/**
 * A Prestamos.
 */
@Entity
@Table(name = "prestamos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prestamos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadosPrestamos estado;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "prestamos" }, allowSetters = true)
    private Materiales materiales;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluaciones", "matriculas", "prestamos", "tipoDocumentos" }, allowSetters = true)
    private Alumnos alumnos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prestamos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPrestamo() {
        return this.fechaPrestamo;
    }

    public Prestamos fechaPrestamo(LocalDate fechaPrestamo) {
        this.setFechaPrestamo(fechaPrestamo);
        return this;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    public Prestamos fechaDevolucion(LocalDate fechaDevolucion) {
        this.setFechaDevolucion(fechaDevolucion);
        return this;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public EstadosPrestamos getEstado() {
        return this.estado;
    }

    public Prestamos estado(EstadosPrestamos estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadosPrestamos estado) {
        this.estado = estado;
    }

    public Materiales getMateriales() {
        return this.materiales;
    }

    public void setMateriales(Materiales materiales) {
        this.materiales = materiales;
    }

    public Prestamos materiales(Materiales materiales) {
        this.setMateriales(materiales);
        return this;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public Prestamos alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestamos)) {
            return false;
        }
        return id != null && id.equals(((Prestamos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestamos{" +
            "id=" + getId() +
            ", fechaPrestamo='" + getFechaPrestamo() + "'" +
            ", fechaDevolucion='" + getFechaDevolucion() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
