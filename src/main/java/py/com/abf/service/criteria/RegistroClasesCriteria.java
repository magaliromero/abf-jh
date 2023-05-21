package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.RegistroClases} entity. This class is used
 * in {@link py.com.abf.web.rest.RegistroClasesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /registro-clases?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RegistroClasesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fecha;

    private IntegerFilter cantidadHoras;

    private BooleanFilter asistenciaAlumno;

    private LongFilter temaId;

    private LongFilter funcionarioId;

    private Boolean distinct;

    public RegistroClasesCriteria() {}

    public RegistroClasesCriteria(RegistroClasesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.cantidadHoras = other.cantidadHoras == null ? null : other.cantidadHoras.copy();
        this.asistenciaAlumno = other.asistenciaAlumno == null ? null : other.asistenciaAlumno.copy();
        this.temaId = other.temaId == null ? null : other.temaId.copy();
        this.funcionarioId = other.funcionarioId == null ? null : other.funcionarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RegistroClasesCriteria copy() {
        return new RegistroClasesCriteria(this);
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

    public IntegerFilter getCantidadHoras() {
        return cantidadHoras;
    }

    public IntegerFilter cantidadHoras() {
        if (cantidadHoras == null) {
            cantidadHoras = new IntegerFilter();
        }
        return cantidadHoras;
    }

    public void setCantidadHoras(IntegerFilter cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public BooleanFilter getAsistenciaAlumno() {
        return asistenciaAlumno;
    }

    public BooleanFilter asistenciaAlumno() {
        if (asistenciaAlumno == null) {
            asistenciaAlumno = new BooleanFilter();
        }
        return asistenciaAlumno;
    }

    public void setAsistenciaAlumno(BooleanFilter asistenciaAlumno) {
        this.asistenciaAlumno = asistenciaAlumno;
    }

    public LongFilter getTemaId() {
        return temaId;
    }

    public LongFilter temaId() {
        if (temaId == null) {
            temaId = new LongFilter();
        }
        return temaId;
    }

    public void setTemaId(LongFilter temaId) {
        this.temaId = temaId;
    }

    public LongFilter getFuncionarioId() {
        return funcionarioId;
    }

    public LongFilter funcionarioId() {
        if (funcionarioId == null) {
            funcionarioId = new LongFilter();
        }
        return funcionarioId;
    }

    public void setFuncionarioId(LongFilter funcionarioId) {
        this.funcionarioId = funcionarioId;
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
        final RegistroClasesCriteria that = (RegistroClasesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(cantidadHoras, that.cantidadHoras) &&
            Objects.equals(asistenciaAlumno, that.asistenciaAlumno) &&
            Objects.equals(temaId, that.temaId) &&
            Objects.equals(funcionarioId, that.funcionarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, cantidadHoras, asistenciaAlumno, temaId, funcionarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroClasesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (cantidadHoras != null ? "cantidadHoras=" + cantidadHoras + ", " : "") +
            (asistenciaAlumno != null ? "asistenciaAlumno=" + asistenciaAlumno + ", " : "") +
            (temaId != null ? "temaId=" + temaId + ", " : "") +
            (funcionarioId != null ? "funcionarioId=" + funcionarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
