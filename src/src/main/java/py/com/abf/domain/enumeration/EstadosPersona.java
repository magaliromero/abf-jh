package py.com.abf.domain.enumeration;

/**
 * The EstadosPersona enumeration.
 */
public enum EstadosPersona {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String value;

    EstadosPersona(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
