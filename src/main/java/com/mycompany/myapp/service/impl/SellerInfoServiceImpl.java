package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.SellerInfoService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SellerInfo}.
 */
@Service
@Transactional
public class SellerInfoServiceImpl implements SellerInfoService {

    private final Logger log = LoggerFactory.getLogger(SellerInfoServiceImpl.class);

    private final SellerInfoRepository sellerInfoRepository;

    public SellerInfoServiceImpl(SellerInfoRepository sellerInfoRepository) {
        this.sellerInfoRepository = sellerInfoRepository;
    }

    @Override
    public SellerInfo save(SellerInfo sellerInfo) {
        log.debug("Request to save SellerInfo : {}", sellerInfo);
        return sellerInfoRepository.save(sellerInfo);
    }

    @Override
    public SellerInfo update(SellerInfo sellerInfo) {
        log.debug("Request to update SellerInfo : {}", sellerInfo);
        return sellerInfoRepository.save(sellerInfo);
    }

    @Override
    public Optional<SellerInfo> partialUpdate(SellerInfo sellerInfo) {
        log.debug("Request to partially update SellerInfo : {}", sellerInfo);

        return sellerInfoRepository
            .findById(sellerInfo.getId())
            .map(existingSellerInfo -> {
                if (sellerInfo.getMarketplaceName() != null) {
                    existingSellerInfo.setMarketplaceName(sellerInfo.getMarketplaceName());
                }
                if (sellerInfo.getUrl() != null) {
                    existingSellerInfo.setUrl(sellerInfo.getUrl());
                }
                if (sellerInfo.getCountry() != null) {
                    existingSellerInfo.setCountry(sellerInfo.getCountry());
                }
                if (sellerInfo.getExternalId() != null) {
                    existingSellerInfo.setExternalId(sellerInfo.getExternalId());
                }

                return existingSellerInfo;
            })
            .map(sellerInfoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SellerInfo> findOne(UUID id) {
        log.debug("Request to get SellerInfo : {}", id);
        return sellerInfoRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete SellerInfo : {}", id);
        sellerInfoRepository.deleteById(id);
    }
}
