package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A Producer.
 */
@Entity
@Table(name = "producer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producer implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "producer_name", nullable = false)
    private String producerName;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Lob
    @Column(name = "producer_logo")
    private byte[] producerLogo;

    @Column(name = "producer_logo_content_type")
    private String producerLogoContentType;

    @JsonIgnoreProperties(value = { "producerId", "sellerInfoId" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "producerId")
    private Seller seller;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Producer id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProducerName() {
        return this.producerName;
    }

    public Producer producerName(String producerName) {
        this.setProducerName(producerName);
        return this;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Producer createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getProducerLogo() {
        return this.producerLogo;
    }

    public Producer producerLogo(byte[] producerLogo) {
        this.setProducerLogo(producerLogo);
        return this;
    }

    public void setProducerLogo(byte[] producerLogo) {
        this.producerLogo = producerLogo;
    }

    public String getProducerLogoContentType() {
        return this.producerLogoContentType;
    }

    public Producer producerLogoContentType(String producerLogoContentType) {
        this.producerLogoContentType = producerLogoContentType;
        return this;
    }

    public void setProducerLogoContentType(String producerLogoContentType) {
        this.producerLogoContentType = producerLogoContentType;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        if (this.seller != null) {
            this.seller.setProducerId(null);
        }
        if (seller != null) {
            seller.setProducerId(this);
        }
        this.seller = seller;
    }

    public Producer seller(Seller seller) {
        this.setSeller(seller);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producer)) {
            return false;
        }
        return getId() != null && getId().equals(((Producer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producer{" +
            "id=" + getId() +
            ", producerName='" + getProducerName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", producerLogo='" + getProducerLogo() + "'" +
            ", producerLogoContentType='" + getProducerLogoContentType() + "'" +
            "}";
    }
}
