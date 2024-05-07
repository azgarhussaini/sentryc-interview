package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.SellerInfoAsserts.*;
import static com.mycompany.myapp.domain.SellerInfoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SellerInfoMapperTest {

    private SellerInfoMapper sellerInfoMapper;

    @BeforeEach
    void setUp() {
        sellerInfoMapper = new SellerInfoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSellerInfoSample1();
        var actual = sellerInfoMapper.toEntity(sellerInfoMapper.toDto(expected));
        assertSellerInfoAllPropertiesEquals(expected, actual);
    }
}
