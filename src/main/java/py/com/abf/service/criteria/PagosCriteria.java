package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Pagos} entity. This class is used
 * in {@link py.com.abf.web.rest.PagosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pagos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private IntegerFilter total;

    private IntegerFilter cantidadHoras;

    private LongFilter productoId;

    private LongFilter funcionarioId;

    private Boolean distinct;

    public PagosCriteria() {}

    public PagosCriteria(PagosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.cantidadHoras = other.cantidadHoras == null ? null : other.cantidadHoras.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
        this.funcionarioId = other.funcionarioId == null ? null : other.funcionarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PagosCriteria copy() {
        return new PagosCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            fecha = new LocalDateFilter();
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public IntegerFilter getTotal() {
        return total;
    }

    public IntegerFilter total() {
        if (total == null) {
            total = new IntegerFilter();
        }
        return total;
    }

    public void setTotal(IntegerFilter total) {
        this.total = total;
    }

    public IntegerFilter getCantidadHoras() {
        return cantidadHoras;
    }

    public IntegerFilter cantidadHoras() {
        if (cantidadHoras == null) {
            cantidadHoras = new IntegerFilter();
        }
        return cantidadHoras;
    }

    public void setCantidadHoras(IntegerFilter cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public LongFilter productoId() {
        if (productoId == null) {
            productoId = new LongFilter();
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }

    public LongFilter getFuncionarioId() {
        return funcionarioId;
    }

    public LongFilter funcionarioId() {
        if (funcionarioId == null) {
            funcionarioId = new LongFilter();
        }
        return funcionarioId;
    }

    public void setFuncionarioId(LongFilter funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PagosCriteria that = (PagosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(total, that.total) &&
            Objects.equals(cantidadHoras, that.cantidadHoras) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(funcionarioId, that.funcionarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, total, cantidadHoras, productoId, funcionarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (cantidadHoras != null ? "cantidadHoras=" + cantidadHoras + ", " : "") +
            (productoId != null ? "productoId=" + productoId + ", " : "") +
            (funcionarioId != null ? "funcionarioId=" + funcionarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
