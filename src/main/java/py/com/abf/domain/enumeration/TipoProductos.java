package py.com.abf.domain.enumeration;

/**
 * The TipoProductos enumeration.
 */
public enum TipoProductos {
    SERVICIO("Servicio"),
    PRODUCTO("Producto");

    private final String value;

    TipoProductos(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
