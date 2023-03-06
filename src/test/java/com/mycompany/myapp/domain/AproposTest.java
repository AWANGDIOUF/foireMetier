package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AproposTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apropos.class);
        Apropos apropos1 = new Apropos();
        apropos1.setId(1L);
        Apropos apropos2 = new Apropos();
        apropos2.setId(apropos1.getId());
        assertThat(apropos1).isEqualTo(apropos2);
        apropos2.setId(2L);
        assertThat(apropos1).isNotEqualTo(apropos2);
        apropos1.setId(null);
        assertThat(apropos1).isNotEqualTo(apropos2);
    }
}
