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

    private LongFilter mallaCurricularId;

    private LongFilter temasId;

    private LongFilter funcionariosId;

    private LongFilter alumnosId;

    private Boolean distinct;

    public RegistroClasesCriteria() {}

    public RegistroClasesCriteria(RegistroClasesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.cantidadHoras = other.cantidadHoras == null ? null : other.cantidadHoras.copy();
        this.asistenciaAlumno = other.asistenciaAlumno == null ? null : other.asistenciaAlumno.copy();
        this.mallaCurricularId = other.mallaCurricularId == null ? null : other.mallaCurricularId.copy();
        this.temasId = other.temasId == null ? null : other.temasId.copy();
        this.funcionariosId = other.funcionariosId == null ? null : other.funcionariosId.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
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

    public LongFilter getMallaCurricularId() {
        return mallaCurricularId;
    }

    public LongFilter mallaCurricularId() {
        if (mallaCurricularId == null) {
            mallaCurricularId = new LongFilter();
        }
        return mallaCurricularId;
    }

    public void setMallaCurricularId(LongFilter mallaCurricularId) {
        this.mallaCurricularId = mallaCurricularId;
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
        final RegistroClasesCriteria that = (RegistroClasesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(cantidadHoras, that.cantidadHoras) &&
            Objects.equals(asistenciaAlumno, that.asistenciaAlumno) &&
            Objects.equals(mallaCurricularId, that.mallaCurricularId) &&
            Objects.equals(temasId, that.temasId) &&
            Objects.equals(funcionariosId, that.funcionariosId) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, cantidadHoras, asistenciaAlumno, mallaCurricularId, temasId, funcionariosId, alumnosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RegistroClasesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (cantidadHoras != null ? "cantidadHoras=" + cantidadHoras + ", " : "") +
            (asistenciaAlumno != null ? "asistenciaAlumno=" + asistenciaAlumno + ", " : "") +
            (mallaCurricularId != null ? "mallaCurricularId=" + mallaCurricularId + ", " : "") +
            (temasId != null ? "temasId=" + temasId + ", " : "") +
            (funcionariosId != null ? "funcionariosId=" + funcionariosId + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
