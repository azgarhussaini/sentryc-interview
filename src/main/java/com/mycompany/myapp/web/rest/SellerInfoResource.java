package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.SellerInfoQueryService;
import com.mycompany.myapp.service.SellerInfoService;
import com.mycompany.myapp.service.criteria.SellerInfoCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SellerInfo}.
 */
@RestController
@RequestMapping("/api/seller-infos")
public class SellerInfoResource {

    private final Logger log = LoggerFactory.getLogger(SellerInfoResource.class);

    private static final String ENTITY_NAME = "sellerInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SellerInfoService sellerInfoService;

    private final SellerInfoRepository sellerInfoRepository;

    private final SellerInfoQueryService sellerInfoQueryService;

    public SellerInfoResource(
        SellerInfoService sellerInfoService,
        SellerInfoRepository sellerInfoRepository,
        SellerInfoQueryService sellerInfoQueryService
    ) {
        this.sellerInfoService = sellerInfoService;
        this.sellerInfoRepository = sellerInfoRepository;
        this.sellerInfoQueryService = sellerInfoQueryService;
    }

    /**
     * {@code POST  /seller-infos} : Create a new sellerInfo.
     *
     * @param sellerInfo the sellerInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sellerInfo, or with status {@code 400 (Bad Request)} if the sellerInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SellerInfo> createSellerInfo(@Valid @RequestBody SellerInfo sellerInfo) throws URISyntaxException {
        log.debug("REST request to save SellerInfo : {}", sellerInfo);
        if (sellerInfo.getId() != null) {
            throw new BadRequestAlertException("A new sellerInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sellerInfo = sellerInfoService.save(sellerInfo);
        return ResponseEntity.created(new URI("/api/seller-infos/" + sellerInfo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sellerInfo.getId().toString()))
            .body(sellerInfo);
    }

    /**
     * {@code PUT  /seller-infos/:id} : Updates an existing sellerInfo.
     *
     * @param id the id of the sellerInfo to save.
     * @param sellerInfo the sellerInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sellerInfo,
     * or with status {@code 400 (Bad Request)} if the sellerInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sellerInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SellerInfo> updateSellerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SellerInfo sellerInfo
    ) throws URISyntaxException {
        log.debug("REST request to update SellerInfo : {}, {}", id, sellerInfo);
        if (sellerInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sellerInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sellerInfo = sellerInfoService.update(sellerInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sellerInfo.getId().toString()))
            .body(sellerInfo);
    }

    /**
     * {@code PATCH  /seller-infos/:id} : Partial updates given fields of an existing sellerInfo, field will ignore if it is null
     *
     * @param id the id of the sellerInfo to save.
     * @param sellerInfo the sellerInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sellerInfo,
     * or with status {@code 400 (Bad Request)} if the sellerInfo is not valid,
     * or with status {@code 404 (Not Found)} if the sellerInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the sellerInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SellerInfo> partialUpdateSellerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SellerInfo sellerInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update SellerInfo partially : {}, {}", id, sellerInfo);
        if (sellerInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sellerInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SellerInfo> result = sellerInfoService.partialUpdate(sellerInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sellerInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /seller-infos} : get all the sellerInfos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sellerInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SellerInfo>> getAllSellerInfos(
        SellerInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SellerInfos by criteria: {}", criteria);

        Page<SellerInfo> page = sellerInfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seller-infos/count} : count all the sellerInfos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSellerInfos(SellerInfoCriteria criteria) {
        log.debug("REST request to count SellerInfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(sellerInfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seller-infos/:id} : get the "id" sellerInfo.
     *
     * @param id the id of the sellerInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sellerInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SellerInfo> getSellerInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get SellerInfo : {}", id);
        Optional<SellerInfo> sellerInfo = sellerInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sellerInfo);
    }

    /**
     * {@code DELETE  /seller-infos/:id} : delete the "id" sellerInfo.
     *
     * @param id the id of the sellerInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSellerInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to delete SellerInfo : {}", id);
        sellerInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
