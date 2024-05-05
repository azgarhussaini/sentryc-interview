package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.repository.ProducerRepository;
import com.mycompany.myapp.service.criteria.ProducerCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Producer} entities in the database.
 * The main input is a {@link ProducerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Producer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProducerQueryService extends QueryService<Producer> {

    private final Logger log = LoggerFactory.getLogger(ProducerQueryService.class);

    private final ProducerRepository producerRepository;

    public ProducerQueryService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    /**
     * Return a {@link Page} of {@link Producer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Producer> findByCriteria(ProducerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producer> specification = createSpecification(criteria);
        return producerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProducerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Producer> specification = createSpecification(criteria);
        return producerRepository.count(specification);
    }

    /**
     * Function to convert {@link ProducerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producer> createSpecification(ProducerCriteria criteria) {
        Specification<Producer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Producer_.id));
            }
            if (criteria.getProducerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProducerName(), Producer_.producerName));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Producer_.createdAt));
            }
            if (criteria.getSellerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSellerId(), root -> root.join(Producer_.seller, JoinType.LEFT).get(Seller_.id))
                );
            }
        }
        return specification;
    }
}
