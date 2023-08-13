package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class TimbradosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timbrados.class);
        Timbrados timbrados1 = new Timbrados();
        timbrados1.setId(1L);
        Timbrados timbrados2 = new Timbrados();
        timbrados2.setId(timbrados1.getId());
        assertThat(timbrados1).isEqualTo(timbrados2);
        timbrados2.setId(2L);
        assertThat(timbrados1).isNotEqualTo(timbrados2);
        timbrados1.setId(null);
        assertThat(timbrados1).isNotEqualTo(timbrados2);
    }
}
