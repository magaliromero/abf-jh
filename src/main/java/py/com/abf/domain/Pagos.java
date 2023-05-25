package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total;

    @NotNull
    @Column(name = "cantidad_horas", nullable = false)
    private Integer cantidadHoras;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pagos", "facturaDetalles" }, allowSetters = true)
    private Productos producto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "evaluaciones", "pagos", "registroClases", "tipoDocumentos" }, allowSetters = true)
    private Funcionarios funcionario;

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

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Pagos fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Pagos total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCantidadHoras() {
        return this.cantidadHoras;
    }

    public Pagos cantidadHoras(Integer cantidadHoras) {
        this.setCantidadHoras(cantidadHoras);
        return this;
    }

    public void setCantidadHoras(Integer cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public Productos getProducto() {
        return this.producto;
    }

    public void setProducto(Productos productos) {
        this.producto = productos;
    }

    public Pagos producto(Productos productos) {
        this.setProducto(productos);
        return this;
    }

    public Funcionarios getFuncionario() {
        return this.funcionario;
    }

    public void setFuncionario(Funcionarios funcionarios) {
        this.funcionario = funcionarios;
    }

    public Pagos funcionario(Funcionarios funcionarios) {
        this.setFuncionario(funcionarios);
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
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", cantidadHoras=" + getCantidadHoras() +
            "}";
    }
}
