package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Producer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Producer}.
 */
public interface ProducerService {
    /**
     * Save a producer.
     *
     * @param producer the entity to save.
     * @return the persisted entity.
     */
    Producer save(Producer producer);

    /**
     * Updates a producer.
     *
     * @param producer the entity to update.
     * @return the persisted entity.
     */
    Producer update(Producer producer);

    /**
     * Partially updates a producer.
     *
     * @param producer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Producer> partialUpdate(Producer producer);

    /**
     * Get all the Producer where Seller is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Producer> findAllWhereSellerIsNull();

    /**
     * Get the "id" producer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Producer> findOne(UUID id);

    /**
     * Delete the "id" producer.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
