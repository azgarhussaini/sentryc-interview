package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.repository.ProducerRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProducerDataFetcher implements DataFetcher<List<Producer>> {

    @Autowired
    ProducerRepository producerRepository;

    @Override
    public List<Producer> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return producerRepository.findAll();
    }
}
