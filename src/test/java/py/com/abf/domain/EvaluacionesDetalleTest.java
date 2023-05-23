package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class EvaluacionesDetalleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluacionesDetalle.class);
        EvaluacionesDetalle evaluacionesDetalle1 = new EvaluacionesDetalle();
        evaluacionesDetalle1.setId(1L);
        EvaluacionesDetalle evaluacionesDetalle2 = new EvaluacionesDetalle();
        evaluacionesDetalle2.setId(evaluacionesDetalle1.getId());
        assertThat(evaluacionesDetalle1).isEqualTo(evaluacionesDetalle2);
        evaluacionesDetalle2.setId(2L);
        assertThat(evaluacionesDetalle1).isNotEqualTo(evaluacionesDetalle2);
        evaluacionesDetalle1.setId(null);
        assertThat(evaluacionesDetalle1).isNotEqualTo(evaluacionesDetalle2);
    }
}
