package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.EstadosPersona;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Alumnos} entity. This class is used
 * in {@link py.com.abf.web.rest.AlumnosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alumnos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlumnosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadosPersona
     */
    public static class EstadosPersonaFilter extends Filter<EstadosPersona> {

        public EstadosPersonaFilter() {}

        public EstadosPersonaFilter(EstadosPersonaFilter filter) {
            super(filter);
        }

        @Override
        public EstadosPersonaFilter copy() {
            return new EstadosPersonaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter elo;

    private IntegerFilter fideId;

    private StringFilter nombres;

    private StringFilter apellidos;

    private StringFilter nombreCompleto;

    private StringFilter email;

    private StringFilter telefono;

    private LocalDateFilter fechaNacimiento;

    private StringFilter documento;

    private EstadosPersonaFilter estado;

    private LongFilter inscripcionesId;

    private LongFilter evaluacionesId;

    private LongFilter matriculaId;

    private LongFilter prestamosId;

    private LongFilter registroClasesId;

    private LongFilter tipoDocumentosId;

    private Boolean distinct;

    public AlumnosCriteria() {}

    public AlumnosCriteria(AlumnosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.elo = other.elo == null ? null : other.elo.copy();
        this.fideId = other.fideId == null ? null : other.fideId.copy();
        this.nombres = other.nombres == null ? null : other.nombres.copy();
        this.apellidos = other.apellidos == null ? null : other.apellidos.copy();
        this.nombreCompleto = other.nombreCompleto == null ? null : other.nombreCompleto.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.documento = other.documento == null ? null : other.documento.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.inscripcionesId = other.inscripcionesId == null ? null : other.inscripcionesId.copy();
        this.evaluacionesId = other.evaluacionesId == null ? null : other.evaluacionesId.copy();
        this.matriculaId = other.matriculaId == null ? null : other.matriculaId.copy();
        this.prestamosId = other.prestamosId == null ? null : other.prestamosId.copy();
        this.registroClasesId = other.registroClasesId == null ? null : other.registroClasesId.copy();
        this.tipoDocumentosId = other.tipoDocumentosId == null ? null : other.tipoDocumentosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AlumnosCriteria copy() {
        return new AlumnosCriteria(this);
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

    public IntegerFilter getElo() {
        return elo;
    }

    public IntegerFilter elo() {
        if (elo == null) {
            elo = new IntegerFilter();
        }
        return elo;
    }

    public void setElo(IntegerFilter elo) {
        this.elo = elo;
    }

    public IntegerFilter getFideId() {
        return fideId;
    }

    public IntegerFilter fideId() {
        if (fideId == null) {
            fideId = new IntegerFilter();
        }
        return fideId;
    }

    public void setFideId(IntegerFilter fideId) {
        this.fideId = fideId;
    }

    public StringFilter getNombres() {
        return nombres;
    }

    public StringFilter nombres() {
        if (nombres == null) {
            nombres = new StringFilter();
        }
        return nombres;
    }

    public void setNombres(StringFilter nombres) {
        this.nombres = nombres;
    }

    public StringFilter getApellidos() {
        return apellidos;
    }

    public StringFilter apellidos() {
        if (apellidos == null) {
            apellidos = new StringFilter();
        }
        return apellidos;
    }

    public void setApellidos(StringFilter apellidos) {
        this.apellidos = apellidos;
    }

    public StringFilter getNombreCompleto() {
        return nombreCompleto;
    }

    public StringFilter nombreCompleto() {
        if (nombreCompleto == null) {
            nombreCompleto = new StringFilter();
        }
        return nombreCompleto;
    }

    public void setNombreCompleto(StringFilter nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public StringFilter telefono() {
        if (telefono == null) {
            telefono = new StringFilter();
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            fechaNacimiento = new LocalDateFilter();
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StringFilter getDocumento() {
        return documento;
    }

    public StringFilter documento() {
        if (documento == null) {
            documento = new StringFilter();
        }
        return documento;
    }

    public void setDocumento(StringFilter documento) {
        this.documento = documento;
    }

    public EstadosPersonaFilter getEstado() {
        return estado;
    }

    public EstadosPersonaFilter estado() {
        if (estado == null) {
            estado = new EstadosPersonaFilter();
        }
        return estado;
    }

    public void setEstado(EstadosPersonaFilter estado) {
        this.estado = estado;
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

    public LongFilter getMatriculaId() {
        return matriculaId;
    }

    public LongFilter matriculaId() {
        if (matriculaId == null) {
            matriculaId = new LongFilter();
        }
        return matriculaId;
    }

    public void setMatriculaId(LongFilter matriculaId) {
        this.matriculaId = matriculaId;
    }

    public LongFilter getPrestamosId() {
        return prestamosId;
    }

    public LongFilter prestamosId() {
        if (prestamosId == null) {
            prestamosId = new LongFilter();
        }
        return prestamosId;
    }

    public void setPrestamosId(LongFilter prestamosId) {
        this.prestamosId = prestamosId;
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

    public LongFilter getTipoDocumentosId() {
        return tipoDocumentosId;
    }

    public LongFilter tipoDocumentosId() {
        if (tipoDocumentosId == null) {
            tipoDocumentosId = new LongFilter();
        }
        return tipoDocumentosId;
    }

    public void setTipoDocumentosId(LongFilter tipoDocumentosId) {
        this.tipoDocumentosId = tipoDocumentosId;
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
        final AlumnosCriteria that = (AlumnosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(elo, that.elo) &&
            Objects.equals(fideId, that.fideId) &&
            Objects.equals(nombres, that.nombres) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(nombreCompleto, that.nombreCompleto) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(documento, that.documento) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(inscripcionesId, that.inscripcionesId) &&
            Objects.equals(evaluacionesId, that.evaluacionesId) &&
            Objects.equals(matriculaId, that.matriculaId) &&
            Objects.equals(prestamosId, that.prestamosId) &&
            Objects.equals(registroClasesId, that.registroClasesId) &&
            Objects.equals(tipoDocumentosId, that.tipoDocumentosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            elo,
            fideId,
            nombres,
            apellidos,
            nombreCompleto,
            email,
            telefono,
            fechaNacimiento,
            documento,
            estado,
            inscripcionesId,
            evaluacionesId,
            matriculaId,
            prestamosId,
            registroClasesId,
            tipoDocumentosId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlumnosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (elo != null ? "elo=" + elo + ", " : "") +
            (fideId != null ? "fideId=" + fideId + ", " : "") +
            (nombres != null ? "nombres=" + nombres + ", " : "") +
            (apellidos != null ? "apellidos=" + apellidos + ", " : "") +
            (nombreCompleto != null ? "nombreCompleto=" + nombreCompleto + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
            (documento != null ? "documento=" + documento + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (inscripcionesId != null ? "inscripcionesId=" + inscripcionesId + ", " : "") +
            (evaluacionesId != null ? "evaluacionesId=" + evaluacionesId + ", " : "") +
            (matriculaId != null ? "matriculaId=" + matriculaId + ", " : "") +
            (prestamosId != null ? "prestamosId=" + prestamosId + ", " : "") +
            (registroClasesId != null ? "registroClasesId=" + registroClasesId + ", " : "") +
            (tipoDocumentosId != null ? "tipoDocumentosId=" + tipoDocumentosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
