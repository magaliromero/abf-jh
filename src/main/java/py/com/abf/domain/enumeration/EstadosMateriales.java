package py.com.abf.domain.enumeration;

/**
 * The EstadosMateriales enumeration.
 */
public enum EstadosMateriales {
    DISPONIBLE("Disponible"),
    PRESTADO("En Prestamo"),
    VENCIDO("No devuelto");

    private final String value;

    EstadosMateriales(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
