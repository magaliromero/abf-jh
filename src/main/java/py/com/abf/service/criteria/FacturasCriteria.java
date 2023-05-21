package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.CondicionVenta;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Facturas} entity. This class is used
 * in {@link py.com.abf.web.rest.FacturasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturasCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CondicionVenta
     */
    public static class CondicionVentaFilter extends Filter<CondicionVenta> {

        public CondicionVentaFilter() {}

        public CondicionVentaFilter(CondicionVentaFilter filter) {
            super(filter);
        }

        @Override
        public CondicionVentaFilter copy() {
            return new CondicionVentaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private StringFilter facturaNro;

    private IntegerFilter timbrado;

    private StringFilter razonSocial;

    private StringFilter ruc;

    private CondicionVentaFilter condicionVenta;

    private IntegerFilter total;

    private LongFilter facturaDetalleId;

    private Boolean distinct;

    public FacturasCriteria() {}

    public FacturasCriteria(FacturasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.facturaNro = other.facturaNro == null ? null : other.facturaNro.copy();
        this.timbrado = other.timbrado == null ? null : other.timbrado.copy();
        this.razonSocial = other.razonSocial == null ? null : other.razonSocial.copy();
        this.ruc = other.ruc == null ? null : other.ruc.copy();
        this.condicionVenta = other.condicionVenta == null ? null : other.condicionVenta.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.facturaDetalleId = other.facturaDetalleId == null ? null : other.facturaDetalleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FacturasCriteria copy() {
        return new FacturasCriteria(this);
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

    public StringFilter getFacturaNro() {
        return facturaNro;
    }

    public StringFilter facturaNro() {
        if (facturaNro == null) {
            facturaNro = new StringFilter();
        }
        return facturaNro;
    }

    public void setFacturaNro(StringFilter facturaNro) {
        this.facturaNro = facturaNro;
    }

    public IntegerFilter getTimbrado() {
        return timbrado;
    }

    public IntegerFilter timbrado() {
        if (timbrado == null) {
            timbrado = new IntegerFilter();
        }
        return timbrado;
    }

    public void setTimbrado(IntegerFilter timbrado) {
        this.timbrado = timbrado;
    }

    public StringFilter getRazonSocial() {
        return razonSocial;
    }

    public StringFilter razonSocial() {
        if (razonSocial == null) {
            razonSocial = new StringFilter();
        }
        return razonSocial;
    }

    public void setRazonSocial(StringFilter razonSocial) {
        this.razonSocial = razonSocial;
    }

    public StringFilter getRuc() {
        return ruc;
    }

    public StringFilter ruc() {
        if (ruc == null) {
            ruc = new StringFilter();
        }
        return ruc;
    }

    public void setRuc(StringFilter ruc) {
        this.ruc = ruc;
    }

    public CondicionVentaFilter getCondicionVenta() {
        return condicionVenta;
    }

    public CondicionVentaFilter condicionVenta() {
        if (condicionVenta == null) {
            condicionVenta = new CondicionVentaFilter();
        }
        return condicionVenta;
    }

    public void setCondicionVenta(CondicionVentaFilter condicionVenta) {
        this.condicionVenta = condicionVenta;
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

    public LongFilter getFacturaDetalleId() {
        return facturaDetalleId;
    }

    public LongFilter facturaDetalleId() {
        if (facturaDetalleId == null) {
            facturaDetalleId = new LongFilter();
        }
        return facturaDetalleId;
    }

    public void setFacturaDetalleId(LongFilter facturaDetalleId) {
        this.facturaDetalleId = facturaDetalleId;
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
        final FacturasCriteria that = (FacturasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(facturaNro, that.facturaNro) &&
            Objects.equals(timbrado, that.timbrado) &&
            Objects.equals(razonSocial, that.razonSocial) &&
            Objects.equals(ruc, that.ruc) &&
            Objects.equals(condicionVenta, that.condicionVenta) &&
            Objects.equals(total, that.total) &&
            Objects.equals(facturaDetalleId, that.facturaDetalleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, facturaNro, timbrado, razonSocial, ruc, condicionVenta, total, facturaDetalleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (facturaNro != null ? "facturaNro=" + facturaNro + ", " : "") +
            (timbrado != null ? "timbrado=" + timbrado + ", " : "") +
            (razonSocial != null ? "razonSocial=" + razonSocial + ", " : "") +
            (ruc != null ? "ruc=" + ruc + ", " : "") +
            (condicionVenta != null ? "condicionVenta=" + condicionVenta + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (facturaDetalleId != null ? "facturaDetalleId=" + facturaDetalleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
