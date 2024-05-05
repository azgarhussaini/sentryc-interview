package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SellerInfo;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.SellerInfo}.
 */
public interface SellerInfoService {
    /**
     * Save a sellerInfo.
     *
     * @param sellerInfo the entity to save.
     * @return the persisted entity.
     */
    SellerInfo save(SellerInfo sellerInfo);

    /**
     * Updates a sellerInfo.
     *
     * @param sellerInfo the entity to update.
     * @return the persisted entity.
     */
    SellerInfo update(SellerInfo sellerInfo);

    /**
     * Partially updates a sellerInfo.
     *
     * @param sellerInfo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SellerInfo> partialUpdate(SellerInfo sellerInfo);

    /**
     * Get the "id" sellerInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SellerInfo> findOne(UUID id);

    /**
     * Delete the "id" sellerInfo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
