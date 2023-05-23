package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class FuncionariosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcionarios.class);
        Funcionarios funcionarios1 = new Funcionarios();
        funcionarios1.setId(1L);
        Funcionarios funcionarios2 = new Funcionarios();
        funcionarios2.setId(funcionarios1.getId());
        assertThat(funcionarios1).isEqualTo(funcionarios2);
        funcionarios2.setId(2L);
        assertThat(funcionarios1).isNotEqualTo(funcionarios2);
        funcionarios1.setId(null);
        assertThat(funcionarios1).isNotEqualTo(funcionarios2);
    }
}
