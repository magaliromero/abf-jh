package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Clientes} entity. This class is used
 * in {@link py.com.abf.web.rest.ClientesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ruc;

    private StringFilter nombres;

    private StringFilter apellidos;

    private StringFilter razonSocial;

    private StringFilter documento;

    private StringFilter email;

    private StringFilter telefono;

    private LocalDateFilter fechaNacimiento;

    private StringFilter direccion;

    private Boolean distinct;

    public ClientesCriteria() {}

    public ClientesCriteria(ClientesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ruc = other.ruc == null ? null : other.ruc.copy();
        this.nombres = other.nombres == null ? null : other.nombres.copy();
        this.apellidos = other.apellidos == null ? null : other.apellidos.copy();
        this.razonSocial = other.razonSocial == null ? null : other.razonSocial.copy();
        this.documento = other.documento == null ? null : other.documento.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefono = other.telefono == null ? null : other.telefono.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ClientesCriteria copy() {
        return new ClientesCriteria(this);
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

    public StringFilter getRuc() {
        return ruc;
    }

    public StringFilter ruc() {
        if (ruc == null) {
            ruc = new StringFilter();
        }
        return ruc;
    }

    public void setRuc(StringFilter ruc) {
        this.ruc = ruc;
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

    public StringFilter getRazonSocial() {
        return razonSocial;
    }

    public StringFilter razonSocial() {
        if (razonSocial == null) {
            razonSocial = new StringFilter();
        }
        return razonSocial;
    }

    public void setRazonSocial(StringFilter razonSocial) {
        this.razonSocial = razonSocial;
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

    public StringFilter getDireccion() {
        return direccion;
    }

    public StringFilter direccion() {
        if (direccion == null) {
            direccion = new StringFilter();
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
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
        final ClientesCriteria that = (ClientesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ruc, that.ruc) &&
            Objects.equals(nombres, that.nombres) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(razonSocial, that.razonSocial) &&
            Objects.equals(documento, that.documento) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruc, nombres, apellidos, razonSocial, documento, email, telefono, fechaNacimiento, direccion, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ruc != null ? "ruc=" + ruc + ", " : "") +
            (nombres != null ? "nombres=" + nombres + ", " : "") +
            (apellidos != null ? "apellidos=" + apellidos + ", " : "") +
            (razonSocial != null ? "razonSocial=" + razonSocial + ", " : "") +
            (documento != null ? "documento=" + documento + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (telefono != null ? "telefono=" + telefono + ", " : "") +
            (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
