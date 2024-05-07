package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SellerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.SellerInfo;
import com.mycompany.myapp.domain.enumeration.SellerState;
import com.mycompany.myapp.repository.SellerRepository;
import com.mycompany.myapp.service.dto.SellerDTO;
import com.mycompany.myapp.service.mapper.SellerMapper;
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
 * Integration tests for the {@link SellerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerResourceIT {

    private static final String DEFAULT_SELLER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SELLER_NAME = "BBBBBBBBBB";

    private static final SellerState DEFAULT_STATE = SellerState.REGULAR;
    private static final SellerState UPDATED_STATE = SellerState.WHITELISTED;

    private static final String ENTITY_API_URL = "/api/sellers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerMockMvc;

    private Seller seller;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createEntity(EntityManager em) {
        Seller seller = new Seller().sellerName(DEFAULT_SELLER_NAME).state(DEFAULT_STATE);
        // Add required entity
        Producer producer;
        if (TestUtil.findAll(em, Producer.class).isEmpty()) {
            producer = ProducerResourceIT.createEntity(em);
            em.persist(producer);
            em.flush();
        } else {
            producer = TestUtil.findAll(em, Producer.class).get(0);
        }
        seller.setProducerId(producer);
        // Add required entity
        SellerInfo sellerInfo;
        if (TestUtil.findAll(em, SellerInfo.class).isEmpty()) {
            sellerInfo = SellerInfoResourceIT.createEntity(em);
            em.persist(sellerInfo);
            em.flush();
        } else {
            sellerInfo = TestUtil.findAll(em, SellerInfo.class).get(0);
        }
        seller.setSellerInfoId(sellerInfo);
        return seller;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createUpdatedEntity(EntityManager em) {
        Seller seller = new Seller().sellerName(UPDATED_SELLER_NAME).state(UPDATED_STATE);
        // Add required entity
        Producer producer;
        if (TestUtil.findAll(em, Producer.class).isEmpty()) {
            producer = ProducerResourceIT.createUpdatedEntity(em);
            em.persist(producer);
            em.flush();
        } else {
            producer = TestUtil.findAll(em, Producer.class).get(0);
        }
        seller.setProducerId(producer);
        // Add required entity
        SellerInfo sellerInfo;
        if (TestUtil.findAll(em, SellerInfo.class).isEmpty()) {
            sellerInfo = SellerInfoResourceIT.createUpdatedEntity(em);
            em.persist(sellerInfo);
            em.flush();
        } else {
            sellerInfo = TestUtil.findAll(em, SellerInfo.class).get(0);
        }
        seller.setSellerInfoId(sellerInfo);
        return seller;
    }

    @BeforeEach
    public void initTest() {
        seller = createEntity(em);
    }

    @Test
    @Transactional
    void createSeller() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);
        var returnedSellerDTO = om.readValue(
            restSellerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SellerDTO.class
        );

        // Validate the Seller in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSeller = sellerMapper.toEntity(returnedSellerDTO);
        assertSellerUpdatableFieldsEquals(returnedSeller, getPersistedSeller(returnedSeller));
    }

    @Test
    @Transactional
    void createSellerWithExistingId() throws Exception {
        // Create the Seller with an existing ID
        sellerRepository.saveAndFlush(seller);
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSellerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seller.setSellerName(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seller.setState(null);

        // Create the Seller, which fails.
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSellers() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().toString())))
            .andExpect(jsonPath("$.[*].sellerName").value(hasItem(DEFAULT_SELLER_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get the seller
        restSellerMockMvc
            .perform(get(ENTITY_API_URL_ID, seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seller.getId().toString()))
            .andExpect(jsonPath("$.sellerName").value(DEFAULT_SELLER_NAME))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getSellersByIdFiltering() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        UUID id = seller.getId();

        defaultSellerFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllSellersBySellerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where sellerName equals to
        defaultSellerFiltering("sellerName.equals=" + DEFAULT_SELLER_NAME, "sellerName.equals=" + UPDATED_SELLER_NAME);
    }

    @Test
    @Transactional
    void getAllSellersBySellerNameIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where sellerName in
        defaultSellerFiltering("sellerName.in=" + DEFAULT_SELLER_NAME + "," + UPDATED_SELLER_NAME, "sellerName.in=" + UPDATED_SELLER_NAME);
    }

    @Test
    @Transactional
    void getAllSellersBySellerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where sellerName is not null
        defaultSellerFiltering("sellerName.specified=true", "sellerName.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersBySellerNameContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where sellerName contains
        defaultSellerFiltering("sellerName.contains=" + DEFAULT_SELLER_NAME, "sellerName.contains=" + UPDATED_SELLER_NAME);
    }

    @Test
    @Transactional
    void getAllSellersBySellerNameNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where sellerName does not contain
        defaultSellerFiltering("sellerName.doesNotContain=" + UPDATED_SELLER_NAME, "sellerName.doesNotContain=" + DEFAULT_SELLER_NAME);
    }

    @Test
    @Transactional
    void getAllSellersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where state equals to
        defaultSellerFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllSellersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where state in
        defaultSellerFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllSellersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where state is not null
        defaultSellerFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByProducerIdIsEqualToSomething() throws Exception {
        // Get already existing entity
        Producer producerId = seller.getProducerId();
        sellerRepository.saveAndFlush(seller);
        UUID producerIdId = producerId.getId();
        // Get all the sellerList where producerId equals to producerIdId
        defaultSellerShouldBeFound("producerIdId.equals=" + producerIdId);

        // Get all the sellerList where producerId equals to UUID.randomUUID()
        defaultSellerShouldNotBeFound("producerIdId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllSellersBySellerInfoIdIsEqualToSomething() throws Exception {
        SellerInfo sellerInfoId;
        if (TestUtil.findAll(em, SellerInfo.class).isEmpty()) {
            sellerRepository.saveAndFlush(seller);
            sellerInfoId = SellerInfoResourceIT.createEntity(em);
        } else {
            sellerInfoId = TestUtil.findAll(em, SellerInfo.class).get(0);
        }
        em.persist(sellerInfoId);
        em.flush();
        seller.setSellerInfoId(sellerInfoId);
        sellerRepository.saveAndFlush(seller);
        UUID sellerInfoIdId = sellerInfoId.getId();
        // Get all the sellerList where sellerInfoId equals to sellerInfoIdId
        defaultSellerShouldBeFound("sellerInfoIdId.equals=" + sellerInfoIdId);

        // Get all the sellerList where sellerInfoId equals to UUID.randomUUID()
        defaultSellerShouldNotBeFound("sellerInfoIdId.equals=" + UUID.randomUUID());
    }

    private void defaultSellerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSellerShouldBeFound(shouldBeFound);
        defaultSellerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSellerShouldBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().toString())))
            .andExpect(jsonPath("$.[*].sellerName").value(hasItem(DEFAULT_SELLER_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSellerShouldNotBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeller() throws Exception {
        // Get the seller
        restSellerMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller
        Seller updatedSeller = sellerRepository.findById(seller.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeller are not directly saved in db
        em.detach(updatedSeller);
        updatedSeller.sellerName(UPDATED_SELLER_NAME).state(UPDATED_STATE);
        SellerDTO sellerDTO = sellerMapper.toDto(updatedSeller);

        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSellerToMatchAllProperties(updatedSeller);
    }

    @Test
    @Transactional
    void putNonExistingSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sellerDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sellerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller.sellerName(UPDATED_SELLER_NAME);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSeller, seller), getPersistedSeller(seller));
    }

    @Test
    @Transactional
    void fullUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller.sellerName(UPDATED_SELLER_NAME).state(UPDATED_STATE);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSellerUpdatableFieldsEquals(partialUpdatedSeller, getPersistedSeller(partialUpdatedSeller));
    }

    @Test
    @Transactional
    void patchNonExistingSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sellerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sellerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeller() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seller.setId(UUID.randomUUID());

        // Create the Seller
        SellerDTO sellerDTO = sellerMapper.toDto(seller);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sellerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the seller
        restSellerMockMvc
            .perform(delete(ENTITY_API_URL_ID, seller.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sellerRepository.count();
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

    protected Seller getPersistedSeller(Seller seller) {
        return sellerRepository.findById(seller.getId()).orElseThrow();
    }

    protected void assertPersistedSellerToMatchAllProperties(Seller expectedSeller) {
        assertSellerAllPropertiesEquals(expectedSeller, getPersistedSeller(expectedSeller));
    }

    protected void assertPersistedSellerToMatchUpdatableProperties(Seller expectedSeller) {
        assertSellerAllUpdatablePropertiesEquals(expectedSeller, getPersistedSeller(expectedSeller));
    }
}
