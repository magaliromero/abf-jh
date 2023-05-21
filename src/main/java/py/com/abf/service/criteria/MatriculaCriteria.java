package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.EstadosPagos;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Matricula} entity. This class is used
 * in {@link py.com.abf.web.rest.MatriculaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /matriculas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriculaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadosPagos
     */
    public static class EstadosPagosFilter extends Filter<EstadosPagos> {

        public EstadosPagosFilter() {}

        public EstadosPagosFilter(EstadosPagosFilter filter) {
            super(filter);
        }

        @Override
        public EstadosPagosFilter copy() {
            return new EstadosPagosFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter concepto;

    private IntegerFilter monto;

    private LocalDateFilter fechaInscripcion;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaPago;

    private EstadosPagosFilter estado;

    private LongFilter alumnoId;

    private Boolean distinct;

    public MatriculaCriteria() {}

    public MatriculaCriteria(MatriculaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.concepto = other.concepto == null ? null : other.concepto.copy();
        this.monto = other.monto == null ? null : other.monto.copy();
        this.fechaInscripcion = other.fechaInscripcion == null ? null : other.fechaInscripcion.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaPago = other.fechaPago == null ? null : other.fechaPago.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.alumnoId = other.alumnoId == null ? null : other.alumnoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MatriculaCriteria copy() {
        return new MatriculaCriteria(this);
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

    public StringFilter getConcepto() {
        return concepto;
    }

    public StringFilter concepto() {
        if (concepto == null) {
            concepto = new StringFilter();
        }
        return concepto;
    }

    public void setConcepto(StringFilter concepto) {
        this.concepto = concepto;
    }

    public IntegerFilter getMonto() {
        return monto;
    }

    public IntegerFilter monto() {
        if (monto == null) {
            monto = new IntegerFilter();
        }
        return monto;
    }

    public void setMonto(IntegerFilter monto) {
        this.monto = monto;
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

    public LocalDateFilter getFechaPago() {
        return fechaPago;
    }

    public LocalDateFilter fechaPago() {
        if (fechaPago == null) {
            fechaPago = new LocalDateFilter();
        }
        return fechaPago;
    }

    public void setFechaPago(LocalDateFilter fechaPago) {
        this.fechaPago = fechaPago;
    }

    public EstadosPagosFilter getEstado() {
        return estado;
    }

    public EstadosPagosFilter estado() {
        if (estado == null) {
            estado = new EstadosPagosFilter();
        }
        return estado;
    }

    public void setEstado(EstadosPagosFilter estado) {
        this.estado = estado;
    }

    public LongFilter getAlumnoId() {
        return alumnoId;
    }

    public LongFilter alumnoId() {
        if (alumnoId == null) {
            alumnoId = new LongFilter();
        }
        return alumnoId;
    }

    public void setAlumnoId(LongFilter alumnoId) {
        this.alumnoId = alumnoId;
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
        final MatriculaCriteria that = (MatriculaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(concepto, that.concepto) &&
            Objects.equals(monto, that.monto) &&
            Objects.equals(fechaInscripcion, that.fechaInscripcion) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaPago, that.fechaPago) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(alumnoId, that.alumnoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, concepto, monto, fechaInscripcion, fechaInicio, fechaPago, estado, alumnoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriculaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (concepto != null ? "concepto=" + concepto + ", " : "") +
            (monto != null ? "monto=" + monto + ", " : "") +
            (fechaInscripcion != null ? "fechaInscripcion=" + fechaInscripcion + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaPago != null ? "fechaPago=" + fechaPago + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (alumnoId != null ? "alumnoId=" + alumnoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
