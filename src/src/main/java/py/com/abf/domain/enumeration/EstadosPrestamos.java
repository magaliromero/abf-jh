package py.com.abf.domain.enumeration;

/**
 * The EstadosPrestamos enumeration.
 */
public enum EstadosPrestamos {
    DEVUELTO("Devuelto"),
    PRESTADO("En Prestamo"),
    VENCIDO("No devuelto");

    private final String value;

    EstadosPrestamos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
