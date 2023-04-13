package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.TiposEvaluaciones;
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

    /**
     * Class for filtering TiposEvaluaciones
     */
    public static class TiposEvaluacionesFilter extends Filter<TiposEvaluaciones> {

        public TiposEvaluacionesFilter() {}

        public TiposEvaluacionesFilter(TiposEvaluacionesFilter filter) {
            super(filter);
        }

        @Override
        public TiposEvaluacionesFilter copy() {
            return new TiposEvaluacionesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TiposEvaluacionesFilter tipoEvaluacion;

    private IntegerFilter idExamen;

    private IntegerFilter idActa;

    private LocalDateFilter fecha;

    private IntegerFilter puntosLogrados;

    private IntegerFilter porcentaje;

    private StringFilter comentarios;

    private LongFilter alumnosId;

    private Boolean distinct;

    public EvaluacionesCriteria() {}

    public EvaluacionesCriteria(EvaluacionesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipoEvaluacion = other.tipoEvaluacion == null ? null : other.tipoEvaluacion.copy();
        this.idExamen = other.idExamen == null ? null : other.idExamen.copy();
        this.idActa = other.idActa == null ? null : other.idActa.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.puntosLogrados = other.puntosLogrados == null ? null : other.puntosLogrados.copy();
        this.porcentaje = other.porcentaje == null ? null : other.porcentaje.copy();
        this.comentarios = other.comentarios == null ? null : other.comentarios.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
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

    public TiposEvaluacionesFilter getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public TiposEvaluacionesFilter tipoEvaluacion() {
        if (tipoEvaluacion == null) {
            tipoEvaluacion = new TiposEvaluacionesFilter();
        }
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(TiposEvaluacionesFilter tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public IntegerFilter getIdExamen() {
        return idExamen;
    }

    public IntegerFilter idExamen() {
        if (idExamen == null) {
            idExamen = new IntegerFilter();
        }
        return idExamen;
    }

    public void setIdExamen(IntegerFilter idExamen) {
        this.idExamen = idExamen;
    }

    public IntegerFilter getIdActa() {
        return idActa;
    }

    public IntegerFilter idActa() {
        if (idActa == null) {
            idActa = new IntegerFilter();
        }
        return idActa;
    }

    public void setIdActa(IntegerFilter idActa) {
        this.idActa = idActa;
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

    public IntegerFilter getPuntosLogrados() {
        return puntosLogrados;
    }

    public IntegerFilter puntosLogrados() {
        if (puntosLogrados == null) {
            puntosLogrados = new IntegerFilter();
        }
        return puntosLogrados;
    }

    public void setPuntosLogrados(IntegerFilter puntosLogrados) {
        this.puntosLogrados = puntosLogrados;
    }

    public IntegerFilter getPorcentaje() {
        return porcentaje;
    }

    public IntegerFilter porcentaje() {
        if (porcentaje == null) {
            porcentaje = new IntegerFilter();
        }
        return porcentaje;
    }

    public void setPorcentaje(IntegerFilter porcentaje) {
        this.porcentaje = porcentaje;
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
        final EvaluacionesCriteria that = (EvaluacionesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoEvaluacion, that.tipoEvaluacion) &&
            Objects.equals(idExamen, that.idExamen) &&
            Objects.equals(idActa, that.idActa) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(puntosLogrados, that.puntosLogrados) &&
            Objects.equals(porcentaje, that.porcentaje) &&
            Objects.equals(comentarios, that.comentarios) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoEvaluacion, idExamen, idActa, fecha, puntosLogrados, porcentaje, comentarios, alumnosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluacionesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tipoEvaluacion != null ? "tipoEvaluacion=" + tipoEvaluacion + ", " : "") +
            (idExamen != null ? "idExamen=" + idExamen + ", " : "") +
            (idActa != null ? "idActa=" + idActa + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (puntosLogrados != null ? "puntosLogrados=" + puntosLogrados + ", " : "") +
            (porcentaje != null ? "porcentaje=" + porcentaje + ", " : "") +
            (comentarios != null ? "comentarios=" + comentarios + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
