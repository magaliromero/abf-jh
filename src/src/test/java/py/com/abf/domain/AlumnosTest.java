package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class AlumnosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alumnos.class);
        Alumnos alumnos1 = new Alumnos();
        alumnos1.setId(1L);
        Alumnos alumnos2 = new Alumnos();
        alumnos2.setId(alumnos1.getId());
        assertThat(alumnos1).isEqualTo(alumnos2);
        alumnos2.setId(2L);
        assertThat(alumnos1).isNotEqualTo(alumnos2);
        alumnos1.setId(null);
        assertThat(alumnos1).isNotEqualTo(alumnos2);
    }
}
