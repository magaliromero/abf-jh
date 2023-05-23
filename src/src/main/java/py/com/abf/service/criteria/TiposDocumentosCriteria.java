package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.TiposDocumentos} entity. This class is used
 * in {@link py.com.abf.web.rest.TiposDocumentosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tipos-documentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TiposDocumentosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private StringFilter descripcion;

    private LongFilter alumnosId;

    private LongFilter funcionariosId;

    private Boolean distinct;

    public TiposDocumentosCriteria() {}

    public TiposDocumentosCriteria(TiposDocumentosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
        this.funcionariosId = other.funcionariosId == null ? null : other.funcionariosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TiposDocumentosCriteria copy() {
        return new TiposDocumentosCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public StringFilter codigo() {
        if (codigo == null) {
            codigo = new StringFilter();
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
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
        final TiposDocumentosCriteria that = (TiposDocumentosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(funcionariosId, that.funcionariosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo, descripcion, alumnosId, funcionariosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TiposDocumentosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codigo != null ? "codigo=" + codigo + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (funcionariosId != null ? "funcionariosId=" + funcionariosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
