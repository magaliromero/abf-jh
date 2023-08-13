package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.Niveles;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Cursos} entity. This class is used
 * in {@link py.com.abf.web.rest.CursosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cursos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CursosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Niveles
     */
    public static class NivelesFilter extends Filter<Niveles> {

        public NivelesFilter() {}

        public NivelesFilter(NivelesFilter filter) {
            super(filter);
        }

        @Override
        public NivelesFilter copy() {
            return new NivelesFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreCurso;

    private StringFilter descripcion;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private IntegerFilter cantidadClases;

    private NivelesFilter nivel;

    private LongFilter inscripcionesId;

    private LongFilter temasId;

    private Boolean distinct;

    public CursosCriteria() {}

    public CursosCriteria(CursosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreCurso = other.nombreCurso == null ? null : other.nombreCurso.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
        this.cantidadClases = other.cantidadClases == null ? null : other.cantidadClases.copy();
        this.nivel = other.nivel == null ? null : other.nivel.copy();
        this.inscripcionesId = other.inscripcionesId == null ? null : other.inscripcionesId.copy();
        this.temasId = other.temasId == null ? null : other.temasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CursosCriteria copy() {
        return new CursosCriteria(this);
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

    public StringFilter getNombreCurso() {
        return nombreCurso;
    }

    public StringFilter nombreCurso() {
        if (nombreCurso == null) {
            nombreCurso = new StringFilter();
        }
        return nombreCurso;
    }

    public void setNombreCurso(StringFilter nombreCurso) {
        this.nombreCurso = nombreCurso;
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

    public IntegerFilter getCantidadClases() {
        return cantidadClases;
    }

    public IntegerFilter cantidadClases() {
        if (cantidadClases == null) {
            cantidadClases = new IntegerFilter();
        }
        return cantidadClases;
    }

    public void setCantidadClases(IntegerFilter cantidadClases) {
        this.cantidadClases = cantidadClases;
    }

    public NivelesFilter getNivel() {
        return nivel;
    }

    public NivelesFilter nivel() {
        if (nivel == null) {
            nivel = new NivelesFilter();
        }
        return nivel;
    }

    public void setNivel(NivelesFilter nivel) {
        this.nivel = nivel;
    }

    public LongFilter getInscripcionesId() {
        return inscripcionesId;
    }

    public LongFilter inscripcionesId() {
        if (inscripcionesId == null) {
            inscripcionesId = new LongFilter();
        }
        return inscripcionesId;
    }

    public void setInscripcionesId(LongFilter inscripcionesId) {
        this.inscripcionesId = inscripcionesId;
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
        final CursosCriteria that = (CursosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreCurso, that.nombreCurso) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(cantidadClases, that.cantidadClases) &&
            Objects.equals(nivel, that.nivel) &&
            Objects.equals(inscripcionesId, that.inscripcionesId) &&
            Objects.equals(temasId, that.temasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreCurso, descripcion, fechaInicio, fechaFin, cantidadClases, nivel, inscripcionesId, temasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreCurso != null ? "nombreCurso=" + nombreCurso + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            (cantidadClases != null ? "cantidadClases=" + cantidadClases + ", " : "") +
            (nivel != null ? "nivel=" + nivel + ", " : "") +
            (inscripcionesId != null ? "inscripcionesId=" + inscripcionesId + ", " : "") +
            (temasId != null ? "temasId=" + temasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
