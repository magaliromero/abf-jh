package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Inscripciones} entity. This class is used
 * in {@link py.com.abf.web.rest.InscripcionesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscripciones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscripcionesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaInscripcion;

    private LongFilter alumnosId;

    private LongFilter cursosId;

    private Boolean distinct;

    public InscripcionesCriteria() {}

    public InscripcionesCriteria(InscripcionesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fechaInscripcion = other.fechaInscripcion == null ? null : other.fechaInscripcion.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
        this.cursosId = other.cursosId == null ? null : other.cursosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InscripcionesCriteria copy() {
        return new InscripcionesCriteria(this);
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

    public LocalDateFilter getFechaInscripcion() {
        return fechaInscripcion;
    }

    public LocalDateFilter fechaInscripcion() {
        if (fechaInscripcion == null) {
            fechaInscripcion = new LocalDateFilter();
        }
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateFilter fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
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

    public LongFilter getCursosId() {
        return cursosId;
    }

    public LongFilter cursosId() {
        if (cursosId == null) {
            cursosId = new LongFilter();
        }
        return cursosId;
    }

    public void setCursosId(LongFilter cursosId) {
        this.cursosId = cursosId;
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
        final InscripcionesCriteria that = (InscripcionesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaInscripcion, that.fechaInscripcion) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(cursosId, that.cursosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInscripcion, alumnosId, cursosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscripcionesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fechaInscripcion != null ? "fechaInscripcion=" + fechaInscripcion + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (cursosId != null ? "cursosId=" + cursosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
