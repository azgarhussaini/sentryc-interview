package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SellerInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SellerInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo, UUID>, JpaSpecificationExecutor<SellerInfo> {}
