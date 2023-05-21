package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.Niveles;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Temas} entity. This class is used
 * in {@link py.com.abf.web.rest.TemasResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /temas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TemasCriteria implements Serializable, Criteria {

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

    private StringFilter descripcion;

    private NivelesFilter nivel;

    private LongFilter registroClasesId;

    private Boolean distinct;

    public TemasCriteria() {}

    public TemasCriteria(TemasCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.nivel = other.nivel == null ? null : other.nivel.copy();
        this.registroClasesId = other.registroClasesId == null ? null : other.registroClasesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TemasCriteria copy() {
        return new TemasCriteria(this);
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
        final TemasCriteria that = (TemasCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(nivel, that.nivel) &&
            Objects.equals(registroClasesId, that.registroClasesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, nivel, registroClasesId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TemasCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (nivel != null ? "nivel=" + nivel + ", " : "") +
            (registroClasesId != null ? "registroClasesId=" + registroClasesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
