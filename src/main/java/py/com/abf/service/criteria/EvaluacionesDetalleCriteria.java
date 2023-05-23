package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.EvaluacionesDetalle} entity. This class is used
 * in {@link py.com.abf.web.rest.EvaluacionesDetalleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evaluaciones-detalles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluacionesDetalleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comentarios;

    private IntegerFilter puntaje;

    private LongFilter evaluacionesId;

    private LongFilter temasId;

    private Boolean distinct;

    public EvaluacionesDetalleCriteria() {}

    public EvaluacionesDetalleCriteria(EvaluacionesDetalleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.comentarios = other.comentarios == null ? null : other.comentarios.copy();
        this.puntaje = other.puntaje == null ? null : other.puntaje.copy();
        this.evaluacionesId = other.evaluacionesId == null ? null : other.evaluacionesId.copy();
        this.temasId = other.temasId == null ? null : other.temasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EvaluacionesDetalleCriteria copy() {
        return new EvaluacionesDetalleCriteria(this);
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

    public StringFilter getComentarios() {
        return comentarios;
    }

    public StringFilter comentarios() {
        if (comentarios == null) {
            comentarios = new StringFilter();
        }
        return comentarios;
    }

    public void setComentarios(StringFilter comentarios) {
        this.comentarios = comentarios;
    }

    public IntegerFilter getPuntaje() {
        return puntaje;
    }

    public IntegerFilter puntaje() {
        if (puntaje == null) {
            puntaje = new IntegerFilter();
        }
        return puntaje;
    }

    public void setPuntaje(IntegerFilter puntaje) {
        this.puntaje = puntaje;
    }

    public LongFilter getEvaluacionesId() {
        return evaluacionesId;
    }

    public LongFilter evaluacionesId() {
        if (evaluacionesId == null) {
            evaluacionesId = new LongFilter();
        }
        return evaluacionesId;
    }

    public void setEvaluacionesId(LongFilter evaluacionesId) {
        this.evaluacionesId = evaluacionesId;
    }

    public LongFilter getTemasId() {
        return temasId;
    }

    public LongFilter temasId() {
        if (temasId == null) {
            temasId = new LongFilter();
        }
        return temasId;
    }

    public void setTemasId(LongFilter temasId) {
        this.temasId = temasId;
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
        final EvaluacionesDetalleCriteria that = (EvaluacionesDetalleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(comentarios, that.comentarios) &&
            Objects.equals(puntaje, that.puntaje) &&
            Objects.equals(evaluacionesId, that.evaluacionesId) &&
            Objects.equals(temasId, that.temasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, comentarios, puntaje, evaluacionesId, temasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluacionesDetalleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (comentarios != null ? "comentarios=" + comentarios + ", " : "") +
            (puntaje != null ? "puntaje=" + puntaje + ", " : "") +
            (evaluacionesId != null ? "evaluacionesId=" + evaluacionesId + ", " : "") +
            (temasId != null ? "temasId=" + temasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
