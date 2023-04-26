package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FichaPartidasTorneos.
 */
@Entity
@Table(name = "fichas")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichaPartidasTorneos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_contrincante")
    private String nombreContrincante;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "winner")
    private String winner;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "comentarios")
    private String comentarios;

    @Column(name = "nombre_arbitro")
    private String nombreArbitro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fichaPartidasTorneos" }, allowSetters = true)
    private Torneos torneos;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "matriculas", "registroClases", "cobros", "evaluaciones", "inscripciones", "fichaPartidasTorneos", "facturas", "tipoDocumentos",
        },
        allowSetters = true
    )
    private Alumnos alumnos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FichaPartidasTorneos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreContrincante() {
        return this.nombreContrincante;
    }

    public FichaPartidasTorneos nombreContrincante(String nombreContrincante) {
        this.setNombreContrincante(nombreContrincante);
        return this;
    }

    public void setNombreContrincante(String nombreContrincante) {
        this.nombreContrincante = nombreContrincante;
    }

    public Integer getDuracion() {
        return this.duracion;
    }

    public FichaPartidasTorneos duracion(Integer duracion) {
        this.setDuracion(duracion);
        return this;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getWinner() {
        return this.winner;
    }

    public FichaPartidasTorneos winner(String winner) {
        this.setWinner(winner);
        return this;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getResultado() {
        return this.resultado;
    }

    public FichaPartidasTorneos resultado(String resultado) {
        this.setResultado(resultado);
        return this;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public FichaPartidasTorneos comentarios(String comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getNombreArbitro() {
        return this.nombreArbitro;
    }

    public FichaPartidasTorneos nombreArbitro(String nombreArbitro) {
        this.setNombreArbitro(nombreArbitro);
        return this;
    }

    public void setNombreArbitro(String nombreArbitro) {
        this.nombreArbitro = nombreArbitro;
    }

    public Torneos getTorneos() {
        return this.torneos;
    }

    public void setTorneos(Torneos torneos) {
        this.torneos = torneos;
    }

    public FichaPartidasTorneos torneos(Torneos torneos) {
        this.setTorneos(torneos);
        return this;
    }

    public Alumnos getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Alumnos alumnos) {
        this.alumnos = alumnos;
    }

    public FichaPartidasTorneos alumnos(Alumnos alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichaPartidasTorneos)) {
            return false;
        }
        return id != null && id.equals(((FichaPartidasTorneos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichaPartidasTorneos{" +
            "id=" + getId() +
            ", nombreContrincante='" + getNombreContrincante() + "'" +
            ", duracion=" + getDuracion() +
            ", winner='" + getWinner() + "'" +
            ", resultado='" + getResultado() + "'" +
            ", comentarios='" + getComentarios() + "'" +
            ", nombreArbitro='" + getNombreArbitro() + "'" +
            "}";
    }
}
