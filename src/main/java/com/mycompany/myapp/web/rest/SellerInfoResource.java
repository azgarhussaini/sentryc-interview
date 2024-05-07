package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.SellerInfoQueryService;
import com.mycompany.myapp.service.SellerInfoService;
import com.mycompany.myapp.service.criteria.SellerInfoCriteria;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
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
     * @param sellerInfoDTO the sellerInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sellerInfoDTO, or with status {@code 400 (Bad Request)} if the sellerInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SellerInfoDTO> createSellerInfo(@Valid @RequestBody SellerInfoDTO sellerInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SellerInfo : {}", sellerInfoDTO);
        if (sellerInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new sellerInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sellerInfoDTO = sellerInfoService.save(sellerInfoDTO);
        return ResponseEntity.created(new URI("/api/seller-infos/" + sellerInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sellerInfoDTO.getId().toString()))
            .body(sellerInfoDTO);
    }

    /**
     * {@code PUT  /seller-infos/:id} : Updates an existing sellerInfo.
     *
     * @param id the id of the sellerInfoDTO to save.
     * @param sellerInfoDTO the sellerInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sellerInfoDTO,
     * or with status {@code 400 (Bad Request)} if the sellerInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sellerInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SellerInfoDTO> updateSellerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody SellerInfoDTO sellerInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SellerInfo : {}, {}", id, sellerInfoDTO);
        if (sellerInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sellerInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sellerInfoDTO = sellerInfoService.update(sellerInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sellerInfoDTO.getId().toString()))
            .body(sellerInfoDTO);
    }

    /**
     * {@code PATCH  /seller-infos/:id} : Partial updates given fields of an existing sellerInfo, field will ignore if it is null
     *
     * @param id the id of the sellerInfoDTO to save.
     * @param sellerInfoDTO the sellerInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sellerInfoDTO,
     * or with status {@code 400 (Bad Request)} if the sellerInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sellerInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sellerInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SellerInfoDTO> partialUpdateSellerInfo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody SellerInfoDTO sellerInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SellerInfo partially : {}, {}", id, sellerInfoDTO);
        if (sellerInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sellerInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sellerInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SellerInfoDTO> result = sellerInfoService.partialUpdate(sellerInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sellerInfoDTO.getId().toString())
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
    public ResponseEntity<List<SellerInfoDTO>> getAllSellerInfos(
        SellerInfoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get SellerInfos by criteria: {}", criteria);

        Page<SellerInfoDTO> page = sellerInfoQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the sellerInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sellerInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SellerInfoDTO> getSellerInfo(@PathVariable("id") UUID id) {
        log.debug("REST request to get SellerInfo : {}", id);
        Optional<SellerInfoDTO> sellerInfoDTO = sellerInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sellerInfoDTO);
    }

    /**
     * {@code DELETE  /seller-infos/:id} : delete the "id" sellerInfo.
     *
     * @param id the id of the sellerInfoDTO to delete.
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
