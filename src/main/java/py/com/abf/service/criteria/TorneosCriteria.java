package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import py.com.abf.domain.enumeration.TiposTorneos;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.Torneos} entity. This class is used
 * in {@link py.com.abf.web.rest.TorneosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /torneos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TorneosCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TiposTorneos
     */
    public static class TiposTorneosFilter extends Filter<TiposTorneos> {

        public TiposTorneosFilter() {}

        public TiposTorneosFilter(TiposTorneosFilter filter) {
            super(filter);
        }

        @Override
        public TiposTorneosFilter copy() {
            return new TiposTorneosFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreTorneo;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private StringFilter lugar;

    private StringFilter tiempo;

    private TiposTorneosFilter tipoTorneo;

    private BooleanFilter torneoEvaluado;

    private BooleanFilter federado;

    private LongFilter fichaPartidasTorneosId;

    private Boolean distinct;

    public TorneosCriteria() {}

    public TorneosCriteria(TorneosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreTorneo = other.nombreTorneo == null ? null : other.nombreTorneo.copy();
        this.fechaInicio = other.fechaInicio == null ? null : other.fechaInicio.copy();
        this.fechaFin = other.fechaFin == null ? null : other.fechaFin.copy();
        this.lugar = other.lugar == null ? null : other.lugar.copy();
        this.tiempo = other.tiempo == null ? null : other.tiempo.copy();
        this.tipoTorneo = other.tipoTorneo == null ? null : other.tipoTorneo.copy();
        this.torneoEvaluado = other.torneoEvaluado == null ? null : other.torneoEvaluado.copy();
        this.federado = other.federado == null ? null : other.federado.copy();
        this.fichaPartidasTorneosId = other.fichaPartidasTorneosId == null ? null : other.fichaPartidasTorneosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TorneosCriteria copy() {
        return new TorneosCriteria(this);
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

    public StringFilter getNombreTorneo() {
        return nombreTorneo;
    }

    public StringFilter nombreTorneo() {
        if (nombreTorneo == null) {
            nombreTorneo = new StringFilter();
        }
        return nombreTorneo;
    }

    public void setNombreTorneo(StringFilter nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
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

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            fechaFin = new LocalDateFilter();
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
    }

    public StringFilter getLugar() {
        return lugar;
    }

    public StringFilter lugar() {
        if (lugar == null) {
            lugar = new StringFilter();
        }
        return lugar;
    }

    public void setLugar(StringFilter lugar) {
        this.lugar = lugar;
    }

    public StringFilter getTiempo() {
        return tiempo;
    }

    public StringFilter tiempo() {
        if (tiempo == null) {
            tiempo = new StringFilter();
        }
        return tiempo;
    }

    public void setTiempo(StringFilter tiempo) {
        this.tiempo = tiempo;
    }

    public TiposTorneosFilter getTipoTorneo() {
        return tipoTorneo;
    }

    public TiposTorneosFilter tipoTorneo() {
        if (tipoTorneo == null) {
            tipoTorneo = new TiposTorneosFilter();
        }
        return tipoTorneo;
    }

    public void setTipoTorneo(TiposTorneosFilter tipoTorneo) {
        this.tipoTorneo = tipoTorneo;
    }

    public BooleanFilter getTorneoEvaluado() {
        return torneoEvaluado;
    }

    public BooleanFilter torneoEvaluado() {
        if (torneoEvaluado == null) {
            torneoEvaluado = new BooleanFilter();
        }
        return torneoEvaluado;
    }

    public void setTorneoEvaluado(BooleanFilter torneoEvaluado) {
        this.torneoEvaluado = torneoEvaluado;
    }

    public BooleanFilter getFederado() {
        return federado;
    }

    public BooleanFilter federado() {
        if (federado == null) {
            federado = new BooleanFilter();
        }
        return federado;
    }

    public void setFederado(BooleanFilter federado) {
        this.federado = federado;
    }

    public LongFilter getFichaPartidasTorneosId() {
        return fichaPartidasTorneosId;
    }

    public LongFilter fichaPartidasTorneosId() {
        if (fichaPartidasTorneosId == null) {
            fichaPartidasTorneosId = new LongFilter();
        }
        return fichaPartidasTorneosId;
    }

    public void setFichaPartidasTorneosId(LongFilter fichaPartidasTorneosId) {
        this.fichaPartidasTorneosId = fichaPartidasTorneosId;
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
        final TorneosCriteria that = (TorneosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreTorneo, that.nombreTorneo) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(lugar, that.lugar) &&
            Objects.equals(tiempo, that.tiempo) &&
            Objects.equals(tipoTorneo, that.tipoTorneo) &&
            Objects.equals(torneoEvaluado, that.torneoEvaluado) &&
            Objects.equals(federado, that.federado) &&
            Objects.equals(fichaPartidasTorneosId, that.fichaPartidasTorneosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombreTorneo,
            fechaInicio,
            fechaFin,
            lugar,
            tiempo,
            tipoTorneo,
            torneoEvaluado,
            federado,
            fichaPartidasTorneosId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TorneosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreTorneo != null ? "nombreTorneo=" + nombreTorneo + ", " : "") +
            (fechaInicio != null ? "fechaInicio=" + fechaInicio + ", " : "") +
            (fechaFin != null ? "fechaFin=" + fechaFin + ", " : "") +
            (lugar != null ? "lugar=" + lugar + ", " : "") +
            (tiempo != null ? "tiempo=" + tiempo + ", " : "") +
            (tipoTorneo != null ? "tipoTorneo=" + tipoTorneo + ", " : "") +
            (torneoEvaluado != null ? "torneoEvaluado=" + torneoEvaluado + ", " : "") +
            (federado != null ? "federado=" + federado + ", " : "") +
            (fichaPartidasTorneosId != null ? "fichaPartidasTorneosId=" + fichaPartidasTorneosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
