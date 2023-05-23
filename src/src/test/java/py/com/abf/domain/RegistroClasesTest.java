package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class RegistroClasesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistroClases.class);
        RegistroClases registroClases1 = new RegistroClases();
        registroClases1.setId(1L);
        RegistroClases registroClases2 = new RegistroClases();
        registroClases2.setId(registroClases1.getId());
        assertThat(registroClases1).isEqualTo(registroClases2);
        registroClases2.setId(2L);
        assertThat(registroClases1).isNotEqualTo(registroClases2);
        registroClases1.setId(null);
        assertThat(registroClases1).isNotEqualTo(registroClases2);
    }
}
