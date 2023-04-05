package py.com.abf.domain.enumeration;

/**
 * The TiposEvaluaciones enumeration.
 */
public enum TiposEvaluaciones {
    FINAL("Final"),
    ACUMULATIVA("Acumulativa"),
    FORMATIVA("Formativa");

    private final String value;

    TiposEvaluaciones(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
