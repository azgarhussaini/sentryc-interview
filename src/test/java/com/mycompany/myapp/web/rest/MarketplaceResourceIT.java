package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MarketplaceAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Marketplace;
import com.mycompany.myapp.repository.MarketplaceRepository;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link MarketplaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarketplaceResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_MARKETPLACE_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MARKETPLACE_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MARKETPLACE_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MARKETPLACE_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/marketplaces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MarketplaceRepository marketplaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarketplaceMockMvc;

    private Marketplace marketplace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marketplace createEntity(EntityManager em) {
        Marketplace marketplace = new Marketplace()
            .description(DEFAULT_DESCRIPTION)
            .marketplaceLogo(DEFAULT_MARKETPLACE_LOGO)
            .marketplaceLogoContentType(DEFAULT_MARKETPLACE_LOGO_CONTENT_TYPE);
        return marketplace;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marketplace createUpdatedEntity(EntityManager em) {
        Marketplace marketplace = new Marketplace()
            .description(UPDATED_DESCRIPTION)
            .marketplaceLogo(UPDATED_MARKETPLACE_LOGO)
            .marketplaceLogoContentType(UPDATED_MARKETPLACE_LOGO_CONTENT_TYPE);
        return marketplace;
    }

    @BeforeEach
    public void initTest() {
        marketplace = createEntity(em);
    }

    @Test
    @Transactional
    void createMarketplace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Marketplace
        var returnedMarketplace = om.readValue(
            restMarketplaceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketplace)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Marketplace.class
        );

        // Validate the Marketplace in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMarketplaceUpdatableFieldsEquals(returnedMarketplace, getPersistedMarketplace(returnedMarketplace));
    }

    @Test
    @Transactional
    void createMarketplaceWithExistingId() throws Exception {
        // Create the Marketplace with an existing ID
        marketplace.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketplaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketplace)))
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        marketplace.setDescription(null);

        // Create the Marketplace, which fails.

        restMarketplaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketplace)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarketplaces() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketplace.getId())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].marketplaceLogoContentType").value(hasItem(DEFAULT_MARKETPLACE_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].marketplaceLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MARKETPLACE_LOGO))));
    }

    @Test
    @Transactional
    void getMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get the marketplace
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL_ID, marketplace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marketplace.getId()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.marketplaceLogoContentType").value(DEFAULT_MARKETPLACE_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.marketplaceLogo").value(Base64.getEncoder().encodeToString(DEFAULT_MARKETPLACE_LOGO)));
    }

    @Test
    @Transactional
    void getMarketplacesByIdFiltering() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        String id = marketplace.getId();

        defaultMarketplaceFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllMarketplacesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList where description equals to
        defaultMarketplaceFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMarketplacesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList where description in
        defaultMarketplaceFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMarketplacesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList where description is not null
        defaultMarketplaceFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMarketplacesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList where description contains
        defaultMarketplaceFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMarketplacesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        // Get all the marketplaceList where description does not contain
        defaultMarketplaceFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultMarketplaceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMarketplaceShouldBeFound(shouldBeFound);
        defaultMarketplaceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMarketplaceShouldBeFound(String filter) throws Exception {
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketplace.getId())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].marketplaceLogoContentType").value(hasItem(DEFAULT_MARKETPLACE_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].marketplaceLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MARKETPLACE_LOGO))));

        // Check, that the count call also returns 1
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMarketplaceShouldNotBeFound(String filter) throws Exception {
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMarketplaceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMarketplace() throws Exception {
        // Get the marketplace
        restMarketplaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketplace
        Marketplace updatedMarketplace = marketplaceRepository.findById(marketplace.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMarketplace are not directly saved in db
        em.detach(updatedMarketplace);
        updatedMarketplace
            .description(UPDATED_DESCRIPTION)
            .marketplaceLogo(UPDATED_MARKETPLACE_LOGO)
            .marketplaceLogoContentType(UPDATED_MARKETPLACE_LOGO_CONTENT_TYPE);

        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMarketplace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMarketplace))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMarketplaceToMatchAllProperties(updatedMarketplace);
    }

    @Test
    @Transactional
    void putNonExistingMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marketplace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketplace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(marketplace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(marketplace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarketplaceWithPatch() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketplace using partial update
        Marketplace partialUpdatedMarketplace = new Marketplace();
        partialUpdatedMarketplace.setId(marketplace.getId());

        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketplace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketplace))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketplaceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMarketplace, marketplace),
            getPersistedMarketplace(marketplace)
        );
    }

    @Test
    @Transactional
    void fullUpdateMarketplaceWithPatch() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the marketplace using partial update
        Marketplace partialUpdatedMarketplace = new Marketplace();
        partialUpdatedMarketplace.setId(marketplace.getId());

        partialUpdatedMarketplace
            .description(UPDATED_DESCRIPTION)
            .marketplaceLogo(UPDATED_MARKETPLACE_LOGO)
            .marketplaceLogoContentType(UPDATED_MARKETPLACE_LOGO_CONTENT_TYPE);

        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarketplace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMarketplace))
            )
            .andExpect(status().isOk());

        // Validate the Marketplace in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMarketplaceUpdatableFieldsEquals(partialUpdatedMarketplace, getPersistedMarketplace(partialUpdatedMarketplace));
    }

    @Test
    @Transactional
    void patchNonExistingMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marketplace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketplace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(marketplace))
            )
            .andExpect(status().isBadRequest());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarketplace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        marketplace.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarketplaceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(marketplace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Marketplace in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMarketplace() throws Exception {
        // Initialize the database
        marketplaceRepository.saveAndFlush(marketplace);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the marketplace
        restMarketplaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, marketplace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return marketplaceRepository.count();
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

    protected Marketplace getPersistedMarketplace(Marketplace marketplace) {
        return marketplaceRepository.findById(marketplace.getId()).orElseThrow();
    }

    protected void assertPersistedMarketplaceToMatchAllProperties(Marketplace expectedMarketplace) {
        assertMarketplaceAllPropertiesEquals(expectedMarketplace, getPersistedMarketplace(expectedMarketplace));
    }

    protected void assertPersistedMarketplaceToMatchUpdatableProperties(Marketplace expectedMarketplace) {
        assertMarketplaceAllUpdatablePropertiesEquals(expectedMarketplace, getPersistedMarketplace(expectedMarketplace));
    }
}
