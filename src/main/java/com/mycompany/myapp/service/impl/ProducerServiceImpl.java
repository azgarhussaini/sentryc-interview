package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.repository.ProducerRepository;
import com.mycompany.myapp.service.ProducerService;
import com.mycompany.myapp.service.dto.ProducerDTO;
import com.mycompany.myapp.service.mapper.ProducerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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

    private final ProducerMapper producerMapper;

    public ProducerServiceImpl(ProducerRepository producerRepository, ProducerMapper producerMapper) {
        this.producerRepository = producerRepository;
        this.producerMapper = producerMapper;
    }

    @Override
    public ProducerDTO save(ProducerDTO producerDTO) {
        log.debug("Request to save Producer : {}", producerDTO);
        Producer producer = producerMapper.toEntity(producerDTO);
        producer = producerRepository.save(producer);
        return producerMapper.toDto(producer);
    }

    @Override
    public ProducerDTO update(ProducerDTO producerDTO) {
        log.debug("Request to update Producer : {}", producerDTO);
        Producer producer = producerMapper.toEntity(producerDTO);
        producer = producerRepository.save(producer);
        return producerMapper.toDto(producer);
    }

    @Override
    public Optional<ProducerDTO> partialUpdate(ProducerDTO producerDTO) {
        log.debug("Request to partially update Producer : {}", producerDTO);

        return producerRepository
            .findById(producerDTO.getId())
            .map(existingProducer -> {
                producerMapper.partialUpdate(existingProducer, producerDTO);

                return existingProducer;
            })
            .map(producerRepository::save)
            .map(producerMapper::toDto);
    }

    /**
     *  Get all the producers where Seller is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProducerDTO> findAllWhereSellerIsNull() {
        log.debug("Request to get all producers where Seller is null");
        return StreamSupport.stream(producerRepository.findAll().spliterator(), false)
            .filter(producer -> producer.getSeller() == null)
            .map(producerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProducerDTO> findOne(UUID id) {
        log.debug("Request to get Producer : {}", id);
        return producerRepository.findById(id).map(producerMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Producer : {}", id);
        producerRepository.deleteById(id);
    }
}
