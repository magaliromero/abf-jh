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

/**
 * A Alumnos.
 */
@Entity
@Table(name = "alumnos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alumnos implements Serializable {

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

    @Column(name = "email")
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

    @OneToMany(mappedBy = "alumnos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alumnos", "cursos" }, allowSetters = true)
    private Set<Inscripciones> inscripciones = new HashSet<>();

    @OneToMany(mappedBy = "alumnos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluacionesDetalles", "alumnos", "funcionarios" }, allowSetters = true)
    private Set<Evaluaciones> evaluaciones = new HashSet<>();

    @OneToMany(mappedBy = "alumno")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "alumno" }, allowSetters = true)
    private Set<Matricula> matriculas = new HashSet<>();

    @OneToMany(mappedBy = "alumnos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materiales", "alumnos" }, allowSetters = true)
    private Set<Prestamos> prestamos = new HashSet<>();

    @OneToMany(mappedBy = "alumnos")
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

    public Alumnos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getElo() {
        return this.elo;
    }

    public Alumnos elo(Integer elo) {
        this.setElo(elo);
        return this;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Integer getFideId() {
        return this.fideId;
    }

    public Alumnos fideId(Integer fideId) {
        this.setFideId(fideId);
        return this;
    }

    public void setFideId(Integer fideId) {
        this.fideId = fideId;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Alumnos nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Alumnos apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public Alumnos nombreCompleto(String nombreCompleto) {
        this.setNombreCompleto(nombreCompleto);
        return this;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return this.email;
    }

    public Alumnos email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Alumnos telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Alumnos fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Alumnos documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public EstadosPersona getEstado() {
        return this.estado;
    }

    public Alumnos estado(EstadosPersona estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadosPersona estado) {
        this.estado = estado;
    }

    public Set<Inscripciones> getInscripciones() {
        return this.inscripciones;
    }

    public void setInscripciones(Set<Inscripciones> inscripciones) {
        if (this.inscripciones != null) {
            this.inscripciones.forEach(i -> i.setAlumnos(null));
        }
        if (inscripciones != null) {
            inscripciones.forEach(i -> i.setAlumnos(this));
        }
        this.inscripciones = inscripciones;
    }

    public Alumnos inscripciones(Set<Inscripciones> inscripciones) {
        this.setInscripciones(inscripciones);
        return this;
    }

    public Alumnos addInscripciones(Inscripciones inscripciones) {
        this.inscripciones.add(inscripciones);
        inscripciones.setAlumnos(this);
        return this;
    }

    public Alumnos removeInscripciones(Inscripciones inscripciones) {
        this.inscripciones.remove(inscripciones);
        inscripciones.setAlumnos(null);
        return this;
    }

    public Set<Evaluaciones> getEvaluaciones() {
        return this.evaluaciones;
    }

    public void setEvaluaciones(Set<Evaluaciones> evaluaciones) {
        if (this.evaluaciones != null) {
            this.evaluaciones.forEach(i -> i.setAlumnos(null));
        }
        if (evaluaciones != null) {
            evaluaciones.forEach(i -> i.setAlumnos(this));
        }
        this.evaluaciones = evaluaciones;
    }

    public Alumnos evaluaciones(Set<Evaluaciones> evaluaciones) {
        this.setEvaluaciones(evaluaciones);
        return this;
    }

    public Alumnos addEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones.add(evaluaciones);
        evaluaciones.setAlumnos(this);
        return this;
    }

    public Alumnos removeEvaluaciones(Evaluaciones evaluaciones) {
        this.evaluaciones.remove(evaluaciones);
        evaluaciones.setAlumnos(null);
        return this;
    }

    public Set<Matricula> getMatriculas() {
        return this.matriculas;
    }

    public void setMatriculas(Set<Matricula> matriculas) {
        if (this.matriculas != null) {
            this.matriculas.forEach(i -> i.setAlumno(null));
        }
        if (matriculas != null) {
            matriculas.forEach(i -> i.setAlumno(this));
        }
        this.matriculas = matriculas;
    }

    public Alumnos matriculas(Set<Matricula> matriculas) {
        this.setMatriculas(matriculas);
        return this;
    }

    public Alumnos addMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
        matricula.setAlumno(this);
        return this;
    }

    public Alumnos removeMatricula(Matricula matricula) {
        this.matriculas.remove(matricula);
        matricula.setAlumno(null);
        return this;
    }

    public Set<Prestamos> getPrestamos() {
        return this.prestamos;
    }

    public void setPrestamos(Set<Prestamos> prestamos) {
        if (this.prestamos != null) {
            this.prestamos.forEach(i -> i.setAlumnos(null));
        }
        if (prestamos != null) {
            prestamos.forEach(i -> i.setAlumnos(this));
        }
        this.prestamos = prestamos;
    }

    public Alumnos prestamos(Set<Prestamos> prestamos) {
        this.setPrestamos(prestamos);
        return this;
    }

    public Alumnos addPrestamos(Prestamos prestamos) {
        this.prestamos.add(prestamos);
        prestamos.setAlumnos(this);
        return this;
    }

    public Alumnos removePrestamos(Prestamos prestamos) {
        this.prestamos.remove(prestamos);
        prestamos.setAlumnos(null);
        return this;
    }

    public Set<RegistroClases> getRegistroClases() {
        return this.registroClases;
    }

    public void setRegistroClases(Set<RegistroClases> registroClases) {
        if (this.registroClases != null) {
            this.registroClases.forEach(i -> i.setAlumnos(null));
        }
        if (registroClases != null) {
            registroClases.forEach(i -> i.setAlumnos(this));
        }
        this.registroClases = registroClases;
    }

    public Alumnos registroClases(Set<RegistroClases> registroClases) {
        this.setRegistroClases(registroClases);
        return this;
    }

    public Alumnos addRegistroClases(RegistroClases registroClases) {
        this.registroClases.add(registroClases);
        registroClases.setAlumnos(this);
        return this;
    }

    public Alumnos removeRegistroClases(RegistroClases registroClases) {
        this.registroClases.remove(registroClases);
        registroClases.setAlumnos(null);
        return this;
    }

    public TiposDocumentos getTipoDocumentos() {
        return this.tipoDocumentos;
    }

    public void setTipoDocumentos(TiposDocumentos tiposDocumentos) {
        this.tipoDocumentos = tiposDocumentos;
    }

    public Alumnos tipoDocumentos(TiposDocumentos tiposDocumentos) {
        this.setTipoDocumentos(tiposDocumentos);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alumnos)) {
            return false;
        }
        return id != null && id.equals(((Alumnos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alumnos{" +
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
            "}";
    }
}
