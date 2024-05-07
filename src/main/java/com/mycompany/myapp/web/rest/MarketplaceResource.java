package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MarketplaceRepository;
import com.mycompany.myapp.service.MarketplaceQueryService;
import com.mycompany.myapp.service.MarketplaceService;
import com.mycompany.myapp.service.criteria.MarketplaceCriteria;
import com.mycompany.myapp.service.dto.MarketplaceDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Marketplace}.
 */
@RestController
@RequestMapping("/api/marketplaces")
public class MarketplaceResource {

    private final Logger log = LoggerFactory.getLogger(MarketplaceResource.class);

    private static final String ENTITY_NAME = "marketplace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketplaceService marketplaceService;

    private final MarketplaceRepository marketplaceRepository;

    private final MarketplaceQueryService marketplaceQueryService;

    public MarketplaceResource(
        MarketplaceService marketplaceService,
        MarketplaceRepository marketplaceRepository,
        MarketplaceQueryService marketplaceQueryService
    ) {
        this.marketplaceService = marketplaceService;
        this.marketplaceRepository = marketplaceRepository;
        this.marketplaceQueryService = marketplaceQueryService;
    }

    /**
     * {@code POST  /marketplaces} : Create a new marketplace.
     *
     * @param marketplaceDTO the marketplaceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marketplaceDTO, or with status {@code 400 (Bad Request)} if the marketplace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MarketplaceDTO> createMarketplace(@Valid @RequestBody MarketplaceDTO marketplaceDTO) throws URISyntaxException {
        log.debug("REST request to save Marketplace : {}", marketplaceDTO);
        if (marketplaceDTO.getId() != null) {
            throw new BadRequestAlertException("A new marketplace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        marketplaceDTO = marketplaceService.save(marketplaceDTO);
        return ResponseEntity.created(new URI("/api/marketplaces/" + marketplaceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, marketplaceDTO.getId()))
            .body(marketplaceDTO);
    }

    /**
     * {@code PUT  /marketplaces/:id} : Updates an existing marketplace.
     *
     * @param id the id of the marketplaceDTO to save.
     * @param marketplaceDTO the marketplaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketplaceDTO,
     * or with status {@code 400 (Bad Request)} if the marketplaceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketplaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarketplaceDTO> updateMarketplace(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody MarketplaceDTO marketplaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Marketplace : {}, {}", id, marketplaceDTO);
        if (marketplaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketplaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketplaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        marketplaceDTO = marketplaceService.update(marketplaceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketplaceDTO.getId()))
            .body(marketplaceDTO);
    }

    /**
     * {@code PATCH  /marketplaces/:id} : Partial updates given fields of an existing marketplace, field will ignore if it is null
     *
     * @param id the id of the marketplaceDTO to save.
     * @param marketplaceDTO the marketplaceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketplaceDTO,
     * or with status {@code 400 (Bad Request)} if the marketplaceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the marketplaceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketplaceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketplaceDTO> partialUpdateMarketplace(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody MarketplaceDTO marketplaceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Marketplace partially : {}, {}", id, marketplaceDTO);
        if (marketplaceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketplaceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketplaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketplaceDTO> result = marketplaceService.partialUpdate(marketplaceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketplaceDTO.getId())
        );
    }

    /**
     * {@code GET  /marketplaces} : get all the marketplaces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketplaces in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MarketplaceDTO>> getAllMarketplaces(
        MarketplaceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Marketplaces by criteria: {}", criteria);

        Page<MarketplaceDTO> page = marketplaceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marketplaces/count} : count all the marketplaces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMarketplaces(MarketplaceCriteria criteria) {
        log.debug("REST request to count Marketplaces by criteria: {}", criteria);
        return ResponseEntity.ok().body(marketplaceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /marketplaces/:id} : get the "id" marketplace.
     *
     * @param id the id of the marketplaceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketplaceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarketplaceDTO> getMarketplace(@PathVariable("id") String id) {
        log.debug("REST request to get Marketplace : {}", id);
        Optional<MarketplaceDTO> marketplaceDTO = marketplaceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marketplaceDTO);
    }

    /**
     * {@code DELETE  /marketplaces/:id} : delete the "id" marketplace.
     *
     * @param id the id of the marketplaceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketplace(@PathVariable("id") String id) {
        log.debug("REST request to delete Marketplace : {}", id);
        marketplaceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
