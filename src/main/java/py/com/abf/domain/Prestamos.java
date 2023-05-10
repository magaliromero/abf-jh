package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @NotNull
    @Column(name = "vigencia_prestamo", nullable = false)
    private Integer vigenciaPrestamo;

    @NotNull
    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDate fechaDevolucion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "prestamos" }, allowSetters = true)
    private Materiales materiales;

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

    public Integer getVigenciaPrestamo() {
        return this.vigenciaPrestamo;
    }

    public Prestamos vigenciaPrestamo(Integer vigenciaPrestamo) {
        this.setVigenciaPrestamo(vigenciaPrestamo);
        return this;
    }

    public void setVigenciaPrestamo(Integer vigenciaPrestamo) {
        this.vigenciaPrestamo = vigenciaPrestamo;
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
            ", vigenciaPrestamo=" + getVigenciaPrestamo() +
            ", fechaDevolucion='" + getFechaDevolucion() + "'" +
            "}";
    }
}
