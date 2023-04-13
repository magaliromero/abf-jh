package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Prestamos} entity. This class is used
 * in {@link py.com.abf.web.rest.PrestamosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prestamos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestamosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaPrestamo;

    private IntegerFilter vigenciaPrestamo;

    private LocalDateFilter fechaDevolucion;

    private LongFilter materialesId;

    private Boolean distinct;

    public PrestamosCriteria() {}

    public PrestamosCriteria(PrestamosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaPrestamo = other.fechaPrestamo == null ? null : other.fechaPrestamo.copy();
        this.vigenciaPrestamo = other.vigenciaPrestamo == null ? null : other.vigenciaPrestamo.copy();
        this.fechaDevolucion = other.fechaDevolucion == null ? null : other.fechaDevolucion.copy();
        this.materialesId = other.materialesId == null ? null : other.materialesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PrestamosCriteria copy() {
        return new PrestamosCriteria(this);
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

    public LocalDateFilter getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateFilter fechaPrestamo() {
        if (fechaPrestamo == null) {
            fechaPrestamo = new LocalDateFilter();
        }
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateFilter fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public IntegerFilter getVigenciaPrestamo() {
        return vigenciaPrestamo;
    }

    public IntegerFilter vigenciaPrestamo() {
        if (vigenciaPrestamo == null) {
            vigenciaPrestamo = new IntegerFilter();
        }
        return vigenciaPrestamo;
    }

    public void setVigenciaPrestamo(IntegerFilter vigenciaPrestamo) {
        this.vigenciaPrestamo = vigenciaPrestamo;
    }

    public LocalDateFilter getFechaDevolucion() {
        return fechaDevolucion;
    }

    public LocalDateFilter fechaDevolucion() {
        if (fechaDevolucion == null) {
            fechaDevolucion = new LocalDateFilter();
        }
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateFilter fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LongFilter getMaterialesId() {
        return materialesId;
    }

    public LongFilter materialesId() {
        if (materialesId == null) {
            materialesId = new LongFilter();
        }
        return materialesId;
    }

    public void setMaterialesId(LongFilter materialesId) {
        this.materialesId = materialesId;
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
        final PrestamosCriteria that = (PrestamosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaPrestamo, that.fechaPrestamo) &&
            Objects.equals(vigenciaPrestamo, that.vigenciaPrestamo) &&
            Objects.equals(fechaDevolucion, that.fechaDevolucion) &&
            Objects.equals(materialesId, that.materialesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaPrestamo, vigenciaPrestamo, fechaDevolucion, materialesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestamosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fechaPrestamo != null ? "fechaPrestamo=" + fechaPrestamo + ", " : "") +
            (vigenciaPrestamo != null ? "vigenciaPrestamo=" + vigenciaPrestamo + ", " : "") +
            (fechaDevolucion != null ? "fechaDevolucion=" + fechaDevolucion + ", " : "") +
            (materialesId != null ? "materialesId=" + materialesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
