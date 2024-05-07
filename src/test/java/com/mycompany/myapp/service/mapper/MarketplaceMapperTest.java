package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MarketplaceAsserts.*;
import static com.mycompany.myapp.domain.MarketplaceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketplaceMapperTest {

    private MarketplaceMapper marketplaceMapper;

    @BeforeEach
    void setUp() {
        marketplaceMapper = new MarketplaceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMarketplaceSample1();
        var actual = marketplaceMapper.toEntity(marketplaceMapper.toDto(expected));
        assertMarketplaceAllPropertiesEquals(expected, actual);
    }
}
