package com.mycompany.myapp.domain;

import java.util.UUID;

public class ProducerTestSamples {

    public static Producer getProducerSample1() {
        return new Producer().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).producerName("producerName1");
    }

    public static Producer getProducerSample2() {
        return new Producer().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).producerName("producerName2");
    }

    public static Producer getProducerRandomSampleGenerator() {
        return new Producer().id(UUID.randomUUID()).producerName(UUID.randomUUID().toString());
    }
}
