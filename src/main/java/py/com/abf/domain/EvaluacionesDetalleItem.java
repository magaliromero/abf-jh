package py.com.abf.domain;

public class EvaluacionesDetalleItem {

    public Integer tema;
    public String comentarios;
    public Integer puntaje;

    public Integer getTema() {
        return tema;
    }

    public void setTema(Integer tema) {
        this.tema = tema;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
