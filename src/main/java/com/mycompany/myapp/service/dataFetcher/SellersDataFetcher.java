package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.repository.SellerRepository;
import com.mycompany.myapp.service.dto.SellerDTO;
import com.mycompany.myapp.service.mapper.SellerMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellersDataFetcher implements DataFetcher<List<SellerDTO>> {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    @Autowired
    public SellersDataFetcher(SellerRepository sellerRepository, SellerMapper sellerMapper) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerDTO> get(DataFetchingEnvironment dataFetchingEnvironment) {
        List<SellerDTO> result = sellerMapper.toDto(sellerRepository.findAll());
        return result;
    }
}
