package py.com.abf.domain.enumeration;

/**
 * The CondicionVenta enumeration.
 */
public enum CondicionVenta {
    CONTADO("Contado"),
    CREDITO("Credito");

    private final String value;

    CondicionVenta(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
