package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cursos.
 */
@Entity
@Table(name = "cursos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre_curso")
    private String nombreCurso;

    @OneToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cursos", "temas", "funcionarios", "alumnos" }, allowSetters = true)
    private Set<RegistroClases> registroClases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cursos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCurso() {
        return this.nombreCurso;
    }

    public Cursos nombreCurso(String nombreCurso) {
        this.setNombreCurso(nombreCurso);
        return this;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public Set<RegistroClases> getRegistroClases() {
        return this.registroClases;
    }

    public void setRegistroClases(Set<RegistroClases> registroClases) {
        if (this.registroClases != null) {
            this.registroClases.forEach(i -> i.setCursos(null));
        }
        if (registroClases != null) {
            registroClases.forEach(i -> i.setCursos(this));
        }
        this.registroClases = registroClases;
    }

    public Cursos registroClases(Set<RegistroClases> registroClases) {
        this.setRegistroClases(registroClases);
        return this;
    }

    public Cursos addRegistroClases(RegistroClases registroClases) {
        this.registroClases.add(registroClases);
        registroClases.setCursos(this);
        return this;
    }

    public Cursos removeRegistroClases(RegistroClases registroClases) {
        this.registroClases.remove(registroClases);
        registroClases.setCursos(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cursos)) {
            return false;
        }
        return id != null && id.equals(((Cursos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cursos{" +
            "id=" + getId() +
            ", nombreCurso='" + getNombreCurso() + "'" +
            "}";
    }
}
