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
import py.com.abf.domain.enumeration.CondVenta;

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
    private Integer ruc;

    @Enumerated(EnumType.STRING)
    @Column(name = "condicion_venta")
    private CondVenta condicionVenta;

    @Column(name = "cantidad")
    private Integer cantidad;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "precio_unitario")
    private Integer precioUnitario;

    @Column(name = "valor_5")
    private Integer valor5;

    @Column(name = "valor_10")
    private Integer valor10;

    @Column(name = "total")
    private Integer total;

    @Column(name = "total_5")
    private Integer total5;

    @Column(name = "total_10")
    private Integer total10;

    @Column(name = "total_iva")
    private Integer totalIva;

    @OneToMany(mappedBy = "factura")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alumnos", "factura" }, allowSetters = true)
    private Set<Cobros> cobros = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "matriculas", "registroClases", "cobros", "evaluaciones", "inscripciones", "fichaPartidasTorneos", "facturas", "tipoDocumentos",
        },
        allowSetters = true
    )
    private Alumnos alumnos;

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

    public Integer getRuc() {
        return this.ruc;
    }

    public Facturas ruc(Integer ruc) {
        this.setRuc(ruc);
        return this;
    }

    public void setRuc(Integer ruc) {
        this.ruc = ruc;
    }

    public CondVenta getCondicionVenta() {
        return this.condicionVenta;
    }

    public Facturas condicionVenta(CondVenta condicionVenta) {
        this.setCondicionVenta(condicionVenta);
        return this;
    }

    public void setCondicionVenta(CondVenta condicionVenta) {
        this.condicionVenta = condicionVenta;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Facturas cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Facturas descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPrecioUnitario() {
        return this.precioUnitario;
    }

    public Facturas precioUnitario(Integer precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(Integer precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Integer getValor5() {
        return this.valor5;
    }

    public Facturas valor5(Integer valor5) {
        this.setValor5(valor5);
        return this;
    }

    public void setValor5(Integer valor5) {
        this.valor5 = valor5;
    }

    public Integer getValor10() {
        return this.valor10;
    }

    public Facturas valor10(Integer valor10) {
        this.setValor10(valor10);
        return this;
    }

    public void setValor10(Integer valor10) {
        this.valor10 = valor10;
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

    public Integer getTotal5() {
        return this.total5;
    }

    public Facturas total5(Integer total5) {
        this.setTotal5(total5);
        return this;
    }

    public void setTotal5(Integer total5) {
        this.total5 = total5;
    }

    public Integer getTotal10() {
        return this.total10;
    }

    public Facturas total10(Integer total10) {
        this.setTotal10(total10);
        return this;
    }

    public void setTotal10(Integer total10) {
        this.total10 = total10;
    }

    public Integer getTotalIva() {
        return this.totalIva;
    }

    public Facturas totalIva(Integer totalIva) {
        this.setTotalIva(totalIva);
        return this;
    }

    public void setTotalIva(Integer totalIva) {
        this.totalIva = totalIva;
    }

    public Set<Cobros> getCobros() {
        return this.cobros;
    }

    public void setCobros(Set<Cobros> cobros) {
        if (this.cobros != null) {
            this.cobros.forEach(i -> i.setFactura(null));
        }
        if (cobros != null) {
            cobros.forEach(i -> i.setFactura(this));
        }
        this.cobros = cobros;
    }

    public Facturas cobros(Set<Cobros> cobros) {
        this.setCobros(cobros);
        return this;
    }

    public Facturas addCobros(Cobros cobros) {
        this.cobros.add(cobros);
        cobros.setFactura(this);
        return this;
    }

    public Facturas removeCobros(Cobros cobros) {
        this.cobros.remove(cobros);
        cobros.setFactura(null);
        return this;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public Facturas alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
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
            ", ruc=" + getRuc() +
            ", condicionVenta='" + getCondicionVenta() + "'" +
            ", cantidad=" + getCantidad() +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            ", valor5=" + getValor5() +
            ", valor10=" + getValor10() +
            ", total=" + getTotal() +
            ", total5=" + getTotal5() +
            ", total10=" + getTotal10() +
            ", totalIva=" + getTotalIva() +
            "}";
    }
}
