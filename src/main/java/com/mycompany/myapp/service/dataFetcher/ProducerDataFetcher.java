package com.mycompany.myapp.service.dataFetcher;

import com.mycompany.myapp.repository.ProducerRepository;
import com.mycompany.myapp.service.dto.ProducerDTO;
import com.mycompany.myapp.service.mapper.ProducerMapper;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProducerDataFetcher implements DataFetcher<List<ProducerDTO>> {

    public ProducerDataFetcher(ProducerRepository producerRepository, ProducerMapper producerMapper) {
        this.producerRepository = producerRepository;
        this.producerMapper = producerMapper;
    }

    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProducerDTO> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return producerMapper.toDto(producerRepository.findAll());
    }
}
