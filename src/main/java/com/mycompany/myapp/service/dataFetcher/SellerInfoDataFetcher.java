package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.repository.SellerRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellerInfoDataFetcher implements DataFetcher<List<SellerInfo>> {

    @Autowired
    SellerInfoRepository sellerInfoRepository;

    @Override
    public List<SellerInfo> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return sellerInfoRepository.findAll();
    }
}
