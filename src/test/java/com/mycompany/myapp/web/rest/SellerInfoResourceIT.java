package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SellerInfoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.repository.SellerInfoRepository;
import com.mycompany.myapp.service.dto.SellerInfoDTO;
import com.mycompany.myapp.service.mapper.SellerInfoMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SellerInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerInfoResourceIT {

    private static final String DEFAULT_MARKETPLACE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MARKETPLACE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seller-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Autowired
    private SellerInfoMapper sellerInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerInfoMockMvc;

    private SellerInfo sellerInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellerInfo createEntity(EntityManager em) {
        SellerInfo sellerInfo = new SellerInfo()
            .marketplaceName(DEFAULT_MARKETPLACE_NAME)
            .url(DEFAULT_URL)
            .country(DEFAULT_COUNTRY)
            .externalId(DEFAULT_EXTERNAL_ID);
        // Add required entity
        Marketplace marketplace;
        if (TestUtil.findAll(em, Marketplace.class).isEmpty()) {
            marketplace = MarketplaceResourceIT.createEntity(em);
            em.persist(marketplace);
            em.flush();
        } else {
            marketplace = TestUtil.findAll(em, Marketplace.class).get(0);
        }
        sellerInfo.setMarketplaceId(marketplace);
        return sellerInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SellerInfo createUpdatedEntity(EntityManager em) {
        SellerInfo sellerInfo = new SellerInfo()
            .marketplaceName(UPDATED_MARKETPLACE_NAME)
            .url(UPDATED_URL)
            .country(UPDATED_COUNTRY)
            .externalId(UPDATED_EXTERNAL_ID);
        // Add required entity
        Marketplace marketplace;
        if (TestUtil.findAll(em, Marketplace.class).isEmpty()) {
            marketplace = MarketplaceResourceIT.createUpdatedEntity(em);
            em.persist(marketplace);
            em.flush();
        } else {
            marketplace = TestUtil.findAll(em, Marketplace.class).get(0);
        }
        sellerInfo.setMarketplaceId(marketplace);
        return sellerInfo;
    }

    @BeforeEach
    public void initTest() {
        sellerInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createSellerInfo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);
        var returnedSellerInfoDTO = om.readValue(
            restSellerInfoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SellerInfoDTO.class
        );

        // Validate the SellerInfo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSellerInfo = sellerInfoMapper.toEntity(returnedSellerInfoDTO);
        assertSellerInfoUpdatableFieldsEquals(returnedSellerInfo, getPersistedSellerInfo(returnedSellerInfo));
    }

    @Test
    @Transactional
    void createSellerInfoWithExistingId() throws Exception {
        // Create the SellerInfo with an existing ID
        sellerInfoRepository.saveAndFlush(sellerInfo);
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellerInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMarketplaceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sellerInfo.setMarketplaceName(null);

        // Create the SellerInfo, which fails.
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        restSellerInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sellerInfo.setCountry(null);

        // Create the SellerInfo, which fails.
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        restSellerInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sellerInfo.setExternalId(null);

        // Create the SellerInfo, which fails.
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        restSellerInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSellerInfos() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellerInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].marketplaceName").value(hasItem(DEFAULT_MARKETPLACE_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)));
    }

    @Test
    @Transactional
    void getSellerInfo() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get the sellerInfo
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, sellerInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sellerInfo.getId().toString()))
            .andExpect(jsonPath("$.marketplaceName").value(DEFAULT_MARKETPLACE_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID));
    }

    @Test
    @Transactional
    void getSellerInfosByIdFiltering() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        UUID id = sellerInfo.getId();

        defaultSellerInfoFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where marketplaceName equals to
        defaultSellerInfoFiltering(
            "marketplaceName.equals=" + DEFAULT_MARKETPLACE_NAME,
            "marketplaceName.equals=" + UPDATED_MARKETPLACE_NAME
        );
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceNameIsInShouldWork() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where marketplaceName in
        defaultSellerInfoFiltering(
            "marketplaceName.in=" + DEFAULT_MARKETPLACE_NAME + "," + UPDATED_MARKETPLACE_NAME,
            "marketplaceName.in=" + UPDATED_MARKETPLACE_NAME
        );
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where marketplaceName is not null
        defaultSellerInfoFiltering("marketplaceName.specified=true", "marketplaceName.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceNameContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where marketplaceName contains
        defaultSellerInfoFiltering(
            "marketplaceName.contains=" + DEFAULT_MARKETPLACE_NAME,
            "marketplaceName.contains=" + UPDATED_MARKETPLACE_NAME
        );
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceNameNotContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where marketplaceName does not contain
        defaultSellerInfoFiltering(
            "marketplaceName.doesNotContain=" + UPDATED_MARKETPLACE_NAME,
            "marketplaceName.doesNotContain=" + DEFAULT_MARKETPLACE_NAME
        );
    }

    @Test
    @Transactional
    void getAllSellerInfosByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where url equals to
        defaultSellerInfoFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSellerInfosByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where url in
        defaultSellerInfoFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSellerInfosByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where url is not null
        defaultSellerInfoFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerInfosByUrlContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where url contains
        defaultSellerInfoFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllSellerInfosByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where url does not contain
        defaultSellerInfoFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllSellerInfosByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where country equals to
        defaultSellerInfoFiltering("country.equals=" + DEFAULT_COUNTRY, "country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSellerInfosByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where country in
        defaultSellerInfoFiltering("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY, "country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSellerInfosByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where country is not null
        defaultSellerInfoFiltering("country.specified=true", "country.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerInfosByCountryContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where country contains
        defaultSellerInfoFiltering("country.contains=" + DEFAULT_COUNTRY, "country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSellerInfosByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where country does not contain
        defaultSellerInfoFiltering("country.doesNotContain=" + UPDATED_COUNTRY, "country.doesNotContain=" + DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSellerInfosByExternalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where externalId equals to
        defaultSellerInfoFiltering("externalId.equals=" + DEFAULT_EXTERNAL_ID, "externalId.equals=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllSellerInfosByExternalIdIsInShouldWork() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where externalId in
        defaultSellerInfoFiltering(
            "externalId.in=" + DEFAULT_EXTERNAL_ID + "," + UPDATED_EXTERNAL_ID,
            "externalId.in=" + UPDATED_EXTERNAL_ID
        );
    }

    @Test
    @Transactional
    void getAllSellerInfosByExternalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where externalId is not null
        defaultSellerInfoFiltering("externalId.specified=true", "externalId.specified=false");
    }

    @Test
    @Transactional
    void getAllSellerInfosByExternalIdContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where externalId contains
        defaultSellerInfoFiltering("externalId.contains=" + DEFAULT_EXTERNAL_ID, "externalId.contains=" + UPDATED_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllSellerInfosByExternalIdNotContainsSomething() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        // Get all the sellerInfoList where externalId does not contain
        defaultSellerInfoFiltering("externalId.doesNotContain=" + UPDATED_EXTERNAL_ID, "externalId.doesNotContain=" + DEFAULT_EXTERNAL_ID);
    }

    @Test
    @Transactional
    void getAllSellerInfosByMarketplaceIdIsEqualToSomething() throws Exception {
        // Get already existing entity
        Marketplace marketplaceId = sellerInfo.getMarketplaceId();
        sellerInfoRepository.saveAndFlush(sellerInfo);
        String marketplaceIdId = marketplaceId.getId();
        // Get all the sellerInfoList where marketplaceId equals to marketplaceIdId
        defaultSellerInfoShouldBeFound("marketplaceIdId.equals=" + marketplaceIdId);

        // Get all the sellerInfoList where marketplaceId equals to "invalid-id"
        defaultSellerInfoShouldNotBeFound("marketplaceIdId.equals=" + "invalid-id");
    }

    private void defaultSellerInfoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSellerInfoShouldBeFound(shouldBeFound);
        defaultSellerInfoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSellerInfoShouldBeFound(String filter) throws Exception {
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sellerInfo.getId().toString())))
            .andExpect(jsonPath("$.[*].marketplaceName").value(hasItem(DEFAULT_MARKETPLACE_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].externalId").value(hasItem(DEFAULT_EXTERNAL_ID)));

        // Check, that the count call also returns 1
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSellerInfoShouldNotBeFound(String filter) throws Exception {
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSellerInfoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSellerInfo() throws Exception {
        // Get the sellerInfo
        restSellerInfoMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSellerInfo() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sellerInfo
        SellerInfo updatedSellerInfo = sellerInfoRepository.findById(sellerInfo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSellerInfo are not directly saved in db
        em.detach(updatedSellerInfo);
        updatedSellerInfo
            .marketplaceName(UPDATED_MARKETPLACE_NAME)
            .url(UPDATED_URL)
            .country(UPDATED_COUNTRY)
            .externalId(UPDATED_EXTERNAL_ID);
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(updatedSellerInfo);

        restSellerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sellerInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSellerInfoToMatchAllProperties(updatedSellerInfo);
    }

    @Test
    @Transactional
    void putNonExistingSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sellerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sellerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSellerInfoWithPatch() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sellerInfo using partial update
        SellerInfo partialUpdatedSellerInfo = new SellerInfo();
        partialUpdatedSellerInfo.setId(sellerInfo.getId());

        partialUpdatedSellerInfo.url(UPDATED_URL).country(UPDATED_COUNTRY);

        restSellerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSellerInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSellerInfo))
            )
            .andExpect(status().isOk());

        // Validate the SellerInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerInfoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSellerInfo, sellerInfo),
            getPersistedSellerInfo(sellerInfo)
        );
    }

    @Test
    @Transactional
    void fullUpdateSellerInfoWithPatch() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sellerInfo using partial update
        SellerInfo partialUpdatedSellerInfo = new SellerInfo();
        partialUpdatedSellerInfo.setId(sellerInfo.getId());

        partialUpdatedSellerInfo
            .marketplaceName(UPDATED_MARKETPLACE_NAME)
            .url(UPDATED_URL)
            .country(UPDATED_COUNTRY)
            .externalId(UPDATED_EXTERNAL_ID);

        restSellerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSellerInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSellerInfo))
            )
            .andExpect(status().isOk());

        // Validate the SellerInfo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerInfoUpdatableFieldsEquals(partialUpdatedSellerInfo, getPersistedSellerInfo(partialUpdatedSellerInfo));
    }

    @Test
    @Transactional
    void patchNonExistingSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sellerInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sellerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sellerInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSellerInfo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sellerInfo.setId(UUID.randomUUID());

        // Create the SellerInfo
        SellerInfoDTO sellerInfoDTO = sellerInfoMapper.toDto(sellerInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sellerInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SellerInfo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSellerInfo() throws Exception {
        // Initialize the database
        sellerInfoRepository.saveAndFlush(sellerInfo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sellerInfo
        restSellerInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, sellerInfo.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sellerInfoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SellerInfo getPersistedSellerInfo(SellerInfo sellerInfo) {
        return sellerInfoRepository.findById(sellerInfo.getId()).orElseThrow();
    }

    protected void assertPersistedSellerInfoToMatchAllProperties(SellerInfo expectedSellerInfo) {
        assertSellerInfoAllPropertiesEquals(expectedSellerInfo, getPersistedSellerInfo(expectedSellerInfo));
    }

    protected void assertPersistedSellerInfoToMatchUpdatableProperties(SellerInfo expectedSellerInfo) {
        assertSellerInfoAllUpdatablePropertiesEquals(expectedSellerInfo, getPersistedSellerInfo(expectedSellerInfo));
    }
}
