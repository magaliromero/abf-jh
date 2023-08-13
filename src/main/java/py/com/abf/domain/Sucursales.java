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
 * A Sucursales.
 */
@Entity
@Table(name = "sucursales")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sucursales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre_sucursal", nullable = false)
    private String nombreSucursal;

    @Column(name = "direccion")
    private String direccion;

    @NotNull
    @Column(name = "numero_establecimiento", nullable = false)
    private String numeroEstablecimiento;

    @OneToMany(mappedBy = "sucursales")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sucursales" }, allowSetters = true)
    private Set<PuntoDeExpedicion> puntoDeExpedicions = new HashSet<>();

    @JsonIgnoreProperties(value = { "sucursales" }, allowSetters = true)
    @OneToOne(mappedBy = "sucursales")
    private Timbrados timbrados;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sucursales id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreSucursal() {
        return this.nombreSucursal;
    }

    public Sucursales nombreSucursal(String nombreSucursal) {
        this.setNombreSucursal(nombreSucursal);
        return this;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Sucursales direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroEstablecimiento() {
        return this.numeroEstablecimiento;
    }

    public Sucursales numeroEstablecimiento(String numeroEstablecimiento) {
        this.setNumeroEstablecimiento(numeroEstablecimiento);
        return this;
    }

    public void setNumeroEstablecimiento(String numeroEstablecimiento) {
        this.numeroEstablecimiento = numeroEstablecimiento;
    }

    public Set<PuntoDeExpedicion> getPuntoDeExpedicions() {
        return this.puntoDeExpedicions;
    }

    public void setPuntoDeExpedicions(Set<PuntoDeExpedicion> puntoDeExpedicions) {
        if (this.puntoDeExpedicions != null) {
            this.puntoDeExpedicions.forEach(i -> i.setSucursales(null));
        }
        if (puntoDeExpedicions != null) {
            puntoDeExpedicions.forEach(i -> i.setSucursales(this));
        }
        this.puntoDeExpedicions = puntoDeExpedicions;
    }

    public Sucursales puntoDeExpedicions(Set<PuntoDeExpedicion> puntoDeExpedicions) {
        this.setPuntoDeExpedicions(puntoDeExpedicions);
        return this;
    }

    public Sucursales addPuntoDeExpedicion(PuntoDeExpedicion puntoDeExpedicion) {
        this.puntoDeExpedicions.add(puntoDeExpedicion);
        puntoDeExpedicion.setSucursales(this);
        return this;
    }

    public Sucursales removePuntoDeExpedicion(PuntoDeExpedicion puntoDeExpedicion) {
        this.puntoDeExpedicions.remove(puntoDeExpedicion);
        puntoDeExpedicion.setSucursales(null);
        return this;
    }

    public Timbrados getTimbrados() {
        return this.timbrados;
    }

    public void setTimbrados(Timbrados timbrados) {
        if (this.timbrados != null) {
            this.timbrados.setSucursales(null);
        }
        if (timbrados != null) {
            timbrados.setSucursales(this);
        }
        this.timbrados = timbrados;
    }

    public Sucursales timbrados(Timbrados timbrados) {
        this.setTimbrados(timbrados);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sucursales)) {
            return false;
        }
        return id != null && id.equals(((Sucursales) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sucursales{" +
            "id=" + getId() +
            ", nombreSucursal='" + getNombreSucursal() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", numeroEstablecimiento='" + getNumeroEstablecimiento() + "'" +
            "}";
    }
}
