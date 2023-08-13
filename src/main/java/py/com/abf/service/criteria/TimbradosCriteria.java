package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Timbrados} entity. This class is used
 * in {@link py.com.abf.web.rest.TimbradosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /timbrados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimbradosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroTimbrado;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private LongFilter sucursalesId;

    private Boolean distinct;

    public TimbradosCriteria() {}

    public TimbradosCriteria(TimbradosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numeroTimbrado = other.numeroTimbrado == null ? null : other.numeroTimbrado.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
        this.sucursalesId = other.sucursalesId == null ? null : other.sucursalesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TimbradosCriteria copy() {
        return new TimbradosCriteria(this);
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

    public StringFilter getNumeroTimbrado() {
        return numeroTimbrado;
    }

    public StringFilter numeroTimbrado() {
        if (numeroTimbrado == null) {
            numeroTimbrado = new StringFilter();
        }
        return numeroTimbrado;
    }

    public void setNumeroTimbrado(StringFilter numeroTimbrado) {
        this.numeroTimbrado = numeroTimbrado;
    }

    public LocalDateFilter getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateFilter fechaInicio() {
        if (fechaInicio == null) {
            fechaInicio = new LocalDateFilter();
        }
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            fechaFin = new LocalDateFilter();
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
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
        final TimbradosCriteria that = (TimbradosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroTimbrado, that.numeroTimbrado) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(sucursalesId, that.sucursalesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroTimbrado, fechaInicio, fechaFin, sucursalesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimbradosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numeroTimbrado != null ? "numeroTimbrado=" + numeroTimbrado + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            (sucursalesId != null ? "sucursalesId=" + sucursalesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
