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
 * A Pagos.
 */
@Entity
@Table(name = "pagos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pagos implements Serializable {

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
    @JsonIgnoreProperties(value = { "registroClases", "pagos", "tipoDocumentos" }, allowSetters = true)
    private Funcionarios funcionarios;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pagos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMontoPago() {
        return this.montoPago;
    }

    public Pagos montoPago(Integer montoPago) {
        this.setMontoPago(montoPago);
        return this;
    }

    public void setMontoPago(Integer montoPago) {
        this.montoPago = montoPago;
    }

    public Integer getMontoInicial() {
        return this.montoInicial;
    }

    public Pagos montoInicial(Integer montoInicial) {
        this.setMontoInicial(montoInicial);
        return this;
    }

    public void setMontoInicial(Integer montoInicial) {
        this.montoInicial = montoInicial;
    }

    public Integer getSaldo() {
        return this.saldo;
    }

    public Pagos saldo(Integer saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Pagos fechaRegistro(LocalDate fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaPago() {
        return this.fechaPago;
    }

    public Pagos fechaPago(LocalDate fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public TiposPagos getTipoPago() {
        return this.tipoPago;
    }

    public Pagos tipoPago(TiposPagos tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(TiposPagos tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Pagos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Funcionarios getFuncionarios() {
        return this.funcionarios;
    }

    public void setFuncionarios(Funcionarios funcionarios) {
        this.funcionarios = funcionarios;
    }

    public Pagos funcionarios(Funcionarios funcionarios) {
        this.setFuncionarios(funcionarios);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pagos)) {
            return false;
        }
        return id != null && id.equals(((Pagos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pagos{" +
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
