package py.com.abf.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link py.com.abf.domain.FichaPartidasTorneos} entity. This class is used
 * in {@link py.com.abf.web.rest.FichaPartidasTorneosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ficha-partidas-torneos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichaPartidasTorneosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreContrincante;

    private IntegerFilter duracion;

    private StringFilter winner;

    private StringFilter resultado;

    private StringFilter comentarios;

    private StringFilter nombreArbitro;

    private LongFilter torneosId;

    private LongFilter alumnosId;

    private Boolean distinct;

    public FichaPartidasTorneosCriteria() {}

    public FichaPartidasTorneosCriteria(FichaPartidasTorneosCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreContrincante = other.nombreContrincante == null ? null : other.nombreContrincante.copy();
        this.duracion = other.duracion == null ? null : other.duracion.copy();
        this.winner = other.winner == null ? null : other.winner.copy();
        this.resultado = other.resultado == null ? null : other.resultado.copy();
        this.comentarios = other.comentarios == null ? null : other.comentarios.copy();
        this.nombreArbitro = other.nombreArbitro == null ? null : other.nombreArbitro.copy();
        this.torneosId = other.torneosId == null ? null : other.torneosId.copy();
        this.alumnosId = other.alumnosId == null ? null : other.alumnosId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FichaPartidasTorneosCriteria copy() {
        return new FichaPartidasTorneosCriteria(this);
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

    public StringFilter getNombreContrincante() {
        return nombreContrincante;
    }

    public StringFilter nombreContrincante() {
        if (nombreContrincante == null) {
            nombreContrincante = new StringFilter();
        }
        return nombreContrincante;
    }

    public void setNombreContrincante(StringFilter nombreContrincante) {
        this.nombreContrincante = nombreContrincante;
    }

    public IntegerFilter getDuracion() {
        return duracion;
    }

    public IntegerFilter duracion() {
        if (duracion == null) {
            duracion = new IntegerFilter();
        }
        return duracion;
    }

    public void setDuracion(IntegerFilter duracion) {
        this.duracion = duracion;
    }

    public StringFilter getWinner() {
        return winner;
    }

    public StringFilter winner() {
        if (winner == null) {
            winner = new StringFilter();
        }
        return winner;
    }

    public void setWinner(StringFilter winner) {
        this.winner = winner;
    }

    public StringFilter getResultado() {
        return resultado;
    }

    public StringFilter resultado() {
        if (resultado == null) {
            resultado = new StringFilter();
        }
        return resultado;
    }

    public void setResultado(StringFilter resultado) {
        this.resultado = resultado;
    }

    public StringFilter getComentarios() {
        return comentarios;
    }

    public StringFilter comentarios() {
        if (comentarios == null) {
            comentarios = new StringFilter();
        }
        return comentarios;
    }

    public void setComentarios(StringFilter comentarios) {
        this.comentarios = comentarios;
    }

    public StringFilter getNombreArbitro() {
        return nombreArbitro;
    }

    public StringFilter nombreArbitro() {
        if (nombreArbitro == null) {
            nombreArbitro = new StringFilter();
        }
        return nombreArbitro;
    }

    public void setNombreArbitro(StringFilter nombreArbitro) {
        this.nombreArbitro = nombreArbitro;
    }

    public LongFilter getTorneosId() {
        return torneosId;
    }

    public LongFilter torneosId() {
        if (torneosId == null) {
            torneosId = new LongFilter();
        }
        return torneosId;
    }

    public void setTorneosId(LongFilter torneosId) {
        this.torneosId = torneosId;
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
        final FichaPartidasTorneosCriteria that = (FichaPartidasTorneosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreContrincante, that.nombreContrincante) &&
            Objects.equals(duracion, that.duracion) &&
            Objects.equals(winner, that.winner) &&
            Objects.equals(resultado, that.resultado) &&
            Objects.equals(comentarios, that.comentarios) &&
            Objects.equals(nombreArbitro, that.nombreArbitro) &&
            Objects.equals(torneosId, that.torneosId) &&
            Objects.equals(alumnosId, that.alumnosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombreContrincante,
            duracion,
            winner,
            resultado,
            comentarios,
            nombreArbitro,
            torneosId,
            alumnosId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichaPartidasTorneosCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreContrincante != null ? "nombreContrincante=" + nombreContrincante + ", " : "") +
            (duracion != null ? "duracion=" + duracion + ", " : "") +
            (winner != null ? "winner=" + winner + ", " : "") +
            (resultado != null ? "resultado=" + resultado + ", " : "") +
            (comentarios != null ? "comentarios=" + comentarios + ", " : "") +
            (nombreArbitro != null ? "nombreArbitro=" + nombreArbitro + ", " : "") +
            (torneosId != null ? "torneosId=" + torneosId + ", " : "") +
            (alumnosId != null ? "alumnosId=" + alumnosId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
