package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class CobrosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cobros.class);
        Cobros cobros1 = new Cobros();
        cobros1.setId(1L);
        Cobros cobros2 = new Cobros();
        cobros2.setId(cobros1.getId());
        assertThat(cobros1).isEqualTo(cobros2);
        cobros2.setId(2L);
        assertThat(cobros1).isNotEqualTo(cobros2);
        cobros1.setId(null);
        assertThat(cobros1).isNotEqualTo(cobros2);
    }
}
