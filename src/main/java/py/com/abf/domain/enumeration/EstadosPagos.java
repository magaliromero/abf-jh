package py.com.abf.domain.enumeration;

/**
 * The EstadosPagos enumeration.
 */
public enum EstadosPagos {
    PAGADO("Pagado"),
    PENDIENTE("Pendiente"),
    ANULADO("Anulado");

    private final String value;

    EstadosPagos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
