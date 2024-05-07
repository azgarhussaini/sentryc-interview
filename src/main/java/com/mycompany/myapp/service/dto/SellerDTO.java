package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.enumeration.SellerState;
import com.mycompany.myapp.service.mapper.EntityMapper;
import com.mycompany.myapp.service.mapper.ProducerMapper;
import com.mycompany.myapp.service.mapper.SellerInfoMapper;
import com.mycompany.myapp.service.mapper.SellerMapper;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A DTO for the {@link Seller} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private String sellerName;

    @NotNull
    private SellerState state;

    @NotNull
    private ProducerDTO producerId;

    @NotNull
    private SellerInfoDTO sellerInfoId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public SellerState getState() {
        return state;
    }

    public void setState(SellerState state) {
        this.state = state;
    }

    public ProducerDTO getProducerId() {
        return producerId;
    }

    public void setProducerId(ProducerDTO producerId) {
        this.producerId = producerId;
    }

    public SellerInfoDTO getSellerInfoId() {
        return sellerInfoId;
    }

    public void setSellerInfoId(SellerInfoDTO sellerInfoId) {
        this.sellerInfoId = sellerInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerDTO)) {
            return false;
        }

        SellerDTO sellerDTO = (SellerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sellerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerDTO{" +
            "id='" + getId() + "'" +
            ", sellerName='" + getSellerName() + "'" +
            ", state='" + getState() + "'" +
            ", producerId=" + getProducerId() +
            ", sellerInfoId=" + getSellerInfoId() +
            "}";
    }
}
