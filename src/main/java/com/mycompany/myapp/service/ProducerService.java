package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ProducerDTO;
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
     * @param producerDTO the entity to save.
     * @return the persisted entity.
     */
    ProducerDTO save(ProducerDTO producerDTO);

    /**
     * Updates a producer.
     *
     * @param producerDTO the entity to update.
     * @return the persisted entity.
     */
    ProducerDTO update(ProducerDTO producerDTO);

    /**
     * Partially updates a producer.
     *
     * @param producerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProducerDTO> partialUpdate(ProducerDTO producerDTO);

    /**
     * Get all the ProducerDTO where Seller is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<ProducerDTO> findAllWhereSellerIsNull();

    /**
     * Get the "id" producer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProducerDTO> findOne(UUID id);

    /**
     * Delete the "id" producer.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
