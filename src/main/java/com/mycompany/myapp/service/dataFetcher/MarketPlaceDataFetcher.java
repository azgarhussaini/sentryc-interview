package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.repository.SellerRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MarketPlaceDataFetcher implements DataFetcher<List<Marketplace>> {

    @Autowired
    MarketplaceRepository marketplaceRepository;

    @Override
    public List<Marketplace> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return marketplaceRepository.findAll();
    }
}
