package py.com.abf.domain.enumeration;

/**
 * The CondVenta enumeration.
 */
public enum CondVenta {
    CONTADO("Contado"),
    CREDITO("Credito");

    private final String value;

    CondVenta(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
