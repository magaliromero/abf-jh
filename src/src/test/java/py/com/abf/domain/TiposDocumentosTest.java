package py.com.abf.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import py.com.abf.web.rest.TestUtil;

class TiposDocumentosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TiposDocumentos.class);
        TiposDocumentos tiposDocumentos1 = new TiposDocumentos();
        tiposDocumentos1.setId(1L);
        TiposDocumentos tiposDocumentos2 = new TiposDocumentos();
        tiposDocumentos2.setId(tiposDocumentos1.getId());
        assertThat(tiposDocumentos1).isEqualTo(tiposDocumentos2);
        tiposDocumentos2.setId(2L);
        assertThat(tiposDocumentos1).isNotEqualTo(tiposDocumentos2);
        tiposDocumentos1.setId(null);
        assertThat(tiposDocumentos1).isNotEqualTo(tiposDocumentos2);
    }
}
