package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProducerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProducerDTO.class);
        ProducerDTO producerDTO1 = new ProducerDTO();
        producerDTO1.setId(UUID.randomUUID());
        ProducerDTO producerDTO2 = new ProducerDTO();
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
        producerDTO2.setId(producerDTO1.getId());
        assertThat(producerDTO1).isEqualTo(producerDTO2);
        producerDTO2.setId(UUID.randomUUID());
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
        producerDTO1.setId(null);
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
    }
}
