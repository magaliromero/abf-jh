package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.PuntoDeExpedicion} entity. This class is used
 * in {@link py.com.abf.web.rest.PuntoDeExpedicionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /punto-de-expedicions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PuntoDeExpedicionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroPuntoDeExpedicion;

    private LongFilter sucursalesId;

    private Boolean distinct;

    public PuntoDeExpedicionCriteria() {}

    public PuntoDeExpedicionCriteria(PuntoDeExpedicionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numeroPuntoDeExpedicion = other.numeroPuntoDeExpedicion == null ? null : other.numeroPuntoDeExpedicion.copy();
        this.sucursalesId = other.sucursalesId == null ? null : other.sucursalesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PuntoDeExpedicionCriteria copy() {
        return new PuntoDeExpedicionCriteria(this);
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

    public StringFilter getNumeroPuntoDeExpedicion() {
        return numeroPuntoDeExpedicion;
    }

    public StringFilter numeroPuntoDeExpedicion() {
        if (numeroPuntoDeExpedicion == null) {
            numeroPuntoDeExpedicion = new StringFilter();
        }
        return numeroPuntoDeExpedicion;
    }

    public void setNumeroPuntoDeExpedicion(StringFilter numeroPuntoDeExpedicion) {
        this.numeroPuntoDeExpedicion = numeroPuntoDeExpedicion;
    }

    public LongFilter getSucursalesId() {
        return sucursalesId;
    }

    public LongFilter sucursalesId() {
        if (sucursalesId == null) {
            sucursalesId = new LongFilter();
        }
        return sucursalesId;
    }

    public void setSucursalesId(LongFilter sucursalesId) {
        this.sucursalesId = sucursalesId;
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
        final PuntoDeExpedicionCriteria that = (PuntoDeExpedicionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroPuntoDeExpedicion, that.numeroPuntoDeExpedicion) &&
            Objects.equals(sucursalesId, that.sucursalesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroPuntoDeExpedicion, sucursalesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PuntoDeExpedicionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numeroPuntoDeExpedicion != null ? "numeroPuntoDeExpedicion=" + numeroPuntoDeExpedicion + ", " : "") +
            (sucursalesId != null ? "sucursalesId=" + sucursalesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
