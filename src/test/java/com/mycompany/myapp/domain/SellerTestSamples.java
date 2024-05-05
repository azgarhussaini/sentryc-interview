package com.mycompany.myapp.domain;

import java.util.UUID;

public class SellerTestSamples {

    public static Seller getSellerSample1() {
        return new Seller().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).sellerName("sellerName1");
    }

    public static Seller getSellerSample2() {
        return new Seller().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).sellerName("sellerName2");
    }

    public static Seller getSellerRandomSampleGenerator() {
        return new Seller().id(UUID.randomUUID()).sellerName(UUID.randomUUID().toString());
    }
}
