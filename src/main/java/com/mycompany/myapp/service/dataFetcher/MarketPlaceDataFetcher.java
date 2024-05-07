package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.mapper.MarketplaceMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MarketPlaceDataFetcher implements DataFetcher<List<MarketplaceDTO>> {

    private final MarketplaceRepository marketplaceRepository;
    private final MarketplaceMapper marketplaceMapper;

    public MarketPlaceDataFetcher(MarketplaceRepository marketplaceRepository, MarketplaceMapper marketplaceMapper) {
        this.marketplaceRepository = marketplaceRepository;
        this.marketplaceMapper = marketplaceMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketplaceDTO> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return marketplaceMapper.toDto(marketplaceRepository.findAll());
    }
}
