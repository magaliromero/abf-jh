package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Sucursales} entity. This class is used
 * in {@link py.com.abf.web.rest.SucursalesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sucursales?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SucursalesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreSucursal;

    private StringFilter direccion;

    private StringFilter numeroEstablecimiento;

    private LongFilter puntoDeExpedicionId;

    private LongFilter timbradosId;

    private Boolean distinct;

    public SucursalesCriteria() {}

    public SucursalesCriteria(SucursalesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreSucursal = other.nombreSucursal == null ? null : other.nombreSucursal.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.numeroEstablecimiento = other.numeroEstablecimiento == null ? null : other.numeroEstablecimiento.copy();
        this.puntoDeExpedicionId = other.puntoDeExpedicionId == null ? null : other.puntoDeExpedicionId.copy();
        this.timbradosId = other.timbradosId == null ? null : other.timbradosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SucursalesCriteria copy() {
        return new SucursalesCriteria(this);
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

    public StringFilter getNombreSucursal() {
        return nombreSucursal;
    }

    public StringFilter nombreSucursal() {
        if (nombreSucursal == null) {
            nombreSucursal = new StringFilter();
        }
        return nombreSucursal;
    }

    public void setNombreSucursal(StringFilter nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
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

    public StringFilter getNumeroEstablecimiento() {
        return numeroEstablecimiento;
    }

    public StringFilter numeroEstablecimiento() {
        if (numeroEstablecimiento == null) {
            numeroEstablecimiento = new StringFilter();
        }
        return numeroEstablecimiento;
    }

    public void setNumeroEstablecimiento(StringFilter numeroEstablecimiento) {
        this.numeroEstablecimiento = numeroEstablecimiento;
    }

    public LongFilter getPuntoDeExpedicionId() {
        return puntoDeExpedicionId;
    }

    public LongFilter puntoDeExpedicionId() {
        if (puntoDeExpedicionId == null) {
            puntoDeExpedicionId = new LongFilter();
        }
        return puntoDeExpedicionId;
    }

    public void setPuntoDeExpedicionId(LongFilter puntoDeExpedicionId) {
        this.puntoDeExpedicionId = puntoDeExpedicionId;
    }

    public LongFilter getTimbradosId() {
        return timbradosId;
    }

    public LongFilter timbradosId() {
        if (timbradosId == null) {
            timbradosId = new LongFilter();
        }
        return timbradosId;
    }

    public void setTimbradosId(LongFilter timbradosId) {
        this.timbradosId = timbradosId;
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
        final SucursalesCriteria that = (SucursalesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreSucursal, that.nombreSucursal) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(numeroEstablecimiento, that.numeroEstablecimiento) &&
            Objects.equals(puntoDeExpedicionId, that.puntoDeExpedicionId) &&
            Objects.equals(timbradosId, that.timbradosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreSucursal, direccion, numeroEstablecimiento, puntoDeExpedicionId, timbradosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SucursalesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreSucursal != null ? "nombreSucursal=" + nombreSucursal + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (numeroEstablecimiento != null ? "numeroEstablecimiento=" + numeroEstablecimiento + ", " : "") +
            (puntoDeExpedicionId != null ? "puntoDeExpedicionId=" + puntoDeExpedicionId + ", " : "") +
            (timbradosId != null ? "timbradosId=" + timbradosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
