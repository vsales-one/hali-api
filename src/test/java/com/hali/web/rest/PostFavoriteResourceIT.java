package com.hali.web.rest;

import com.hali.HaliApp;
import com.hali.domain.PostFavorite;
import com.hali.domain.PostingItem;
import com.hali.domain.UserProfile;
import com.hali.repository.PostFavoriteRepository;
import com.hali.service.PostFavoriteService;
import com.hali.service.dto.PostFavoriteDTO;
import com.hali.service.mapper.PostFavoriteMapper;
import com.hali.web.rest.errors.ExceptionTranslator;
import com.hali.service.dto.PostFavoriteCriteria;
import com.hali.service.PostFavoriteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.hali.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PostFavoriteResource} REST controller.
 */
@SpringBootTest(classes = HaliApp.class)
public class PostFavoriteResourceIT {

    private static final Instant DEFAULT_DATE_FAVORITED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FAVORITED = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE_FAVORITED = Instant.ofEpochMilli(-1L);

    @Autowired
    private PostFavoriteRepository postFavoriteRepository;

    @Autowired
    private PostFavoriteMapper postFavoriteMapper;

    @Autowired
    private PostFavoriteService postFavoriteService;

    @Autowired
    private PostFavoriteQueryService postFavoriteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPostFavoriteMockMvc;

