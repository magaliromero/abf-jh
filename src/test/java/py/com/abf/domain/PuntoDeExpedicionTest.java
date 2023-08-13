package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class PuntoDeExpedicionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PuntoDeExpedicion.class);
        PuntoDeExpedicion puntoDeExpedicion1 = new PuntoDeExpedicion();
        puntoDeExpedicion1.setId(1L);
        PuntoDeExpedicion puntoDeExpedicion2 = new PuntoDeExpedicion();
        puntoDeExpedicion2.setId(puntoDeExpedicion1.getId());
        assertThat(puntoDeExpedicion1).isEqualTo(puntoDeExpedicion2);
        puntoDeExpedicion2.setId(2L);
        assertThat(puntoDeExpedicion1).isNotEqualTo(puntoDeExpedicion2);
        puntoDeExpedicion1.setId(null);
        assertThat(puntoDeExpedicion1).isNotEqualTo(puntoDeExpedicion2);
    }
}
