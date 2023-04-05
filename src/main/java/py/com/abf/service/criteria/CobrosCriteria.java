package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.TiposPagos;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Cobros} entity. This class is used
 * in {@link py.com.abf.web.rest.CobrosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cobros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CobrosCriteria implements Serializable, Criteria {

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

    private LongFilter alumnosId;

    private Boolean distinct;

    public CobrosCriteria() {}

    public CobrosCriteria(CobrosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montoPago = other.montoPago == null ? null : other.montoPago.copy();
        this.montoInicial = other.montoInicial == null ? null : other.montoInicial.copy();
        this.saldo = other.saldo == null ? null : other.saldo.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.fechaPago = other.fechaPago == null ? null : other.fechaPago.copy();
        this.tipoPago = other.tipoPago == null ? null : other.tipoPago.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CobrosCriteria copy() {
        return new CobrosCriteria(this);
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

    public LongFilter getAlumnosId() {
        return alumnosId;
    }

    public LongFilter alumnosId() {
        if (alumnosId == null) {
            alumnosId = new LongFilter();
        }
        return alumnosId;
    }

    public void setAlumnosId(LongFilter alumnosId) {
        this.alumnosId = alumnosId;
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
        final CobrosCriteria that = (CobrosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(montoPago, that.montoPago) &&
            Objects.equals(montoInicial, that.montoInicial) &&
            Objects.equals(saldo, that.saldo) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(fechaPago, that.fechaPago) &&
            Objects.equals(tipoPago, that.tipoPago) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, montoPago, montoInicial, saldo, fechaRegistro, fechaPago, tipoPago, descripcion, alumnosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CobrosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (montoPago != null ? "montoPago=" + montoPago + ", " : "") +
            (montoInicial != null ? "montoInicial=" + montoInicial + ", " : "") +
            (saldo != null ? "saldo=" + saldo + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (fechaPago != null ? "fechaPago=" + fechaPago + ", " : "") +
            (tipoPago != null ? "tipoPago=" + tipoPago + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
