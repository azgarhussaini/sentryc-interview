package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Marketplace} and its DTO {@link MarketplaceDTO}.
 */
@Mapper(componentModel = "spring")
public interface MarketplaceMapper extends EntityMapper<MarketplaceDTO, Marketplace> {}
