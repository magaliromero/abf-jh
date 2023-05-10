package py.com.abf.domain.enumeration;

/**
 * The TiposTorneos enumeration.
 */
public enum TiposTorneos {
    INTERNO("Interno"),
    NACIONAL("Nacional"),
    INTERNACIONAL("Internacional");

    private final String value;

    TiposTorneos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
