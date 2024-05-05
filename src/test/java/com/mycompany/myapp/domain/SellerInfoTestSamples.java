package com.mycompany.myapp.domain;

import java.util.UUID;

public class SellerInfoTestSamples {

    public static SellerInfo getSellerInfoSample1() {
        return new SellerInfo()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .marketplaceName("marketplaceName1")
            .url("url1")
            .country("country1")
            .externalId("externalId1");
    }

    public static SellerInfo getSellerInfoSample2() {
        return new SellerInfo()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .marketplaceName("marketplaceName2")
            .url("url2")
            .country("country2")
            .externalId("externalId2");
    }

    public static SellerInfo getSellerInfoRandomSampleGenerator() {
        return new SellerInfo()
            .id(UUID.randomUUID())
            .marketplaceName(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .externalId(UUID.randomUUID().toString());
    }
}
