package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.service.dto.ProducerDTO;
import com.mycompany.myapp.service.dto.SellerDTO;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Seller} and its DTO {@link SellerDTO}.
 */
@Mapper(componentModel = "spring")
public interface SellerMapper extends EntityMapper<SellerDTO, Seller> {
    @Mapping(target = "producerId", source = "producerId")
    @Mapping(target = "sellerInfoId", source = "sellerInfoId")
    SellerDTO toDto(Seller s);

    @Named("producerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProducerDTO toDtoProducerId(Producer producer);

    @Named("sellerInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SellerInfoDTO toDtoSellerInfoId(SellerInfo sellerInfo);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
