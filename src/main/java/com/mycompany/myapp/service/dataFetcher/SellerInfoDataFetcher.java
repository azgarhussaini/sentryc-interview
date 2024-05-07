package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import com.mycompany.myapp.service.mapper.SellerInfoMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerInfoDataFetcher implements DataFetcher<List<SellerInfoDTO>> {

    private final SellerInfoRepository sellerInfoRepository;
    private final SellerInfoMapper sellerInfoMapper;

    public SellerInfoDataFetcher(SellerInfoRepository sellerInfoRepository, SellerInfoMapper sellerInfoMapper) {
        this.sellerInfoRepository = sellerInfoRepository;
        this.sellerInfoMapper = sellerInfoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerInfoDTO> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return sellerInfoMapper.toDto(sellerInfoRepository.findAll());
    }
}
