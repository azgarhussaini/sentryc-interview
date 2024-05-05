package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.SellerState;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * A Seller.
 */
@Entity
@Table(name = "seller")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "seller_name", nullable = false)
    private String sellerName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private SellerState state;

    @JsonIgnoreProperties(value = { "seller" }, allowSetters = true)
    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(unique = true)
    private Producer producerId;

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties(value = { "marketplaceId" }, allowSetters = true)
    private SellerInfo sellerInfoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Seller id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSellerName() {
        return this.sellerName;
    }

    public Seller sellerName(String sellerName) {
        this.setSellerName(sellerName);
        return this;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public SellerState getState() {
        return this.state;
    }

    public Seller state(SellerState state) {
        this.setState(state);
        return this;
    }

    public void setState(SellerState state) {
        this.state = state;
    }

    public Producer getProducerId() {
        return this.producerId;
    }

    public void setProducerId(Producer producer) {
        this.producerId = producer;
    }

    public Seller producerId(Producer producer) {
        this.setProducerId(producer);
        return this;
    }

    public SellerInfo getSellerInfoId() {
        return this.sellerInfoId;
    }

    public void setSellerInfoId(SellerInfo sellerInfo) {
        this.sellerInfoId = sellerInfo;
    }

    public Seller sellerInfoId(SellerInfo sellerInfo) {
        this.setSellerInfoId(sellerInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller)) {
            return false;
        }
        return getId() != null && getId().equals(((Seller) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seller{" +
            "id=" + getId() +
            ", sellerName='" + getSellerName() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
