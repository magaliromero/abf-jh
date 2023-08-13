package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PuntoDeExpedicion.
 */
@Entity
@Table(name = "punto_de_expedicion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PuntoDeExpedicion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "numero_punto_de_expedicion", nullable = false)
    private String numeroPuntoDeExpedicion;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "puntoDeExpedicions", "timbrados" }, allowSetters = true)
    private Sucursales sucursales;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PuntoDeExpedicion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPuntoDeExpedicion() {
        return this.numeroPuntoDeExpedicion;
    }

    public PuntoDeExpedicion numeroPuntoDeExpedicion(String numeroPuntoDeExpedicion) {
        this.setNumeroPuntoDeExpedicion(numeroPuntoDeExpedicion);
        return this;
    }

    public void setNumeroPuntoDeExpedicion(String numeroPuntoDeExpedicion) {
        this.numeroPuntoDeExpedicion = numeroPuntoDeExpedicion;
    }

    public Sucursales getSucursales() {
        return this.sucursales;
    }

    public void setSucursales(Sucursales sucursales) {
        this.sucursales = sucursales;
    }

    public PuntoDeExpedicion sucursales(Sucursales sucursales) {
        this.setSucursales(sucursales);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PuntoDeExpedicion)) {
            return false;
        }
        return id != null && id.equals(((PuntoDeExpedicion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PuntoDeExpedicion{" +
            "id=" + getId() +
            ", numeroPuntoDeExpedicion='" + getNumeroPuntoDeExpedicion() + "'" +
            "}";
    }
}
