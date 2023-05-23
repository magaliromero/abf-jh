package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.TipoProductos;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Productos} entity. This class is used
 * in {@link py.com.abf.web.rest.ProductosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoProductos
     */
    public static class TipoProductosFilter extends Filter<TipoProductos> {

        public TipoProductosFilter() {}

        public TipoProductosFilter(TipoProductosFilter filter) {
            super(filter);
        }

        @Override
        public TipoProductosFilter copy() {
            return new TipoProductosFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoProductosFilter tipoProducto;

    private IntegerFilter precioUnitario;

    private IntegerFilter porcentajeIva;

    private StringFilter descripcion;

    private LongFilter pagosId;

    private LongFilter facturaDetalleId;

    private Boolean distinct;

    public ProductosCriteria() {}

    public ProductosCriteria(ProductosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipoProducto = other.tipoProducto == null ? null : other.tipoProducto.copy();
        this.precioUnitario = other.precioUnitario == null ? null : other.precioUnitario.copy();
        this.porcentajeIva = other.porcentajeIva == null ? null : other.porcentajeIva.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.pagosId = other.pagosId == null ? null : other.pagosId.copy();
        this.facturaDetalleId = other.facturaDetalleId == null ? null : other.facturaDetalleId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductosCriteria copy() {
        return new ProductosCriteria(this);
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

    public TipoProductosFilter getTipoProducto() {
        return tipoProducto;
    }

    public TipoProductosFilter tipoProducto() {
        if (tipoProducto == null) {
            tipoProducto = new TipoProductosFilter();
        }
        return tipoProducto;
    }

    public void setTipoProducto(TipoProductosFilter tipoProducto) {
        this.tipoProducto = tipoProducto;
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

    public IntegerFilter getPorcentajeIva() {
        return porcentajeIva;
    }

    public IntegerFilter porcentajeIva() {
        if (porcentajeIva == null) {
            porcentajeIva = new IntegerFilter();
        }
        return porcentajeIva;
    }

    public void setPorcentajeIva(IntegerFilter porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
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

    public LongFilter getPagosId() {
        return pagosId;
    }

    public LongFilter pagosId() {
        if (pagosId == null) {
            pagosId = new LongFilter();
        }
        return pagosId;
    }

    public void setPagosId(LongFilter pagosId) {
        this.pagosId = pagosId;
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
        final ProductosCriteria that = (ProductosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoProducto, that.tipoProducto) &&
            Objects.equals(precioUnitario, that.precioUnitario) &&
            Objects.equals(porcentajeIva, that.porcentajeIva) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(pagosId, that.pagosId) &&
            Objects.equals(facturaDetalleId, that.facturaDetalleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoProducto, precioUnitario, porcentajeIva, descripcion, pagosId, facturaDetalleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipoProducto != null ? "tipoProducto=" + tipoProducto + ", " : "") +
            (precioUnitario != null ? "precioUnitario=" + precioUnitario + ", " : "") +
            (porcentajeIva != null ? "porcentajeIva=" + porcentajeIva + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (pagosId != null ? "pagosId=" + pagosId + ", " : "") +
            (facturaDetalleId != null ? "facturaDetalleId=" + facturaDetalleId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
