package py.com.abf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import py.com.abf.domain.enumeration.EstadosPersona;
import py.com.abf.domain.enumeration.TipoFuncionarios;

/**
 * A Funcionarios.
 */
@Entity
@Table(name = "funcionarios")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Funcionarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "elo")
    private Integer elo;

    @Column(name = "fide_id")
    private Integer fideId;

    @NotNull
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotNull
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @NotNull
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Column(name = "documento", nullable = false)
    private String documento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadosPersona estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_funcionario")
    private TipoFuncionarios tipoFuncionario;

    @OneToMany(mappedBy = "funcionarios")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "alumnos", "funcionarios" }, allowSetters = true)
    private Set<Evaluaciones> evaluaciones = new HashSet<>();

    @OneToMany(mappedBy = "funcionario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "funcionario" }, allowSetters = true)
    private Set<Pagos> pagos = new HashSet<>();

    @OneToMany(mappedBy = "funcionario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "temas", "funcionario", "alumnos" }, allowSetters = true)
    private Set<RegistroClases> registroClases = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "alumnos", "funcionarios" }, allowSetters = true)
    private TiposDocumentos tipoDocumentos;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Funcionarios id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getElo() {
        return this.elo;
    }

    public Funcionarios elo(Integer elo) {
        this.setElo(elo);
        return this;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Integer getFideId() {
        return this.fideId;
    }

    public Funcionarios fideId(Integer fideId) {
        this.setFideId(fideId);
        return this;
    }

    public void setFideId(Integer fideId) {
        this.fideId = fideId;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Funcionarios nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Funcionarios apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public Funcionarios nombreCompleto(String nombreCompleto) {
        this.setNombreCompleto(nombreCompleto);
        return this;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return this.email;
    }

    public Funcionarios email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Funcionarios telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Funcionarios fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Funcionarios documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public EstadosPersona getEstado() {
        return this.estado;
    }

    public Funcionarios estado(EstadosPersona estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadosPersona estado) {
        this.estado = estado;
    }

    public TipoFuncionarios getTipoFuncionario() {
        return this.tipoFuncionario;
    }

    public Funcionarios tipoFuncionario(TipoFuncionarios tipoFuncionario) {
        this.setTipoFuncionario(tipoFuncionario);
        return this;
    }

    public void setTipoFuncionario(TipoFuncionarios tipoFuncionario) {
        this.tipoFuncionario = tipoFuncionario;
    }

    public Set<Evaluaciones> getEvaluaciones() {
        return this.evaluaciones;
    }

    public void setEvaluaciones(Set<Evaluaciones> evaluaciones) {
        if (this.evaluaciones != null) {
            this.evaluaciones.forEach(i -> i.setFuncionarios(null));
        }
        if (evaluaciones != null) {
            evaluaciones.forEach(i -> i.setFuncionarios(this));
        }
        this.evaluaciones = evaluaciones;
    }

    public Funcionarios evaluaciones(Set<Evaluaciones> evaluaciones) {
        this.setEvaluaciones(evaluaciones);
        return this;
    }

    public Funcionarios addEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones.add(evaluaciones);
        evaluaciones.setFuncionarios(this);
        return this;
    }

    public Funcionarios removeEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones.remove(evaluaciones);
        evaluaciones.setFuncionarios(null);
        return this;
    }

    public Set<Pagos> getPagos() {
        return this.pagos;
    }

    public void setPagos(Set<Pagos> pagos) {
        if (this.pagos != null) {
            this.pagos.forEach(i -> i.setFuncionario(null));
        }
        if (pagos != null) {
            pagos.forEach(i -> i.setFuncionario(this));
        }
        this.pagos = pagos;
    }

    public Funcionarios pagos(Set<Pagos> pagos) {
        this.setPagos(pagos);
        return this;
    }

    public Funcionarios addPagos(Pagos pagos) {
        this.pagos.add(pagos);
        pagos.setFuncionario(this);
        return this;
    }

    public Funcionarios removePagos(Pagos pagos) {
        this.pagos.remove(pagos);
        pagos.setFuncionario(null);
        return this;
    }

    public Set<RegistroClases> getRegistroClases() {
        return this.registroClases;
    }

    public void setRegistroClases(Set<RegistroClases> registroClases) {
        if (this.registroClases != null) {
            this.registroClases.forEach(i -> i.setFuncionario(null));
        }
        if (registroClases != null) {
            registroClases.forEach(i -> i.setFuncionario(this));
        }
        this.registroClases = registroClases;
    }

    public Funcionarios registroClases(Set<RegistroClases> registroClases) {
        this.setRegistroClases(registroClases);
        return this;
    }

    public Funcionarios addRegistroClases(RegistroClases registroClases) {
        this.registroClases.add(registroClases);
        registroClases.setFuncionario(this);
        return this;
    }

    public Funcionarios removeRegistroClases(RegistroClases registroClases) {
        this.registroClases.remove(registroClases);
        registroClases.setFuncionario(null);
        return this;
    }

    public TiposDocumentos getTipoDocumentos() {
        return this.tipoDocumentos;
    }

    public void setTipoDocumentos(TiposDocumentos tiposDocumentos) {
        this.tipoDocumentos = tiposDocumentos;
    }

    public Funcionarios tipoDocumentos(TiposDocumentos tiposDocumentos) {
        this.setTipoDocumentos(tiposDocumentos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcionarios)) {
            return false;
        }
        return id != null && id.equals(((Funcionarios) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Funcionarios{" +
            "id=" + getId() +
            ", elo=" + getElo() +
            ", fideId=" + getFideId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", estado='" + getEstado() + "'" +
            ", tipoFuncionario='" + getTipoFuncionario() + "'" +
            "}";
    }
}
