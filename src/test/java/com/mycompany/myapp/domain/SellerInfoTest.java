package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MarketplaceTestSamples.*;
import static com.mycompany.myapp.domain.SellerInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SellerInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellerInfo.class);
        SellerInfo sellerInfo1 = getSellerInfoSample1();
        SellerInfo sellerInfo2 = new SellerInfo();
        assertThat(sellerInfo1).isNotEqualTo(sellerInfo2);

        sellerInfo2.setId(sellerInfo1.getId());
        assertThat(sellerInfo1).isEqualTo(sellerInfo2);

        sellerInfo2 = getSellerInfoSample2();
        assertThat(sellerInfo1).isNotEqualTo(sellerInfo2);
    }

    @Test
    void marketplaceIdTest() throws Exception {
        SellerInfo sellerInfo = getSellerInfoRandomSampleGenerator();
        Marketplace marketplaceBack = getMarketplaceRandomSampleGenerator();

        sellerInfo.setMarketplaceId(marketplaceBack);
        assertThat(sellerInfo.getMarketplaceId()).isEqualTo(marketplaceBack);

        sellerInfo.marketplaceId(null);
        assertThat(sellerInfo.getMarketplaceId()).isNull();
    }
}
