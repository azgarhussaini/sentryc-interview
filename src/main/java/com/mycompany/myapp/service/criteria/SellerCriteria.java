package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.SellerState;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Seller} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SellerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sellers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SellerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SellerState
     */
    public static class SellerStateFilter extends Filter<SellerState> {

        public SellerStateFilter() {}

        public SellerStateFilter(SellerStateFilter filter) {
            super(filter);
        }

        @Override
        public SellerStateFilter copy() {
            return new SellerStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter sellerName;

    private SellerStateFilter state;

    private UUIDFilter producerIdId;

    private UUIDFilter sellerInfoIdId;

    private Boolean distinct;

    public SellerCriteria() {}

    public SellerCriteria(SellerCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.sellerName = other.optionalSellerName().map(StringFilter::copy).orElse(null);
        this.state = other.optionalState().map(SellerStateFilter::copy).orElse(null);
        this.producerIdId = other.optionalProducerIdId().map(UUIDFilter::copy).orElse(null);
        this.sellerInfoIdId = other.optionalSellerInfoIdId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SellerCriteria copy() {
        return new SellerCriteria(this);
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

    public StringFilter getSellerName() {
        return sellerName;
    }

    public Optional<StringFilter> optionalSellerName() {
        return Optional.ofNullable(sellerName);
    }

    public StringFilter sellerName() {
        if (sellerName == null) {
            setSellerName(new StringFilter());
        }
        return sellerName;
    }

    public void setSellerName(StringFilter sellerName) {
        this.sellerName = sellerName;
    }

    public SellerStateFilter getState() {
        return state;
    }

    public Optional<SellerStateFilter> optionalState() {
        return Optional.ofNullable(state);
    }

    public SellerStateFilter state() {
        if (state == null) {
            setState(new SellerStateFilter());
        }
        return state;
    }

    public void setState(SellerStateFilter state) {
        this.state = state;
    }

    public UUIDFilter getProducerIdId() {
        return producerIdId;
    }

    public Optional<UUIDFilter> optionalProducerIdId() {
        return Optional.ofNullable(producerIdId);
    }

    public UUIDFilter producerIdId() {
        if (producerIdId == null) {
            setProducerIdId(new UUIDFilter());
        }
        return producerIdId;
    }

    public void setProducerIdId(UUIDFilter producerIdId) {
        this.producerIdId = producerIdId;
    }

    public UUIDFilter getSellerInfoIdId() {
        return sellerInfoIdId;
    }

    public Optional<UUIDFilter> optionalSellerInfoIdId() {
        return Optional.ofNullable(sellerInfoIdId);
    }

    public UUIDFilter sellerInfoIdId() {
        if (sellerInfoIdId == null) {
            setSellerInfoIdId(new UUIDFilter());
        }
        return sellerInfoIdId;
    }

    public void setSellerInfoIdId(UUIDFilter sellerInfoIdId) {
        this.sellerInfoIdId = sellerInfoIdId;
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
        final SellerCriteria that = (SellerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sellerName, that.sellerName) &&
            Objects.equals(state, that.state) &&
            Objects.equals(producerIdId, that.producerIdId) &&
            Objects.equals(sellerInfoIdId, that.sellerInfoIdId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sellerName, state, producerIdId, sellerInfoIdId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSellerName().map(f -> "sellerName=" + f + ", ").orElse("") +
            optionalState().map(f -> "state=" + f + ", ").orElse("") +
            optionalProducerIdId().map(f -> "producerIdId=" + f + ", ").orElse("") +
            optionalSellerInfoIdId().map(f -> "sellerInfoIdId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
