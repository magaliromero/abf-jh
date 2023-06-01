package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TiposDocumentos.
 */
@Entity
@Table(name = "tipos_documentos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TiposDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "tipoDocumentos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "inscripciones", "evaluaciones", "matriculas", "prestamos", "registroClases", "tipoDocumentos" },
        allowSetters = true
    )
    private Set<Alumnos> alumnos = new HashSet<>();

    @OneToMany(mappedBy = "tipoDocumentos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluaciones", "pagos", "registroClases", "tipoDocumentos" }, allowSetters = true)
    private Set<Funcionarios> funcionarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TiposDocumentos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public TiposDocumentos codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public TiposDocumentos descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Alumnos> getAlumnos() {
        return this.alumnos;
    }

    public void setAlumnos(Set<Alumnos> alumnos) {
        if (this.alumnos != null) {
            this.alumnos.forEach(i -> i.setTipoDocumentos(null));
        }
        if (alumnos != null) {
            alumnos.forEach(i -> i.setTipoDocumentos(this));
        }
        this.alumnos = alumnos;
    }

    public TiposDocumentos alumnos(Set<Alumnos> alumnos) {
        this.setAlumnos(alumnos);
        return this;
    }

    public TiposDocumentos addAlumnos(Alumnos alumnos) {
        this.alumnos.add(alumnos);
        alumnos.setTipoDocumentos(this);
        return this;
    }

    public TiposDocumentos removeAlumnos(Alumnos alumnos) {
        this.alumnos.remove(alumnos);
        alumnos.setTipoDocumentos(null);
        return this;
    }

    public Set<Funcionarios> getFuncionarios() {
        return this.funcionarios;
    }

    public void setFuncionarios(Set<Funcionarios> funcionarios) {
        if (this.funcionarios != null) {
            this.funcionarios.forEach(i -> i.setTipoDocumentos(null));
        }
        if (funcionarios != null) {
            funcionarios.forEach(i -> i.setTipoDocumentos(this));
        }
        this.funcionarios = funcionarios;
    }

    public TiposDocumentos funcionarios(Set<Funcionarios> funcionarios) {
        this.setFuncionarios(funcionarios);
        return this;
    }

    public TiposDocumentos addFuncionarios(Funcionarios funcionarios) {
        this.funcionarios.add(funcionarios);
        funcionarios.setTipoDocumentos(this);
        return this;
    }

    public TiposDocumentos removeFuncionarios(Funcionarios funcionarios) {
        this.funcionarios.remove(funcionarios);
        funcionarios.setTipoDocumentos(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TiposDocumentos)) {
            return false;
        }
        return id != null && id.equals(((TiposDocumentos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TiposDocumentos{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
