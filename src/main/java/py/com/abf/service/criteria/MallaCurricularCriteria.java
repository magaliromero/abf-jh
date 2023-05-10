package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.Niveles;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.MallaCurricular} entity. This class is used
 * in {@link py.com.abf.web.rest.MallaCurricularResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /malla-curriculars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MallaCurricularCriteria implements Serializable, Criteria {

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

    private StringFilter titulo;

    private NivelesFilter nivel;

    private LongFilter temasId;

    private Boolean distinct;

    public MallaCurricularCriteria() {}

    public MallaCurricularCriteria(MallaCurricularCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.nivel = other.nivel == null ? null : other.nivel.copy();
        this.temasId = other.temasId == null ? null : other.temasId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MallaCurricularCriteria copy() {
        return new MallaCurricularCriteria(this);
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

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
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
        final MallaCurricularCriteria that = (MallaCurricularCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(nivel, that.nivel) &&
            Objects.equals(temasId, that.temasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, nivel, temasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MallaCurricularCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (nivel != null ? "nivel=" + nivel + ", " : "") +
            (temasId != null ? "temasId=" + temasId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