    private PostFavorite postFavorite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostFavoriteResource postFavoriteResource = new PostFavoriteResource(postFavoriteService, postFavoriteQueryService);
        this.restPostFavoriteMockMvc = MockMvcBuilders.standaloneSetup(postFavoriteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFavorite createEntity(EntityManager em) {
        PostFavorite postFavorite = new PostFavorite()
            .dateFavorited(DEFAULT_DATE_FAVORITED);
        return postFavorite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFavorite createUpdatedEntity(EntityManager em) {
        PostFavorite postFavorite = new PostFavorite()
            .dateFavorited(UPDATED_DATE_FAVORITED);
        return postFavorite;
    }

    @BeforeEach
    public void initTest() {
        postFavorite = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostFavorite() throws Exception {
        int databaseSizeBeforeCreate = postFavoriteRepository.findAll().size();

        // Create the PostFavorite
        PostFavoriteDTO postFavoriteDTO = postFavoriteMapper.toDto(postFavorite);
        restPostFavoriteMockMvc.perform(post("/api/post-favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postFavoriteDTO)))
            .andExpect(status().isCreated());

        // Validate the PostFavorite in the database
        List<PostFavorite> postFavoriteList = postFavoriteRepository.findAll();
        assertThat(postFavoriteList).hasSize(databaseSizeBeforeCreate + 1);
        PostFavorite testPostFavorite = postFavoriteList.get(postFavoriteList.size() - 1);
        assertThat(testPostFavorite.getDateFavorited()).isEqualTo(DEFAULT_DATE_FAVORITED);
    }

    @Test
    @Transactional
    public void createPostFavoriteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postFavoriteRepository.findAll().size();

        // Create the PostFavorite with an existing ID
        postFavorite.setId(1L);
        PostFavoriteDTO postFavoriteDTO = postFavoriteMapper.toDto(postFavorite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostFavoriteMockMvc.perform(post("/api/post-favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postFavoriteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostFavorite in the database
        List<PostFavorite> postFavoriteList = postFavoriteRepository.findAll();
        assertThat(postFavoriteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPostFavorites() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        // Get all the postFavoriteList
        restPostFavoriteMockMvc.perform(get("/api/post-favorites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFavorite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFavorited").value(hasItem(DEFAULT_DATE_FAVORITED.toString())));
    }
    
    @Test
    @Transactional
    public void getPostFavorite() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        // Get the postFavorite
        restPostFavoriteMockMvc.perform(get("/api/post-favorites/{id}", postFavorite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postFavorite.getId().intValue()))
            .andExpect(jsonPath("$.dateFavorited").value(DEFAULT_DATE_FAVORITED.toString()));
    }

    @Test
    @Transactional
    public void getAllPostFavoritesByDateFavoritedIsEqualToSomething() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        // Get all the postFavoriteList where dateFavorited equals to DEFAULT_DATE_FAVORITED
        defaultPostFavoriteShouldBeFound("dateFavorited.equals=" + DEFAULT_DATE_FAVORITED);

        // Get all the postFavoriteList where dateFavorited equals to UPDATED_DATE_FAVORITED
        defaultPostFavoriteShouldNotBeFound("dateFavorited.equals=" + UPDATED_DATE_FAVORITED);
    }

    @Test
    @Transactional
    public void getAllPostFavoritesByDateFavoritedIsInShouldWork() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        // Get all the postFavoriteList where dateFavorited in DEFAULT_DATE_FAVORITED or UPDATED_DATE_FAVORITED
        defaultPostFavoriteShouldBeFound("dateFavorited.in=" + DEFAULT_DATE_FAVORITED + "," + UPDATED_DATE_FAVORITED);

        // Get all the postFavoriteList where dateFavorited equals to UPDATED_DATE_FAVORITED
        defaultPostFavoriteShouldNotBeFound("dateFavorited.in=" + UPDATED_DATE_FAVORITED);
    }

    @Test
    @Transactional
    public void getAllPostFavoritesByDateFavoritedIsNullOrNotNull() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        // Get all the postFavoriteList where dateFavorited is not null
        defaultPostFavoriteShouldBeFound("dateFavorited.specified=true");

        // Get all the postFavoriteList where dateFavorited is null
        defaultPostFavoriteShouldNotBeFound("dateFavorited.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostFavoritesByPostingItemIsEqualToSomething() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);
        PostingItem postingItem = PostingItemResourceIT.createEntity(em);
        em.persist(postingItem);
        em.flush();
        postFavorite.setPostingItem(postingItem);
        postFavoriteRepository.saveAndFlush(postFavorite);
        Long postingItemId = postingItem.getId();

        // Get all the postFavoriteList where postingItem equals to postingItemId
        defaultPostFavoriteShouldBeFound("postingItemId.equals=" + postingItemId);

        // Get all the postFavoriteList where postingItem equals to postingItemId + 1
        defaultPostFavoriteShouldNotBeFound("postingItemId.equals=" + (postingItemId + 1));
    }


    @Test
    @Transactional
    public void getAllPostFavoritesByUserProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);
        UserProfile userProfile = UserProfileResourceIT.createEntity(em);
        em.persist(userProfile);
        em.flush();
        postFavorite.setUserProfile(userProfile);
        postFavoriteRepository.saveAndFlush(postFavorite);
        Long userProfileId = userProfile.getId();

        // Get all the postFavoriteList where userProfile equals to userProfileId
        defaultPostFavoriteShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the postFavoriteList where userProfile equals to userProfileId + 1
        defaultPostFavoriteShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostFavoriteShouldBeFound(String filter) throws Exception {
        restPostFavoriteMockMvc.perform(get("/api/post-favorites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFavorite.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFavorited").value(hasItem(DEFAULT_DATE_FAVORITED.toString())));

        // Check, that the count call also returns 1
        restPostFavoriteMockMvc.perform(get("/api/post-favorites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostFavoriteShouldNotBeFound(String filter) throws Exception {
        restPostFavoriteMockMvc.perform(get("/api/post-favorites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostFavoriteMockMvc.perform(get("/api/post-favorites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPostFavorite() throws Exception {
        // Get the postFavorite
        restPostFavoriteMockMvc.perform(get("/api/post-favorites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostFavorite() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        int databaseSizeBeforeUpdate = postFavoriteRepository.findAll().size();

        // Update the postFavorite
        PostFavorite updatedPostFavorite = postFavoriteRepository.findById(postFavorite.getId()).get();
        // Disconnect from session so that the updates on updatedPostFavorite are not directly saved in db
        em.detach(updatedPostFavorite);
        updatedPostFavorite
            .dateFavorited(UPDATED_DATE_FAVORITED);
        PostFavoriteDTO postFavoriteDTO = postFavoriteMapper.toDto(updatedPostFavorite);

        restPostFavoriteMockMvc.perform(put("/api/post-favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postFavoriteDTO)))
            .andExpect(status().isOk());

        // Validate the PostFavorite in the database
        List<PostFavorite> postFavoriteList = postFavoriteRepository.findAll();
        assertThat(postFavoriteList).hasSize(databaseSizeBeforeUpdate);
        PostFavorite testPostFavorite = postFavoriteList.get(postFavoriteList.size() - 1);
        assertThat(testPostFavorite.getDateFavorited()).isEqualTo(UPDATED_DATE_FAVORITED);
    }

    @Test
    @Transactional
    public void updateNonExistingPostFavorite() throws Exception {
        int databaseSizeBeforeUpdate = postFavoriteRepository.findAll().size();

        // Create the PostFavorite
        PostFavoriteDTO postFavoriteDTO = postFavoriteMapper.toDto(postFavorite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFavoriteMockMvc.perform(put("/api/post-favorites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postFavoriteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostFavorite in the database
        List<PostFavorite> postFavoriteList = postFavoriteRepository.findAll();
        assertThat(postFavoriteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostFavorite() throws Exception {
        // Initialize the database
        postFavoriteRepository.saveAndFlush(postFavorite);

        int databaseSizeBeforeDelete = postFavoriteRepository.findAll().size();

        // Delete the postFavorite
        restPostFavoriteMockMvc.perform(delete("/api/post-favorites/{id}", postFavorite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostFavorite> postFavoriteList = postFavoriteRepository.findAll();
        assertThat(postFavoriteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFavorite.class);
        PostFavorite postFavorite1 = new PostFavorite();
        postFavorite1.setId(1L);
        PostFavorite postFavorite2 = new PostFavorite();
        postFavorite2.setId(postFavorite1.getId());
        assertThat(postFavorite1).isEqualTo(postFavorite2);
        postFavorite2.setId(2L);
        assertThat(postFavorite1).isNotEqualTo(postFavorite2);
        postFavorite1.setId(null);
        assertThat(postFavorite1).isNotEqualTo(postFavorite2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostFavoriteDTO.class);
        PostFavoriteDTO postFavoriteDTO1 = new PostFavoriteDTO();
        postFavoriteDTO1.setId(1L);
        PostFavoriteDTO postFavoriteDTO2 = new PostFavoriteDTO();
        assertThat(postFavoriteDTO1).isNotEqualTo(postFavoriteDTO2);
        postFavoriteDTO2.setId(postFavoriteDTO1.getId());
        assertThat(postFavoriteDTO1).isEqualTo(postFavoriteDTO2);
        postFavoriteDTO2.setId(2L);
        assertThat(postFavoriteDTO1).isNotEqualTo(postFavoriteDTO2);
        postFavoriteDTO1.setId(null);
        assertThat(postFavoriteDTO1).isNotEqualTo(postFavoriteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postFavoriteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(postFavoriteMapper.fromId(null)).isNull();
    }
}
