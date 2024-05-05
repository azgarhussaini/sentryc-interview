package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Producer} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProducerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /producers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProducerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter producerName;

    private InstantFilter createdAt;

    private UUIDFilter sellerId;

    private Boolean distinct;

    public ProducerCriteria() {}

    public ProducerCriteria(ProducerCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.producerName = other.optionalProducerName().map(StringFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.sellerId = other.optionalSellerId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProducerCriteria copy() {
        return new ProducerCriteria(this);
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

    public StringFilter getProducerName() {
        return producerName;
    }

    public Optional<StringFilter> optionalProducerName() {
        return Optional.ofNullable(producerName);
    }

    public StringFilter producerName() {
        if (producerName == null) {
            setProducerName(new StringFilter());
        }
        return producerName;
    }

    public void setProducerName(StringFilter producerName) {
        this.producerName = producerName;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public UUIDFilter getSellerId() {
        return sellerId;
    }

    public Optional<UUIDFilter> optionalSellerId() {
        return Optional.ofNullable(sellerId);
    }

    public UUIDFilter sellerId() {
        if (sellerId == null) {
            setSellerId(new UUIDFilter());
        }
        return sellerId;
    }

    public void setSellerId(UUIDFilter sellerId) {
        this.sellerId = sellerId;
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
        final ProducerCriteria that = (ProducerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(producerName, that.producerName) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, producerName, createdAt, sellerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProducerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalProducerName().map(f -> "producerName=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalSellerId().map(f -> "sellerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
