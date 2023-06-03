package py.com.abf.domain;

import java.util.List;

public class EvaluacionesConDetalle {

    public Evaluaciones cabecera;
    public List<EvaluacionesDetalleItem> detalle;

    public Evaluaciones getCabecera() {
        return cabecera;
    }

    public void setCabecera(Evaluaciones cabecera) {
        this.cabecera = cabecera;
    }

    public List<EvaluacionesDetalleItem> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<EvaluacionesDetalleItem> detalle) {
        this.detalle = detalle;
    }
}
