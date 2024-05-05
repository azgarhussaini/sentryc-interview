package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MarketplaceTestSamples.*;
import static com.mycompany.myapp.domain.SellerInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketplaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marketplace.class);
        Marketplace marketplace1 = getMarketplaceSample1();
        Marketplace marketplace2 = new Marketplace();
        assertThat(marketplace1).isNotEqualTo(marketplace2);

        marketplace2.setId(marketplace1.getId());
        assertThat(marketplace1).isEqualTo(marketplace2);

        marketplace2 = getMarketplaceSample2();
        assertThat(marketplace1).isNotEqualTo(marketplace2);
    }

    @Test
    void sellerInfoTest() throws Exception {
        Marketplace marketplace = getMarketplaceRandomSampleGenerator();
        SellerInfo sellerInfoBack = getSellerInfoRandomSampleGenerator();

        marketplace.setSellerInfo(sellerInfoBack);
        assertThat(marketplace.getSellerInfo()).isEqualTo(sellerInfoBack);
        assertThat(sellerInfoBack.getMarketplaceId()).isEqualTo(marketplace);

        marketplace.sellerInfo(null);
        assertThat(marketplace.getSellerInfo()).isNull();
        assertThat(sellerInfoBack.getMarketplaceId()).isNull();
    }
}
