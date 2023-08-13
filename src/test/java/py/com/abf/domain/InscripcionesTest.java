package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class InscripcionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscripciones.class);
        Inscripciones inscripciones1 = new Inscripciones();
        inscripciones1.setId(1L);
        Inscripciones inscripciones2 = new Inscripciones();
        inscripciones2.setId(inscripciones1.getId());
        assertThat(inscripciones1).isEqualTo(inscripciones2);
        inscripciones2.setId(2L);
        assertThat(inscripciones1).isNotEqualTo(inscripciones2);
        inscripciones1.setId(null);
        assertThat(inscripciones1).isNotEqualTo(inscripciones2);
    }
}
