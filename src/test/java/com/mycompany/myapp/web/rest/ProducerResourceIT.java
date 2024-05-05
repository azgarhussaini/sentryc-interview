package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProducerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Producer;
import com.mycompany.myapp.repository.ProducerRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProducerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProducerResourceIT {

    private static final String DEFAULT_PRODUCER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_PRODUCER_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRODUCER_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRODUCER_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRODUCER_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/producers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProducerMockMvc;

    private Producer producer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createEntity(EntityManager em) {
        Producer producer = new Producer()
            .producerName(DEFAULT_PRODUCER_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .producerLogo(DEFAULT_PRODUCER_LOGO)
            .producerLogoContentType(DEFAULT_PRODUCER_LOGO_CONTENT_TYPE);
        return producer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createUpdatedEntity(EntityManager em) {
        Producer producer = new Producer()
            .producerName(UPDATED_PRODUCER_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .producerLogo(UPDATED_PRODUCER_LOGO)
            .producerLogoContentType(UPDATED_PRODUCER_LOGO_CONTENT_TYPE);
        return producer;
    }

    @BeforeEach
    public void initTest() {
        producer = createEntity(em);
    }

    @Test
    @Transactional
    void createProducer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Producer
        var returnedProducer = om.readValue(
            restProducerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Producer.class
        );

        // Validate the Producer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProducerUpdatableFieldsEquals(returnedProducer, getPersistedProducer(returnedProducer));
    }

    @Test
    @Transactional
    void createProducerWithExistingId() throws Exception {
        // Create the Producer with an existing ID
        producerRepository.saveAndFlush(producer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProducerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer)))
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProducerNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producer.setProducerName(null);

        // Create the Producer, which fails.

        restProducerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producer.setCreatedAt(null);

        // Create the Producer, which fails.

        restProducerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducers() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().toString())))
            .andExpect(jsonPath("$.[*].producerName").value(hasItem(DEFAULT_PRODUCER_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].producerLogoContentType").value(hasItem(DEFAULT_PRODUCER_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].producerLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PRODUCER_LOGO))));
    }

    @Test
    @Transactional
    void getProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get the producer
        restProducerMockMvc
            .perform(get(ENTITY_API_URL_ID, producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producer.getId().toString()))
            .andExpect(jsonPath("$.producerName").value(DEFAULT_PRODUCER_NAME))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.producerLogoContentType").value(DEFAULT_PRODUCER_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.producerLogo").value(Base64.getEncoder().encodeToString(DEFAULT_PRODUCER_LOGO)));
    }

    @Test
    @Transactional
    void getProducersByIdFiltering() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        UUID id = producer.getId();

        defaultProducerFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllProducersByProducerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where producerName equals to
        defaultProducerFiltering("producerName.equals=" + DEFAULT_PRODUCER_NAME, "producerName.equals=" + UPDATED_PRODUCER_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByProducerNameIsInShouldWork() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where producerName in
        defaultProducerFiltering(
            "producerName.in=" + DEFAULT_PRODUCER_NAME + "," + UPDATED_PRODUCER_NAME,
            "producerName.in=" + UPDATED_PRODUCER_NAME
        );
    }

    @Test
    @Transactional
    void getAllProducersByProducerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where producerName is not null
        defaultProducerFiltering("producerName.specified=true", "producerName.specified=false");
    }

    @Test
    @Transactional
    void getAllProducersByProducerNameContainsSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where producerName contains
        defaultProducerFiltering("producerName.contains=" + DEFAULT_PRODUCER_NAME, "producerName.contains=" + UPDATED_PRODUCER_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByProducerNameNotContainsSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where producerName does not contain
        defaultProducerFiltering(
            "producerName.doesNotContain=" + UPDATED_PRODUCER_NAME,
            "producerName.doesNotContain=" + DEFAULT_PRODUCER_NAME
        );
    }

    @Test
    @Transactional
    void getAllProducersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where createdAt equals to
        defaultProducerFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProducersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where createdAt in
        defaultProducerFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllProducersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where createdAt is not null
        defaultProducerFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    private void defaultProducerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProducerShouldBeFound(shouldBeFound);
        defaultProducerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProducerShouldBeFound(String filter) throws Exception {
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().toString())))
            .andExpect(jsonPath("$.[*].producerName").value(hasItem(DEFAULT_PRODUCER_NAME)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].producerLogoContentType").value(hasItem(DEFAULT_PRODUCER_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].producerLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PRODUCER_LOGO))));

        // Check, that the count call also returns 1
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProducerShouldNotBeFound(String filter) throws Exception {
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducer() throws Exception {
        // Get the producer
        restProducerMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producer
        Producer updatedProducer = producerRepository.findById(producer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducer are not directly saved in db
        em.detach(updatedProducer);
        updatedProducer
            .producerName(UPDATED_PRODUCER_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .producerLogo(UPDATED_PRODUCER_LOGO)
            .producerLogoContentType(UPDATED_PRODUCER_LOGO_CONTENT_TYPE);

        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProducer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProducerToMatchAllProperties(updatedProducer);
    }

    @Test
    @Transactional
    void putNonExistingProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        partialUpdatedProducer.producerName(UPDATED_PRODUCER_NAME);

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProducerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProducer, producer), getPersistedProducer(producer));
    }

    @Test
    @Transactional
    void fullUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        partialUpdatedProducer
            .producerName(UPDATED_PRODUCER_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .producerLogo(UPDATED_PRODUCER_LOGO)
            .producerLogoContentType(UPDATED_PRODUCER_LOGO_CONTENT_TYPE);

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProducerUpdatableFieldsEquals(partialUpdatedProducer, getPersistedProducer(partialUpdatedProducer));
    }

    @Test
    @Transactional
    void patchNonExistingProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, producer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producer.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(producer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producer
        restProducerMockMvc
            .perform(delete(ENTITY_API_URL_ID, producer.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return producerRepository.count();
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

    protected Producer getPersistedProducer(Producer producer) {
        return producerRepository.findById(producer.getId()).orElseThrow();
    }

    protected void assertPersistedProducerToMatchAllProperties(Producer expectedProducer) {
        assertProducerAllPropertiesEquals(expectedProducer, getPersistedProducer(expectedProducer));
    }

    protected void assertPersistedProducerToMatchUpdatableProperties(Producer expectedProducer) {
        assertProducerAllUpdatablePropertiesEquals(expectedProducer, getPersistedProducer(expectedProducer));
    }
}
