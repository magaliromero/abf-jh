package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class PrestamosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestamos.class);
        Prestamos prestamos1 = new Prestamos();
        prestamos1.setId(1L);
        Prestamos prestamos2 = new Prestamos();
        prestamos2.setId(prestamos1.getId());
        assertThat(prestamos1).isEqualTo(prestamos2);
        prestamos2.setId(2L);
        assertThat(prestamos1).isNotEqualTo(prestamos2);
        prestamos1.setId(null);
        assertThat(prestamos1).isNotEqualTo(prestamos2);
    }
}
