package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Producer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProducerDTO implements Serializable {

    @NotNull
    private UUID id;

    @NotNull
    private String producerName;

    @NotNull
    private Instant createdAt;

    @Lob
    private byte[] producerLogo;

    private String producerLogoContentType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getProducerLogo() {
        return producerLogo;
    }

    public void setProducerLogo(byte[] producerLogo) {
        this.producerLogo = producerLogo;
    }

    public String getProducerLogoContentType() {
        return producerLogoContentType;
    }

    public void setProducerLogoContentType(String producerLogoContentType) {
        this.producerLogoContentType = producerLogoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProducerDTO)) {
            return false;
        }

        ProducerDTO producerDTO = (ProducerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, producerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProducerDTO{" +
            "id='" + getId() + "'" +
            ", producerName='" + getProducerName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", producerLogo='" + getProducerLogo() + "'" +
            "}";
    }
}
