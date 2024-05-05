package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.repository.SellerRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellersDataFetcher implements DataFetcher<List<Seller>> {

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public List<Seller> get(DataFetchingEnvironment dataFetchingEnvironment) {
        Iterable<Seller> df = sellerRepository.findAll();
        List<Seller> result = new ArrayList<>();
        df.forEach(result::add);
        return result;
    }
}
