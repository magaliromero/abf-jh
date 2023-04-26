package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreCurso;

    private LongFilter registroClasesId;

    private Boolean distinct;

    public CursosCriteria() {}

    public CursosCriteria(CursosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreCurso = other.nombreCurso == null ? null : other.nombreCurso.copy();
        this.registroClasesId = other.registroClasesId == null ? null : other.registroClasesId.copy();
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

    public LongFilter getRegistroClasesId() {
        return registroClasesId;
    }

    public LongFilter registroClasesId() {
        if (registroClasesId == null) {
            registroClasesId = new LongFilter();
        }
        return registroClasesId;
    }

    public void setRegistroClasesId(LongFilter registroClasesId) {
        this.registroClasesId = registroClasesId;
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
            Objects.equals(registroClasesId, that.registroClasesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreCurso, registroClasesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CursosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreCurso != null ? "nombreCurso=" + nombreCurso + ", " : "") +
            (registroClasesId != null ? "registroClasesId=" + registroClasesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
