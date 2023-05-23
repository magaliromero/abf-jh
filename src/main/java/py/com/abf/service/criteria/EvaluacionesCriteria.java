package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Evaluaciones} entity. This class is used
 * in {@link py.com.abf.web.rest.EvaluacionesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evaluaciones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluacionesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter nroEvaluacion;

    private LocalDateFilter fecha;

    private LongFilter evaluacionesDetalleId;

    private LongFilter alumnosId;

    private LongFilter funcionariosId;

    private Boolean distinct;

    public EvaluacionesCriteria() {}

    public EvaluacionesCriteria(EvaluacionesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nroEvaluacion = other.nroEvaluacion == null ? null : other.nroEvaluacion.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.evaluacionesDetalleId = other.evaluacionesDetalleId == null ? null : other.evaluacionesDetalleId.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
        this.funcionariosId = other.funcionariosId == null ? null : other.funcionariosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EvaluacionesCriteria copy() {
        return new EvaluacionesCriteria(this);
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

    public IntegerFilter getNroEvaluacion() {
        return nroEvaluacion;
    }

    public IntegerFilter nroEvaluacion() {
        if (nroEvaluacion == null) {
            nroEvaluacion = new IntegerFilter();
        }
        return nroEvaluacion;
    }

    public void setNroEvaluacion(IntegerFilter nroEvaluacion) {
        this.nroEvaluacion = nroEvaluacion;
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

    public LongFilter getEvaluacionesDetalleId() {
        return evaluacionesDetalleId;
    }

    public LongFilter evaluacionesDetalleId() {
        if (evaluacionesDetalleId == null) {
            evaluacionesDetalleId = new LongFilter();
        }
        return evaluacionesDetalleId;
    }

    public void setEvaluacionesDetalleId(LongFilter evaluacionesDetalleId) {
        this.evaluacionesDetalleId = evaluacionesDetalleId;
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
        final EvaluacionesCriteria that = (EvaluacionesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nroEvaluacion, that.nroEvaluacion) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(evaluacionesDetalleId, that.evaluacionesDetalleId) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(funcionariosId, that.funcionariosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nroEvaluacion, fecha, evaluacionesDetalleId, alumnosId, funcionariosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluacionesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nroEvaluacion != null ? "nroEvaluacion=" + nroEvaluacion + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (evaluacionesDetalleId != null ? "evaluacionesDetalleId=" + evaluacionesDetalleId + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (funcionariosId != null ? "funcionariosId=" + funcionariosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
