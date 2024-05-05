package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProducerTestSamples.*;
import static com.mycompany.myapp.domain.SellerInfoTestSamples.*;
import static com.mycompany.myapp.domain.SellerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SellerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Seller.class);
        Seller seller1 = getSellerSample1();
        Seller seller2 = new Seller();
        assertThat(seller1).isNotEqualTo(seller2);

        seller2.setId(seller1.getId());
        assertThat(seller1).isEqualTo(seller2);

        seller2 = getSellerSample2();
        assertThat(seller1).isNotEqualTo(seller2);
    }

    @Test
    void producerIdTest() throws Exception {
        Seller seller = getSellerRandomSampleGenerator();
        Producer producerBack = getProducerRandomSampleGenerator();

        seller.setProducerId(producerBack);
        assertThat(seller.getProducerId()).isEqualTo(producerBack);

        seller.producerId(null);
        assertThat(seller.getProducerId()).isNull();
    }

    @Test
    void sellerInfoIdTest() throws Exception {
        Seller seller = getSellerRandomSampleGenerator();
        SellerInfo sellerInfoBack = getSellerInfoRandomSampleGenerator();

        seller.setSellerInfoId(sellerInfoBack);
        assertThat(seller.getSellerInfoId()).isEqualTo(sellerInfoBack);

        seller.sellerInfoId(null);
        assertThat(seller.getSellerInfoId()).isNull();
    }
}
