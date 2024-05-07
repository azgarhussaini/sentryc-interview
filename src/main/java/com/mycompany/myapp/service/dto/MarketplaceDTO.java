package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Marketplace} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketplaceDTO implements Serializable {

    @NotNull
    private String id;

    @NotNull
    private String description;

    @Lob
    private byte[] marketplaceLogo;

    private String marketplaceLogoContentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMarketplaceLogo() {
        return marketplaceLogo;
    }

    public void setMarketplaceLogo(byte[] marketplaceLogo) {
        this.marketplaceLogo = marketplaceLogo;
    }

    public String getMarketplaceLogoContentType() {
        return marketplaceLogoContentType;
    }

    public void setMarketplaceLogoContentType(String marketplaceLogoContentType) {
        this.marketplaceLogoContentType = marketplaceLogoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketplaceDTO)) {
            return false;
        }

        MarketplaceDTO marketplaceDTO = (MarketplaceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marketplaceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketplaceDTO{" +
            "id='" + getId() + "'" +
            ", description='" + getDescription() + "'" +
            ", marketplaceLogo='" + getMarketplaceLogo() + "'" +
            "}";
    }
}
