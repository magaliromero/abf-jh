package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.CondVenta;
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
     * Class for filtering CondVenta
     */
    public static class CondVentaFilter extends Filter<CondVenta> {

        public CondVentaFilter() {}

        public CondVentaFilter(CondVentaFilter filter) {
            super(filter);
        }

        @Override
        public CondVentaFilter copy() {
            return new CondVentaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private StringFilter facturaNro;

    private IntegerFilter timbrado;

    private IntegerFilter ruc;

    private CondVentaFilter condicionVenta;

    private IntegerFilter cantidad;

    private StringFilter descripcion;

    private IntegerFilter precioUnitario;

    private IntegerFilter valor5;

    private IntegerFilter valor10;

    private IntegerFilter total;

    private IntegerFilter total5;

    private IntegerFilter total10;

    private IntegerFilter totalIva;

    private LongFilter cobrosId;

    private LongFilter alumnosId;

    private Boolean distinct;

    public FacturasCriteria() {}

    public FacturasCriteria(FacturasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.facturaNro = other.facturaNro == null ? null : other.facturaNro.copy();
        this.timbrado = other.timbrado == null ? null : other.timbrado.copy();
        this.ruc = other.ruc == null ? null : other.ruc.copy();
        this.condicionVenta = other.condicionVenta == null ? null : other.condicionVenta.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.precioUnitario = other.precioUnitario == null ? null : other.precioUnitario.copy();
        this.valor5 = other.valor5 == null ? null : other.valor5.copy();
        this.valor10 = other.valor10 == null ? null : other.valor10.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.total5 = other.total5 == null ? null : other.total5.copy();
        this.total10 = other.total10 == null ? null : other.total10.copy();
        this.totalIva = other.totalIva == null ? null : other.totalIva.copy();
        this.cobrosId = other.cobrosId == null ? null : other.cobrosId.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
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

    public IntegerFilter getRuc() {
        return ruc;
    }

    public IntegerFilter ruc() {
        if (ruc == null) {
            ruc = new IntegerFilter();
        }
        return ruc;
    }

    public void setRuc(IntegerFilter ruc) {
        this.ruc = ruc;
    }

    public CondVentaFilter getCondicionVenta() {
        return condicionVenta;
    }

    public CondVentaFilter condicionVenta() {
        if (condicionVenta == null) {
            condicionVenta = new CondVentaFilter();
        }
        return condicionVenta;
    }

    public void setCondicionVenta(CondVentaFilter condicionVenta) {
        this.condicionVenta = condicionVenta;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            cantidad = new IntegerFilter();
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
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

    public IntegerFilter getPrecioUnitario() {
        return precioUnitario;
    }

    public IntegerFilter precioUnitario() {
        if (precioUnitario == null) {
            precioUnitario = new IntegerFilter();
        }
        return precioUnitario;
    }

    public void setPrecioUnitario(IntegerFilter precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public IntegerFilter getValor5() {
        return valor5;
    }

    public IntegerFilter valor5() {
        if (valor5 == null) {
            valor5 = new IntegerFilter();
        }
        return valor5;
    }

    public void setValor5(IntegerFilter valor5) {
        this.valor5 = valor5;
    }

    public IntegerFilter getValor10() {
        return valor10;
    }

    public IntegerFilter valor10() {
        if (valor10 == null) {
            valor10 = new IntegerFilter();
        }
        return valor10;
    }

    public void setValor10(IntegerFilter valor10) {
        this.valor10 = valor10;
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

    public IntegerFilter getTotal5() {
        return total5;
    }

    public IntegerFilter total5() {
        if (total5 == null) {
            total5 = new IntegerFilter();
        }
        return total5;
    }

    public void setTotal5(IntegerFilter total5) {
        this.total5 = total5;
    }

    public IntegerFilter getTotal10() {
        return total10;
    }

    public IntegerFilter total10() {
        if (total10 == null) {
            total10 = new IntegerFilter();
        }
        return total10;
    }

    public void setTotal10(IntegerFilter total10) {
        this.total10 = total10;
    }

    public IntegerFilter getTotalIva() {
        return totalIva;
    }

    public IntegerFilter totalIva() {
        if (totalIva == null) {
            totalIva = new IntegerFilter();
        }
        return totalIva;
    }

    public void setTotalIva(IntegerFilter totalIva) {
        this.totalIva = totalIva;
    }

    public LongFilter getCobrosId() {
        return cobrosId;
    }

    public LongFilter cobrosId() {
        if (cobrosId == null) {
            cobrosId = new LongFilter();
        }
        return cobrosId;
    }

    public void setCobrosId(LongFilter cobrosId) {
        this.cobrosId = cobrosId;
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
        final FacturasCriteria that = (FacturasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(facturaNro, that.facturaNro) &&
            Objects.equals(timbrado, that.timbrado) &&
            Objects.equals(ruc, that.ruc) &&
            Objects.equals(condicionVenta, that.condicionVenta) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precioUnitario, that.precioUnitario) &&
            Objects.equals(valor5, that.valor5) &&
            Objects.equals(valor10, that.valor10) &&
            Objects.equals(total, that.total) &&
            Objects.equals(total5, that.total5) &&
            Objects.equals(total10, that.total10) &&
            Objects.equals(totalIva, that.totalIva) &&
            Objects.equals(cobrosId, that.cobrosId) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fecha,
            facturaNro,
            timbrado,
            ruc,
            condicionVenta,
            cantidad,
            descripcion,
            precioUnitario,
            valor5,
            valor10,
            total,
            total5,
            total10,
            totalIva,
            cobrosId,
            alumnosId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (facturaNro != null ? "facturaNro=" + facturaNro + ", " : "") +
            (timbrado != null ? "timbrado=" + timbrado + ", " : "") +
            (ruc != null ? "ruc=" + ruc + ", " : "") +
            (condicionVenta != null ? "condicionVenta=" + condicionVenta + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (precioUnitario != null ? "precioUnitario=" + precioUnitario + ", " : "") +
            (valor5 != null ? "valor5=" + valor5 + ", " : "") +
            (valor10 != null ? "valor10=" + valor10 + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (total5 != null ? "total5=" + total5 + ", " : "") +
            (total10 != null ? "total10=" + total10 + ", " : "") +
            (totalIva != null ? "totalIva=" + totalIva + ", " : "") +
            (cobrosId != null ? "cobrosId=" + cobrosId + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
