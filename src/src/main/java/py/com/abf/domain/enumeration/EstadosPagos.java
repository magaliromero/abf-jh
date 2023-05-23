package py.com.abf.domain.enumeration;

/**
 * The EstadosPagos enumeration.
 */
public enum EstadosPagos {
    PAGADO("Pagado"),
    ANULADO("Anulado"),
    PENDIENTE("Pendiente");

    private final String value;

    EstadosPagos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
