package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProducerTestSamples.*;
import static com.mycompany.myapp.domain.SellerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProducerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producer.class);
        Producer producer1 = getProducerSample1();
        Producer producer2 = new Producer();
        assertThat(producer1).isNotEqualTo(producer2);

        producer2.setId(producer1.getId());
        assertThat(producer1).isEqualTo(producer2);

        producer2 = getProducerSample2();
        assertThat(producer1).isNotEqualTo(producer2);
    }

    @Test
    void sellerTest() throws Exception {
        Producer producer = getProducerRandomSampleGenerator();
        Seller sellerBack = getSellerRandomSampleGenerator();

        producer.setSeller(sellerBack);
        assertThat(producer.getSeller()).isEqualTo(sellerBack);
        assertThat(sellerBack.getProducerId()).isEqualTo(producer);

        producer.seller(null);
        assertThat(producer.getSeller()).isNull();
        assertThat(sellerBack.getProducerId()).isNull();
    }
}
