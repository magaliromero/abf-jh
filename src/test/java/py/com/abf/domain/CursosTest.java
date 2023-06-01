package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class CursosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cursos.class);
        Cursos cursos1 = new Cursos();
        cursos1.setId(1L);
        Cursos cursos2 = new Cursos();
        cursos2.setId(cursos1.getId());
        assertThat(cursos1).isEqualTo(cursos2);
        cursos2.setId(2L);
        assertThat(cursos1).isNotEqualTo(cursos2);
        cursos1.setId(null);
        assertThat(cursos1).isNotEqualTo(cursos2);
    }
}
