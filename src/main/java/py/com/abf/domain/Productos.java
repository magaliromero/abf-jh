package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.TipoProductos;

/**
 * A Productos.
 */
@Entity
@Table(name = "productos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    private TipoProductos tipoProducto;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;

    @NotNull
    @Column(name = "porcentaje_iva", nullable = false)
    private Integer porcentajeIva;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "funcionario" }, allowSetters = true)
    private Set<Pagos> pagos = new HashSet<>();

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "factura" }, allowSetters = true)
    private Set<FacturaDetalle> facturaDetalles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Productos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProductos getTipoProducto() {
        return this.tipoProducto;
    }

    public Productos tipoProducto(TipoProductos tipoProducto) {
        this.setTipoProducto(tipoProducto);
        return this;
    }

    public void setTipoProducto(TipoProductos tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public Integer getPrecioUnitario() {
        return this.precioUnitario;
    }

    public Productos precioUnitario(Integer precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Integer precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getPorcentajeIva() {
        return this.porcentajeIva;
    }

    public Productos porcentajeIva(Integer porcentajeIva) {
        this.setPorcentajeIva(porcentajeIva);
        return this;
    }

    public void setPorcentajeIva(Integer porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Productos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Pagos> getPagos() {
        return this.pagos;
    }

    public void setPagos(Set<Pagos> pagos) {
        if (this.pagos != null) {
            this.pagos.forEach(i -> i.setProducto(null));
        }
        if (pagos != null) {
            pagos.forEach(i -> i.setProducto(this));
        }
        this.pagos = pagos;
    }

    public Productos pagos(Set<Pagos> pagos) {
        this.setPagos(pagos);
        return this;
    }

    public Productos addPagos(Pagos pagos) {
        this.pagos.add(pagos);
        pagos.setProducto(this);
        return this;
    }

    public Productos removePagos(Pagos pagos) {
        this.pagos.remove(pagos);
        pagos.setProducto(null);
        return this;
    }

    public Set<FacturaDetalle> getFacturaDetalles() {
        return this.facturaDetalles;
    }

    public void setFacturaDetalles(Set<FacturaDetalle> facturaDetalles) {
        if (this.facturaDetalles != null) {
            this.facturaDetalles.forEach(i -> i.setProducto(null));
        }
        if (facturaDetalles != null) {
            facturaDetalles.forEach(i -> i.setProducto(this));
        }
        this.facturaDetalles = facturaDetalles;
    }

    public Productos facturaDetalles(Set<FacturaDetalle> facturaDetalles) {
        this.setFacturaDetalles(facturaDetalles);
        return this;
    }

    public Productos addFacturaDetalle(FacturaDetalle facturaDetalle) {
        this.facturaDetalles.add(facturaDetalle);
        facturaDetalle.setProducto(this);
        return this;
    }

    public Productos removeFacturaDetalle(FacturaDetalle facturaDetalle) {
        this.facturaDetalles.remove(facturaDetalle);
        facturaDetalle.setProducto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Productos)) {
            return false;
        }
        return id != null && id.equals(((Productos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Productos{" +
            "id=" + getId() +
            ", tipoProducto='" + getTipoProducto() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            ", porcentajeIva=" + getPorcentajeIva() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
