package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class FacturasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facturas.class);
        Facturas facturas1 = new Facturas();
        facturas1.setId(1L);
        Facturas facturas2 = new Facturas();
        facturas2.setId(facturas1.getId());
        assertThat(facturas1).isEqualTo(facturas2);
        facturas2.setId(2L);
        assertThat(facturas1).isNotEqualTo(facturas2);
        facturas1.setId(null);
        assertThat(facturas1).isNotEqualTo(facturas2);
    }
}
