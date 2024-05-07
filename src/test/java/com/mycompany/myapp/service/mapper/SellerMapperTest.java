package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.SellerAsserts.*;
import static com.mycompany.myapp.domain.SellerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SellerMapperTest {

    private SellerMapper sellerMapper;

    @BeforeEach
    void setUp() {
        sellerMapper = new SellerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSellerSample1();
        var actual = sellerMapper.toEntity(sellerMapper.toDto(expected));
        assertSellerAllPropertiesEquals(expected, actual);
    }
}
