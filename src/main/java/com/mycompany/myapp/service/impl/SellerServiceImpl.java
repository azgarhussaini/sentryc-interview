package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.repository.SellerRepository;
import com.mycompany.myapp.service.SellerService;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Seller}.
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller save(Seller seller) {
        log.debug("Request to save Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller update(Seller seller) {
        log.debug("Request to update Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    public Optional<Seller> partialUpdate(Seller seller) {
        log.debug("Request to partially update Seller : {}", seller);

        return sellerRepository
            .findById(seller.getId())
            .map(existingSeller -> {
                if (seller.getSellerName() != null) {
                    existingSeller.setSellerName(seller.getSellerName());
                }
                if (seller.getState() != null) {
                    existingSeller.setState(seller.getState());
                }

                return existingSeller;
            })
            .map(sellerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findOne(UUID id) {
        log.debug("Request to get Seller : {}", id);
        return sellerRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
    }
}
