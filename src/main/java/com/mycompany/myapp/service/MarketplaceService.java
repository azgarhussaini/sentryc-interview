package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Marketplace;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Marketplace}.
 */
public interface MarketplaceService {
    /**
     * Save a marketplace.
     *
     * @param marketplace the entity to save.
     * @return the persisted entity.
     */
    Marketplace save(Marketplace marketplace);

    /**
     * Updates a marketplace.
     *
     * @param marketplace the entity to update.
     * @return the persisted entity.
     */
    Marketplace update(Marketplace marketplace);

    /**
     * Partially updates a marketplace.
     *
     * @param marketplace the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Marketplace> partialUpdate(Marketplace marketplace);

    /**
     * Get all the Marketplace where SellerInfo is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Marketplace> findAllWhereSellerInfoIsNull();

    /**
     * Get the "id" marketplace.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Marketplace> findOne(String id);

    /**
     * Delete the "id" marketplace.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
