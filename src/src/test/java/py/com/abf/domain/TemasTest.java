package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class TemasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Temas.class);
        Temas temas1 = new Temas();
        temas1.setId(1L);
        Temas temas2 = new Temas();
        temas2.setId(temas1.getId());
        assertThat(temas1).isEqualTo(temas2);
        temas2.setId(2L);
        assertThat(temas1).isNotEqualTo(temas2);
        temas1.setId(null);
        assertThat(temas1).isNotEqualTo(temas2);
    }
}
