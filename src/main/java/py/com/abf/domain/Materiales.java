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
 * A Materiales.
 */
@Entity
@Table(name = "materiales")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materiales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "cantidad_en_prestamo")
    private Integer cantidadEnPrestamo;

    @OneToMany(mappedBy = "materiales")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materiales", "alumnos" }, allowSetters = true)
    private Set<Prestamos> prestamos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materiales id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Materiales descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Materiales cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadEnPrestamo() {
        return this.cantidadEnPrestamo;
    }

    public Materiales cantidadEnPrestamo(Integer cantidadEnPrestamo) {
        this.setCantidadEnPrestamo(cantidadEnPrestamo);
        return this;
    }

    public void setCantidadEnPrestamo(Integer cantidadEnPrestamo) {
        this.cantidadEnPrestamo = cantidadEnPrestamo;
    }

    public Set<Prestamos> getPrestamos() {
        return this.prestamos;
    }

    public void setPrestamos(Set<Prestamos> prestamos) {
        if (this.prestamos != null) {
            this.prestamos.forEach(i -> i.setMateriales(null));
        }
        if (prestamos != null) {
            prestamos.forEach(i -> i.setMateriales(this));
        }
        this.prestamos = prestamos;
    }

    public Materiales prestamos(Set<Prestamos> prestamos) {
        this.setPrestamos(prestamos);
        return this;
    }

    public Materiales addPrestamos(Prestamos prestamos) {
        this.prestamos.add(prestamos);
        prestamos.setMateriales(this);
        return this;
    }

    public Materiales removePrestamos(Prestamos prestamos) {
        this.prestamos.remove(prestamos);
        prestamos.setMateriales(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materiales)) {
            return false;
        }
        return id != null && id.equals(((Materiales) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materiales{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", cantidad=" + getCantidad() +
            ", cantidadEnPrestamo=" + getCantidadEnPrestamo() +
            "}";
    }
}
