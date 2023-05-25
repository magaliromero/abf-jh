package py.com.abf.domain;

import java.util.List;

public class FacturaConDetalle {

    public Facturas factura;
    public List<FacturaDetalleItem> detalle;

    public Facturas getFactura() {
        return factura;
    }

    public void setFactura(Facturas factura) {
        this.factura = factura;
    }

    public List<FacturaDetalleItem> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FacturaDetalleItem> detalle) {
        this.detalle = detalle;
    }
}
