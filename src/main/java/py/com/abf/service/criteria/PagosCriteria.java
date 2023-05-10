package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.TiposPagos;
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

    /**
     * Class for filtering TiposPagos
     */
    public static class TiposPagosFilter extends Filter<TiposPagos> {

        public TiposPagosFilter() {}

        public TiposPagosFilter(TiposPagosFilter filter) {
            super(filter);
        }

        @Override
        public TiposPagosFilter copy() {
            return new TiposPagosFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter montoPago;

    private IntegerFilter montoInicial;

    private IntegerFilter saldo;

    private LocalDateFilter fechaRegistro;

    private LocalDateFilter fechaPago;

    private TiposPagosFilter tipoPago;

    private StringFilter descripcion;

    private LongFilter funcionariosId;

    private Boolean distinct;

    public PagosCriteria() {}

    public PagosCriteria(PagosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montoPago = other.montoPago == null ? null : other.montoPago.copy();
        this.montoInicial = other.montoInicial == null ? null : other.montoInicial.copy();
        this.saldo = other.saldo == null ? null : other.saldo.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.fechaPago = other.fechaPago == null ? null : other.fechaPago.copy();
        this.tipoPago = other.tipoPago == null ? null : other.tipoPago.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.funcionariosId = other.funcionariosId == null ? null : other.funcionariosId.copy();
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

    public IntegerFilter getMontoPago() {
        return montoPago;
    }

    public IntegerFilter montoPago() {
        if (montoPago == null) {
            montoPago = new IntegerFilter();
        }
        return montoPago;
    }

    public void setMontoPago(IntegerFilter montoPago) {
        this.montoPago = montoPago;
    }

    public IntegerFilter getMontoInicial() {
        return montoInicial;
    }

    public IntegerFilter montoInicial() {
        if (montoInicial == null) {
            montoInicial = new IntegerFilter();
        }
        return montoInicial;
    }

    public void setMontoInicial(IntegerFilter montoInicial) {
        this.montoInicial = montoInicial;
    }

    public IntegerFilter getSaldo() {
        return saldo;
    }

    public IntegerFilter saldo() {
        if (saldo == null) {
            saldo = new IntegerFilter();
        }
        return saldo;
    }

    public void setSaldo(IntegerFilter saldo) {
        this.saldo = saldo;
    }

    public LocalDateFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateFilter fechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = new LocalDateFilter();
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateFilter getFechaPago() {
        return fechaPago;
    }

    public LocalDateFilter fechaPago() {
        if (fechaPago == null) {
            fechaPago = new LocalDateFilter();
        }
        return fechaPago;
    }

    public void setFechaPago(LocalDateFilter fechaPago) {
        this.fechaPago = fechaPago;
    }

    public TiposPagosFilter getTipoPago() {
        return tipoPago;
    }

    public TiposPagosFilter tipoPago() {
        if (tipoPago == null) {
            tipoPago = new TiposPagosFilter();
        }
        return tipoPago;
    }

    public void setTipoPago(TiposPagosFilter tipoPago) {
        this.tipoPago = tipoPago;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LongFilter getFuncionariosId() {
        return funcionariosId;
    }

    public LongFilter funcionariosId() {
        if (funcionariosId == null) {
            funcionariosId = new LongFilter();
        }
        return funcionariosId;
    }

    public void setFuncionariosId(LongFilter funcionariosId) {
        this.funcionariosId = funcionariosId;
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
            Objects.equals(montoPago, that.montoPago) &&
            Objects.equals(montoInicial, that.montoInicial) &&
            Objects.equals(saldo, that.saldo) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(fechaPago, that.fechaPago) &&
            Objects.equals(tipoPago, that.tipoPago) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(funcionariosId, that.funcionariosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, montoPago, montoInicial, saldo, fechaRegistro, fechaPago, tipoPago, descripcion, funcionariosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (montoPago != null ? "montoPago=" + montoPago + ", " : "") +
            (montoInicial != null ? "montoInicial=" + montoInicial + ", " : "") +
            (saldo != null ? "saldo=" + saldo + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (fechaPago != null ? "fechaPago=" + fechaPago + ", " : "") +
            (tipoPago != null ? "tipoPago=" + tipoPago + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (funcionariosId != null ? "funcionariosId=" + funcionariosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
