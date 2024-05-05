package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Marketplace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Marketplace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace, String>, JpaSpecificationExecutor<Marketplace> {}
