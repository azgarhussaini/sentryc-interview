package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.criteria.SellerInfoCriteria;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import com.mycompany.myapp.service.mapper.SellerInfoMapper;
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
 * Service for executing complex queries for {@link SellerInfo} entities in the database.
 * The main input is a {@link SellerInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SellerInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SellerInfoQueryService extends QueryService<SellerInfo> {

    private final Logger log = LoggerFactory.getLogger(SellerInfoQueryService.class);

    private final SellerInfoRepository sellerInfoRepository;

    private final SellerInfoMapper sellerInfoMapper;

    public SellerInfoQueryService(SellerInfoRepository sellerInfoRepository, SellerInfoMapper sellerInfoMapper) {
        this.sellerInfoRepository = sellerInfoRepository;
        this.sellerInfoMapper = sellerInfoMapper;
    }

    /**
     * Return a {@link Page} of {@link SellerInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SellerInfoDTO> findByCriteria(SellerInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SellerInfo> specification = createSpecification(criteria);
        return sellerInfoRepository.findAll(specification, page).map(sellerInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SellerInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SellerInfo> specification = createSpecification(criteria);
        return sellerInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link SellerInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SellerInfo> createSpecification(SellerInfoCriteria criteria) {
        Specification<SellerInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SellerInfo_.id));
            }
            if (criteria.getMarketplaceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarketplaceName(), SellerInfo_.marketplaceName));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), SellerInfo_.url));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), SellerInfo_.country));
            }
            if (criteria.getExternalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExternalId(), SellerInfo_.externalId));
            }
            if (criteria.getMarketplaceIdId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getMarketplaceIdId(),
                        root -> root.join(SellerInfo_.marketplaceId, JoinType.LEFT).get(Marketplace_.id)
                    )
                );
            }
        }
        return specification;
    }
}
