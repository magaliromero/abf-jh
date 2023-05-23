package py.com.abf.domain.enumeration;

/**
 * The TipoFuncionarios enumeration.
 */
public enum TipoFuncionarios {
    PROFESORES("Profesores"),
    FUNCIONARIOS("Funcionarios"),
    OTRO("Otro");

    private final String value;

    TipoFuncionarios(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
