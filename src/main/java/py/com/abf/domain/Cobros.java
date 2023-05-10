package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.TiposPagos;

/**
 * A Cobros.
 */
@Entity
@Table(name = "cobros")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cobros implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "monto_pago", nullable = false)
    private Integer montoPago;

    @NotNull
    @Column(name = "monto_inicial", nullable = false)
    private Integer montoInicial;

    @NotNull
    @Column(name = "saldo", nullable = false)
    private Integer saldo;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TiposPagos tipoPago;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "matriculas", "registroClases", "cobros", "evaluaciones", "inscripciones", "fichaPartidasTorneos", "facturas", "tipoDocumentos",
        },
        allowSetters = true
    )
    private Alumnos alumnos;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cobros", "alumnos" }, allowSetters = true)
    private Facturas factura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cobros id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMontoPago() {
        return this.montoPago;
    }

    public Cobros montoPago(Integer montoPago) {
        this.setMontoPago(montoPago);
        return this;
    }

    public void setMontoPago(Integer montoPago) {
        this.montoPago = montoPago;
    }

    public Integer getMontoInicial() {
        return this.montoInicial;
    }

    public Cobros montoInicial(Integer montoInicial) {
        this.setMontoInicial(montoInicial);
        return this;
    }

    public void setMontoInicial(Integer montoInicial) {
        this.montoInicial = montoInicial;
    }

    public Integer getSaldo() {
        return this.saldo;
    }

    public Cobros saldo(Integer saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Cobros fechaRegistro(LocalDate fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaPago() {
        return this.fechaPago;
    }

    public Cobros fechaPago(LocalDate fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public TiposPagos getTipoPago() {
        return this.tipoPago;
    }

    public Cobros tipoPago(TiposPagos tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(TiposPagos tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Cobros descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public Cobros alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public Facturas getFactura() {
        return this.factura;
    }

    public void setFactura(Facturas facturas) {
        this.factura = facturas;
    }

    public Cobros factura(Facturas facturas) {
        this.setFactura(facturas);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cobros)) {
            return false;
        }
        return id != null && id.equals(((Cobros) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cobros{" +
            "id=" + getId() +
            ", montoPago=" + getMontoPago() +
            ", montoInicial=" + getMontoInicial() +
            ", saldo=" + getSaldo() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", fechaPago='" + getFechaPago() + "'" +
            ", tipoPago='" + getTipoPago() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
