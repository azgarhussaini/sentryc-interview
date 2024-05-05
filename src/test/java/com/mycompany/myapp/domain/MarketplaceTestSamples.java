package com.mycompany.myapp.domain;

import java.util.UUID;

public class MarketplaceTestSamples {

    public static Marketplace getMarketplaceSample1() {
        return new Marketplace().id("id1").description("description1");
    }

    public static Marketplace getMarketplaceSample2() {
        return new Marketplace().id("id2").description("description2");
    }

    public static Marketplace getMarketplaceRandomSampleGenerator() {
        return new Marketplace().id(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
