package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Marketplace.
 */
@Entity
@Table(name = "marketplace")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Marketplace implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "marketplace_logo")
    private byte[] marketplaceLogo;

    @Column(name = "marketplace_logo_content_type")
    private String marketplaceLogoContentType;

    @JsonIgnoreProperties(value = { "marketplaceId" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "marketplaceId")
    private SellerInfo sellerInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Marketplace id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Marketplace description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMarketplaceLogo() {
        return this.marketplaceLogo;
    }

    public Marketplace marketplaceLogo(byte[] marketplaceLogo) {
        this.setMarketplaceLogo(marketplaceLogo);
        return this;
    }

    public void setMarketplaceLogo(byte[] marketplaceLogo) {
        this.marketplaceLogo = marketplaceLogo;
    }

    public String getMarketplaceLogoContentType() {
        return this.marketplaceLogoContentType;
    }

    public Marketplace marketplaceLogoContentType(String marketplaceLogoContentType) {
        this.marketplaceLogoContentType = marketplaceLogoContentType;
        return this;
    }

    public void setMarketplaceLogoContentType(String marketplaceLogoContentType) {
        this.marketplaceLogoContentType = marketplaceLogoContentType;
    }

    public SellerInfo getSellerInfo() {
        return this.sellerInfo;
    }

    public void setSellerInfo(SellerInfo sellerInfo) {
        if (this.sellerInfo != null) {
            this.sellerInfo.setMarketplaceId(null);
        }
        if (sellerInfo != null) {
            sellerInfo.setMarketplaceId(this);
        }
        this.sellerInfo = sellerInfo;
    }

    public Marketplace sellerInfo(SellerInfo sellerInfo) {
        this.setSellerInfo(sellerInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marketplace)) {
            return false;
        }
        return getId() != null && getId().equals(((Marketplace) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marketplace{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", marketplaceLogo='" + getMarketplaceLogo() + "'" +
            ", marketplaceLogoContentType='" + getMarketplaceLogoContentType() + "'" +
            "}";
    }
}
