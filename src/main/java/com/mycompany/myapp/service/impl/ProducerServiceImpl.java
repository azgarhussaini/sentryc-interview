package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.repository.ProducerRepository;
import com.mycompany.myapp.service.ProducerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Producer}.
 */
@Service
@Transactional
public class ProducerServiceImpl implements ProducerService {

    private final Logger log = LoggerFactory.getLogger(ProducerServiceImpl.class);

    private final ProducerRepository producerRepository;

    public ProducerServiceImpl(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Override
    public Producer save(Producer producer) {
        log.debug("Request to save Producer : {}", producer);
        return producerRepository.save(producer);
    }

    @Override
    public Producer update(Producer producer) {
        log.debug("Request to update Producer : {}", producer);
        return producerRepository.save(producer);
    }

    @Override
    public Optional<Producer> partialUpdate(Producer producer) {
        log.debug("Request to partially update Producer : {}", producer);

        return producerRepository
            .findById(producer.getId())
            .map(existingProducer -> {
                if (producer.getProducerName() != null) {
                    existingProducer.setProducerName(producer.getProducerName());
                }
                if (producer.getCreatedAt() != null) {
                    existingProducer.setCreatedAt(producer.getCreatedAt());
                }
                if (producer.getProducerLogo() != null) {
                    existingProducer.setProducerLogo(producer.getProducerLogo());
                }
                if (producer.getProducerLogoContentType() != null) {
                    existingProducer.setProducerLogoContentType(producer.getProducerLogoContentType());
                }

                return existingProducer;
            })
            .map(producerRepository::save);
    }

    /**
     *  Get all the producers where Seller is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Producer> findAllWhereSellerIsNull() {
        log.debug("Request to get all producers where Seller is null");
        return StreamSupport.stream(producerRepository.findAll().spliterator(), false)
            .filter(producer -> producer.getSeller() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producer> findOne(UUID id) {
        log.debug("Request to get Producer : {}", id);
        return producerRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Producer : {}", id);
        producerRepository.deleteById(id);
    }
}
