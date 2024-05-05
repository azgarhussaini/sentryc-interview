package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producer;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Producer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProducerRepository extends JpaRepository<Producer, UUID>, JpaSpecificationExecutor<Producer> {}
