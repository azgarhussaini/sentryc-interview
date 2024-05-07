package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.service.dto.ProducerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producer} and its DTO {@link ProducerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProducerMapper extends EntityMapper<ProducerDTO, Producer> {}
