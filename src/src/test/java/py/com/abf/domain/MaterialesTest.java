package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class MaterialesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Materiales.class);
        Materiales materiales1 = new Materiales();
        materiales1.setId(1L);
        Materiales materiales2 = new Materiales();
        materiales2.setId(materiales1.getId());
        assertThat(materiales1).isEqualTo(materiales2);
        materiales2.setId(2L);
        assertThat(materiales1).isNotEqualTo(materiales2);
        materiales1.setId(null);
        assertThat(materiales1).isNotEqualTo(materiales2);
    }
}
