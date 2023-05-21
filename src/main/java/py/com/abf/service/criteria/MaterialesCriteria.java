package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Materiales} entity. This class is used
 * in {@link py.com.abf.web.rest.MaterialesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /materiales?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descripcion;

    private IntegerFilter cantidad;

    private IntegerFilter cantidadEnPrestamo;

    private LongFilter prestamosId;

    private Boolean distinct;

    public MaterialesCriteria() {}

    public MaterialesCriteria(MaterialesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.cantidadEnPrestamo = other.cantidadEnPrestamo == null ? null : other.cantidadEnPrestamo.copy();
        this.prestamosId = other.prestamosId == null ? null : other.prestamosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MaterialesCriteria copy() {
        return new MaterialesCriteria(this);
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

    public IntegerFilter getCantidadEnPrestamo() {
        return cantidadEnPrestamo;
    }

    public IntegerFilter cantidadEnPrestamo() {
        if (cantidadEnPrestamo == null) {
            cantidadEnPrestamo = new IntegerFilter();
        }
        return cantidadEnPrestamo;
    }

    public void setCantidadEnPrestamo(IntegerFilter cantidadEnPrestamo) {
        this.cantidadEnPrestamo = cantidadEnPrestamo;
    }

    public LongFilter getPrestamosId() {
        return prestamosId;
    }

    public LongFilter prestamosId() {
        if (prestamosId == null) {
            prestamosId = new LongFilter();
        }
        return prestamosId;
    }

    public void setPrestamosId(LongFilter prestamosId) {
        this.prestamosId = prestamosId;
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
        final MaterialesCriteria that = (MaterialesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(cantidadEnPrestamo, that.cantidadEnPrestamo) &&
            Objects.equals(prestamosId, that.prestamosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, cantidad, cantidadEnPrestamo, prestamosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (cantidadEnPrestamo != null ? "cantidadEnPrestamo=" + cantidadEnPrestamo + ", " : "") +
            (prestamosId != null ? "prestamosId=" + prestamosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
