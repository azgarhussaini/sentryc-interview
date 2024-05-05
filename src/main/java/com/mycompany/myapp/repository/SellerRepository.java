package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Seller;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Seller entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID>, JpaSpecificationExecutor<Seller> {}
