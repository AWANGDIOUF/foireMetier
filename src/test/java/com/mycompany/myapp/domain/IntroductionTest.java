package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntroductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Introduction.class);
        Introduction introduction1 = new Introduction();
        introduction1.setId(1L);
        Introduction introduction2 = new Introduction();
        introduction2.setId(introduction1.getId());
        assertThat(introduction1).isEqualTo(introduction2);
        introduction2.setId(2L);
        assertThat(introduction1).isNotEqualTo(introduction2);
        introduction1.setId(null);
        assertThat(introduction1).isNotEqualTo(introduction2);
    }
}
