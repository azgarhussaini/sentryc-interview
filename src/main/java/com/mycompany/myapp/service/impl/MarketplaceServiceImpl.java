package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.MarketplaceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Marketplace}.
 */
@Service
@Transactional
public class MarketplaceServiceImpl implements MarketplaceService {

    private final Logger log = LoggerFactory.getLogger(MarketplaceServiceImpl.class);

    private final MarketplaceRepository marketplaceRepository;

    public MarketplaceServiceImpl(MarketplaceRepository marketplaceRepository) {
        this.marketplaceRepository = marketplaceRepository;
    }

    @Override
    public Marketplace save(Marketplace marketplace) {
        log.debug("Request to save Marketplace : {}", marketplace);
        return marketplaceRepository.save(marketplace);
    }

    @Override
    public Marketplace update(Marketplace marketplace) {
        log.debug("Request to update Marketplace : {}", marketplace);
        return marketplaceRepository.save(marketplace);
    }

    @Override
    public Optional<Marketplace> partialUpdate(Marketplace marketplace) {
        log.debug("Request to partially update Marketplace : {}", marketplace);

        return marketplaceRepository
            .findById(marketplace.getId())
            .map(existingMarketplace -> {
                if (marketplace.getDescription() != null) {
                    existingMarketplace.setDescription(marketplace.getDescription());
                }
                if (marketplace.getMarketplaceLogo() != null) {
                    existingMarketplace.setMarketplaceLogo(marketplace.getMarketplaceLogo());
                }
                if (marketplace.getMarketplaceLogoContentType() != null) {
                    existingMarketplace.setMarketplaceLogoContentType(marketplace.getMarketplaceLogoContentType());
                }

                return existingMarketplace;
            })
            .map(marketplaceRepository::save);
    }

    /**
     *  Get all the marketplaces where SellerInfo is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Marketplace> findAllWhereSellerInfoIsNull() {
        log.debug("Request to get all marketplaces where SellerInfo is null");
        return StreamSupport.stream(marketplaceRepository.findAll().spliterator(), false)
            .filter(marketplace -> marketplace.getSellerInfo() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Marketplace> findOne(String id) {
        log.debug("Request to get Marketplace : {}", id);
        return marketplaceRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Marketplace : {}", id);
        marketplaceRepository.deleteById(id);
    }
}
