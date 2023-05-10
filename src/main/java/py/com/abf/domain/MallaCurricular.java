package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.Niveles;

/**
 * A MallaCurricular.
 */
@Entity
@Table(name = "malla_curricular")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MallaCurricular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false)
    private Niveles nivel;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_malla_curricular__temas",
        joinColumns = @JoinColumn(name = "malla_curricular_id"),
        inverseJoinColumns = @JoinColumn(name = "temas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "registroClases", "mallaCurriculars" }, allowSetters = true)
    private Set<Temas> temas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MallaCurricular id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public MallaCurricular titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Niveles getNivel() {
        return this.nivel;
    }

    public MallaCurricular nivel(Niveles nivel) {
        this.setNivel(nivel);
        return this;
    }

    public void setNivel(Niveles nivel) {
        this.nivel = nivel;
    }

    public Set<Temas> getTemas() {
        return this.temas;
    }

    public void setTemas(Set<Temas> temas) {
        this.temas = temas;
    }

    public MallaCurricular temas(Set<Temas> temas) {
        this.setTemas(temas);
        return this;
    }

    public MallaCurricular addTemas(Temas temas) {
        this.temas.add(temas);
        temas.getMallaCurriculars().add(this);
        return this;
    }

    public MallaCurricular removeTemas(Temas temas) {
        this.temas.remove(temas);
        temas.getMallaCurriculars().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MallaCurricular)) {
            return false;
        }
        return id != null && id.equals(((MallaCurricular) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MallaCurricular{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", nivel='" + getNivel() + "'" +
            "}";
    }
}
