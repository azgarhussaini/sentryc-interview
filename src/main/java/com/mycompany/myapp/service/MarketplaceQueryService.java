package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.criteria.MarketplaceCriteria;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.service.mapper.MarketplaceMapper;
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
 * Service for executing complex queries for {@link Marketplace} entities in the database.
 * The main input is a {@link MarketplaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MarketplaceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MarketplaceQueryService extends QueryService<Marketplace> {

    private final Logger log = LoggerFactory.getLogger(MarketplaceQueryService.class);

    private final MarketplaceRepository marketplaceRepository;

    private final MarketplaceMapper marketplaceMapper;

    public MarketplaceQueryService(MarketplaceRepository marketplaceRepository, MarketplaceMapper marketplaceMapper) {
        this.marketplaceRepository = marketplaceRepository;
        this.marketplaceMapper = marketplaceMapper;
    }

    /**
     * Return a {@link Page} of {@link MarketplaceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MarketplaceDTO> findByCriteria(MarketplaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Marketplace> specification = createSpecification(criteria);
        return marketplaceRepository.findAll(specification, page).map(marketplaceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MarketplaceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Marketplace> specification = createSpecification(criteria);
        return marketplaceRepository.count(specification);
    }

    /**
     * Function to convert {@link MarketplaceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Marketplace> createSpecification(MarketplaceCriteria criteria) {
        Specification<Marketplace> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), Marketplace_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Marketplace_.description));
            }
            if (criteria.getSellerInfoId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getSellerInfoId(),
                        root -> root.join(Marketplace_.sellerInfo, JoinType.LEFT).get(SellerInfo_.id)
                    )
                );
            }
        }
        return specification;
    }
}
