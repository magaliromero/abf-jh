package py.com.abf.domain.enumeration;

/**
 * The Niveles enumeration.
 */
public enum Niveles {
    PREAJEDREZ("Pre-Ajedrez"),
    INICIAL("Inicial"),
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado"),
    TODOS("Todos los niveles");

    private final String value;

    Niveles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
