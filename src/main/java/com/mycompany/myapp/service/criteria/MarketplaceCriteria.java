package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Marketplace} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MarketplaceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /marketplaces?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketplaceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter description;

    private UUIDFilter sellerInfoId;

    private Boolean distinct;

    public MarketplaceCriteria() {}

    public MarketplaceCriteria(MarketplaceCriteria other) {
        this.id = other.optionalId().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.sellerInfoId = other.optionalSellerInfoId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MarketplaceCriteria copy() {
        return new MarketplaceCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public Optional<StringFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public StringFilter id() {
        if (id == null) {
            setId(new StringFilter());
        }
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public UUIDFilter getSellerInfoId() {
        return sellerInfoId;
    }

    public Optional<UUIDFilter> optionalSellerInfoId() {
        return Optional.ofNullable(sellerInfoId);
    }

    public UUIDFilter sellerInfoId() {
        if (sellerInfoId == null) {
            setSellerInfoId(new UUIDFilter());
        }
        return sellerInfoId;
    }

    public void setSellerInfoId(UUIDFilter sellerInfoId) {
        this.sellerInfoId = sellerInfoId;
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
        final MarketplaceCriteria that = (MarketplaceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sellerInfoId, that.sellerInfoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, sellerInfoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketplaceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalSellerInfoId().map(f -> "sellerInfoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
