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
import py.com.abf.domain.enumeration.CondicionVenta;

/**
 * A Facturas.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Facturas implements Serializable {

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
    @Column(name = "factura_nro", nullable = false)
    private String facturaNro;

    @NotNull
    @Column(name = "timbrado", nullable = false)
    private Integer timbrado;

    @NotNull
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @NotNull
    @Column(name = "ruc", nullable = false)
    private String ruc;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_venta", nullable = false)
    private CondicionVenta condicionVenta;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total;

    @OneToMany(mappedBy = "factura")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "factura" }, allowSetters = true)
    private Set<FacturaDetalle> facturaDetalles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facturas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Facturas fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getFacturaNro() {
        return this.facturaNro;
    }

    public Facturas facturaNro(String facturaNro) {
        this.setFacturaNro(facturaNro);
        return this;
    }

    public void setFacturaNro(String facturaNro) {
        this.facturaNro = facturaNro;
    }

    public Integer getTimbrado() {
        return this.timbrado;
    }

    public Facturas timbrado(Integer timbrado) {
        this.setTimbrado(timbrado);
        return this;
    }

    public void setTimbrado(Integer timbrado) {
        this.timbrado = timbrado;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public Facturas razonSocial(String razonSocial) {
        this.setRazonSocial(razonSocial);
        return this;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return this.ruc;
    }

    public Facturas ruc(String ruc) {
        this.setRuc(ruc);
        return this;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public CondicionVenta getCondicionVenta() {
        return this.condicionVenta;
    }

    public Facturas condicionVenta(CondicionVenta condicionVenta) {
        this.setCondicionVenta(condicionVenta);
        return this;
    }

    public void setCondicionVenta(CondicionVenta condicionVenta) {
        this.condicionVenta = condicionVenta;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Facturas total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Set<FacturaDetalle> getFacturaDetalles() {
        return this.facturaDetalles;
    }

    public void setFacturaDetalles(Set<FacturaDetalle> facturaDetalles) {
        if (this.facturaDetalles != null) {
            this.facturaDetalles.forEach(i -> i.setFactura(null));
        }
        if (facturaDetalles != null) {
            facturaDetalles.forEach(i -> i.setFactura(this));
        }
        this.facturaDetalles = facturaDetalles;
    }

    public Facturas facturaDetalles(Set<FacturaDetalle> facturaDetalles) {
        this.setFacturaDetalles(facturaDetalles);
        return this;
    }

    public Facturas addFacturaDetalle(FacturaDetalle facturaDetalle) {
        this.facturaDetalles.add(facturaDetalle);
        facturaDetalle.setFactura(this);
        return this;
    }

    public Facturas removeFacturaDetalle(FacturaDetalle facturaDetalle) {
        this.facturaDetalles.remove(facturaDetalle);
        facturaDetalle.setFactura(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facturas)) {
            return false;
        }
        return id != null && id.equals(((Facturas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facturas{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", facturaNro='" + getFacturaNro() + "'" +
            ", timbrado=" + getTimbrado() +
            ", razonSocial='" + getRazonSocial() + "'" +
            ", ruc='" + getRuc() + "'" +
            ", condicionVenta='" + getCondicionVenta() + "'" +
            ", total=" + getTotal() +
            "}";
    }
}
