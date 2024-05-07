package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.SellerInfoDTO;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.SellerInfo}.
 */
public interface SellerInfoService {
    /**
     * Save a sellerInfo.
     *
     * @param sellerInfoDTO the entity to save.
     * @return the persisted entity.
     */
    SellerInfoDTO save(SellerInfoDTO sellerInfoDTO);

    /**
     * Updates a sellerInfo.
     *
     * @param sellerInfoDTO the entity to update.
     * @return the persisted entity.
     */
    SellerInfoDTO update(SellerInfoDTO sellerInfoDTO);

    /**
     * Partially updates a sellerInfo.
     *
     * @param sellerInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SellerInfoDTO> partialUpdate(SellerInfoDTO sellerInfoDTO);

    /**
     * Get the "id" sellerInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SellerInfoDTO> findOne(UUID id);

    /**
     * Delete the "id" sellerInfo.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
