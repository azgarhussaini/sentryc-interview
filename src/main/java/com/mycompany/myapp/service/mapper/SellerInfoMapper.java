package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SellerInfo} and its DTO {@link SellerInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SellerInfoMapper extends EntityMapper<SellerInfoDTO, SellerInfo> {
    @Mapping(target = "marketplaceId", source = "marketplaceId", qualifiedByName = "marketplaceId")
    SellerInfoDTO toDto(SellerInfo s);

    @Named("marketplaceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MarketplaceDTO toDtoMarketplaceId(Marketplace marketplace);
}
