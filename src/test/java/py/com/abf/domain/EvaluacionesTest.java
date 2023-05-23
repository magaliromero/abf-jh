package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class EvaluacionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evaluaciones.class);
        Evaluaciones evaluaciones1 = new Evaluaciones();
        evaluaciones1.setId(1L);
        Evaluaciones evaluaciones2 = new Evaluaciones();
        evaluaciones2.setId(evaluaciones1.getId());
        assertThat(evaluaciones1).isEqualTo(evaluaciones2);
        evaluaciones2.setId(2L);
        assertThat(evaluaciones1).isNotEqualTo(evaluaciones2);
        evaluaciones1.setId(null);
        assertThat(evaluaciones1).isNotEqualTo(evaluaciones2);
    }
}
