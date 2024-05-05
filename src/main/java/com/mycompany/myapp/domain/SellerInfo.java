package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A SellerInfo.
 */
@Entity
@Table(name = "seller_info")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Size(max = 2048)
    @Column(name = "marketplace_name", length = 2048, nullable = false)
    private String marketplaceName;

    @Size(max = 2048)
    @Column(name = "url", length = 2048)
    private String url;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @JsonIgnoreProperties(value = { "sellerInfo" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Marketplace marketplaceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public SellerInfo id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarketplaceName() {
        return this.marketplaceName;
    }

    public SellerInfo marketplaceName(String marketplaceName) {
        this.setMarketplaceName(marketplaceName);
        return this;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public String getUrl() {
        return this.url;
    }

    public SellerInfo url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return this.country;
    }

    public SellerInfo country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public SellerInfo externalId(String externalId) {
        this.setExternalId(externalId);
        return this;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Marketplace getMarketplaceId() {
        return this.marketplaceId;
    }

    public void setMarketplaceId(Marketplace marketplace) {
        this.marketplaceId = marketplace;
    }

    public SellerInfo marketplaceId(Marketplace marketplace) {
        this.setMarketplaceId(marketplace);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SellerInfo)) {
            return false;
        }
        return getId() != null && getId().equals(((SellerInfo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerInfo{" +
            "id=" + getId() +
            ", marketplaceName='" + getMarketplaceName() + "'" +
            ", url='" + getUrl() + "'" +
            ", country='" + getCountry() + "'" +
            ", externalId='" + getExternalId() + "'" +
            "}";
    }
}
