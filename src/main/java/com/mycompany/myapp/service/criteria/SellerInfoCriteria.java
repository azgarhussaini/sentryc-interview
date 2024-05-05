package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.SellerInfo} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SellerInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seller-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerInfoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter marketplaceName;

    private StringFilter url;

    private StringFilter country;

    private StringFilter externalId;

    private StringFilter marketplaceIdId;

    private Boolean distinct;

    public SellerInfoCriteria() {}

    public SellerInfoCriteria(SellerInfoCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.marketplaceName = other.optionalMarketplaceName().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.country = other.optionalCountry().map(StringFilter::copy).orElse(null);
        this.externalId = other.optionalExternalId().map(StringFilter::copy).orElse(null);
        this.marketplaceIdId = other.optionalMarketplaceIdId().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SellerInfoCriteria copy() {
        return new SellerInfoCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getMarketplaceName() {
        return marketplaceName;
    }

    public Optional<StringFilter> optionalMarketplaceName() {
        return Optional.ofNullable(marketplaceName);
    }

    public StringFilter marketplaceName() {
        if (marketplaceName == null) {
            setMarketplaceName(new StringFilter());
        }
        return marketplaceName;
    }

    public void setMarketplaceName(StringFilter marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getCountry() {
        return country;
    }

    public Optional<StringFilter> optionalCountry() {
        return Optional.ofNullable(country);
    }

    public StringFilter country() {
        if (country == null) {
            setCountry(new StringFilter());
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getExternalId() {
        return externalId;
    }

    public Optional<StringFilter> optionalExternalId() {
        return Optional.ofNullable(externalId);
    }

    public StringFilter externalId() {
        if (externalId == null) {
            setExternalId(new StringFilter());
        }
        return externalId;
    }

    public void setExternalId(StringFilter externalId) {
        this.externalId = externalId;
    }

    public StringFilter getMarketplaceIdId() {
        return marketplaceIdId;
    }

    public Optional<StringFilter> optionalMarketplaceIdId() {
        return Optional.ofNullable(marketplaceIdId);
    }

    public StringFilter marketplaceIdId() {
        if (marketplaceIdId == null) {
            setMarketplaceIdId(new StringFilter());
        }
        return marketplaceIdId;
    }

    public void setMarketplaceIdId(StringFilter marketplaceIdId) {
        this.marketplaceIdId = marketplaceIdId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SellerInfoCriteria that = (SellerInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(marketplaceName, that.marketplaceName) &&
            Objects.equals(url, that.url) &&
            Objects.equals(country, that.country) &&
            Objects.equals(externalId, that.externalId) &&
            Objects.equals(marketplaceIdId, that.marketplaceIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marketplaceName, url, country, externalId, marketplaceIdId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerInfoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMarketplaceName().map(f -> "marketplaceName=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalCountry().map(f -> "country=" + f + ", ").orElse("") +
            optionalExternalId().map(f -> "externalId=" + f + ", ").orElse("") +
            optionalMarketplaceIdId().map(f -> "marketplaceIdId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
