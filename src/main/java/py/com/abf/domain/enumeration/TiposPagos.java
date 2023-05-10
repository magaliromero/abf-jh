package py.com.abf.domain.enumeration;

/**
 * The TiposPagos enumeration.
 */
public enum TiposPagos {
    PARCIAL("Parcial"),
    CUOTA("Cuota"),
    MENSUAL("Mensual"),
    DIARIO("Diario"),
    UNICO("Unico");

    private final String value;

    TiposPagos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
