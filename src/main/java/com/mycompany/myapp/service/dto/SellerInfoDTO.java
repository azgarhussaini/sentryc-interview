package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.SellerInfo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerInfoDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    @Size(max = 2048)
    private String marketplaceName;

    @Size(max = 2048)
    private String url;

    @NotNull
    private String country;

    @NotNull
    private String externalId;

    @NotNull
    private MarketplaceDTO marketplaceId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public MarketplaceDTO getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(MarketplaceDTO marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerInfoDTO)) {
            return false;
        }

        SellerInfoDTO sellerInfoDTO = (SellerInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sellerInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerInfoDTO{" +
            "id='" + getId() + "'" +
            ", marketplaceName='" + getMarketplaceName() + "'" +
            ", url='" + getUrl() + "'" +
            ", country='" + getCountry() + "'" +
            ", externalId='" + getExternalId() + "'" +
            ", marketplaceId=" + getMarketplaceId() +
            "}";
    }
}
