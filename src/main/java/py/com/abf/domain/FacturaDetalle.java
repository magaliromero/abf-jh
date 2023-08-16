package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FacturaDetalle.
 */
@Entity
@Table(name = "factura_detalle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Integer precioUnitario;

    @Column(name = "subtotal")
    private Integer subtotal;

    @Column(name = "porcentaje_iva")
    private Integer porcentajeIva;

    @Column(name = "valor_porcentaje")
    private Integer valorPorcentaje;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pagos", "facturaDetalles" }, allowSetters = true)
    private Productos producto;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "facturaDetalles" }, allowSetters = true)
    private Facturas factura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FacturaDetalle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public FacturaDetalle cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPrecioUnitario() {
        return this.precioUnitario;
    }

    public FacturaDetalle precioUnitario(Integer precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Integer precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getSubtotal() {
        return this.subtotal;
    }

    public FacturaDetalle subtotal(Integer subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getPorcentajeIva() {
        return this.porcentajeIva;
    }

    public FacturaDetalle porcentajeIva(Integer porcentajeIva) {
        this.setPorcentajeIva(porcentajeIva);
        return this;
    }

    public void setPorcentajeIva(Integer porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public Integer getValorPorcentaje() {
        return this.valorPorcentaje;
    }

    public FacturaDetalle valorPorcentaje(Integer valorPorcentaje) {
        this.setValorPorcentaje(valorPorcentaje);
        return this;
    }

    public void setValorPorcentaje(Integer valorPorcentaje) {
        this.valorPorcentaje = valorPorcentaje;
    }

    public Productos getProducto() {
        return this.producto;
    }

    public void setProducto(Productos productos) {
        this.producto = productos;
    }

    public FacturaDetalle producto(Productos productos) {
        this.setProducto(productos);
        return this;
    }

    public Facturas getFactura() {
        return this.factura;
    }

    public void setFactura(Facturas facturas) {
        this.factura = facturas;
    }

    public FacturaDetalle factura(Facturas facturas) {
        this.setFactura(facturas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaDetalle)) {
            return false;
        }
        return id != null && id.equals(((FacturaDetalle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDetalle{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", precioUnitario=" + getPrecioUnitario() +
            ", subtotal=" + getSubtotal() +
            ", porcentajeIva=" + getPorcentajeIva() +
            ", valorPorcentaje=" + getValorPorcentaje() +
            "}";
    }
}
