package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalonMetierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalonMetier.class);
        SalonMetier salonMetier1 = new SalonMetier();
        salonMetier1.setId(1L);
        SalonMetier salonMetier2 = new SalonMetier();
        salonMetier2.setId(salonMetier1.getId());
        assertThat(salonMetier1).isEqualTo(salonMetier2);
        salonMetier2.setId(2L);
        assertThat(salonMetier1).isNotEqualTo(salonMetier2);
        salonMetier1.setId(null);
        assertThat(salonMetier1).isNotEqualTo(salonMetier2);
    }
}
