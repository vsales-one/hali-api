package com.hali.web.rest;

import com.hali.HaliApp;
import com.hali.domain.PostingItem;
import com.hali.domain.Category;
import com.hali.repository.PostingItemRepository;
import com.hali.service.PostingItemService;
import com.hali.service.dto.PostingItemDTO;
import com.hali.service.mapper.PostingItemMapper;
import com.hali.web.rest.errors.ExceptionTranslator;
import com.hali.service.dto.PostingItemCriteria;
import com.hali.service.PostingItemQueryService;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.hali.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PostingItemResource} REST controller.
 */
@SpringBootTest(classes = HaliApp.class)
public class PostingItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PICK_UP_TIME = "AAAAAAAAAA";
    private static final String UPDATED_PICK_UP_TIME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_START_DATE = Instant.ofEpochMilli(-1L);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_END_DATE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_PICKUP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_LAST_MODIFIED_DATE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private PostingItemRepository postingItemRepository;

    @Autowired
    private PostingItemMapper postingItemMapper;

    @Autowired
    private PostingItemService postingItemService;

    @Autowired
    private PostingItemQueryService postingItemQueryService;

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

    private MockMvc restPostingItemMockMvc;

    private PostingItem postingItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostingItemResource postingItemResource = new PostingItemResource(postingItemService, postingItemQueryService);
        this.restPostingItemMockMvc = MockMvcBuilders.standaloneSetup(postingItemResource)
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
    public static PostingItem createEntity(EntityManager em) {
        PostingItem postingItem = new PostingItem()
            .title(DEFAULT_TITLE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .description(DEFAULT_DESCRIPTION)
            .pickUpTime(DEFAULT_PICK_UP_TIME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .pickupAddress(DEFAULT_PICKUP_ADDRESS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .city(DEFAULT_CITY)
            .district(DEFAULT_DISTRICT)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .status(DEFAULT_STATUS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        postingItem.setCategory(category);
        return postingItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostingItem createUpdatedEntity(EntityManager em) {
        PostingItem postingItem = new PostingItem()
            .title(UPDATED_TITLE)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .pickUpTime(UPDATED_PICK_UP_TIME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .pickupAddress(UPDATED_PICKUP_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .city(UPDATED_CITY)
            .district(UPDATED_DISTRICT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .status(UPDATED_STATUS);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        postingItem.setCategory(category);
        return postingItem;
    }

    @BeforeEach
    public void initTest() {
        postingItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostingItem() throws Exception {
        int databaseSizeBeforeCreate = postingItemRepository.findAll().size();

        // Create the PostingItem
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);
        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PostingItem in the database
        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeCreate + 1);
        PostingItem testPostingItem = postingItemList.get(postingItemList.size() - 1);
        assertThat(testPostingItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPostingItem.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testPostingItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPostingItem.getPickUpTime()).isEqualTo(DEFAULT_PICK_UP_TIME);
        assertThat(testPostingItem.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPostingItem.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPostingItem.getPickupAddress()).isEqualTo(DEFAULT_PICKUP_ADDRESS);
        assertThat(testPostingItem.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testPostingItem.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testPostingItem.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPostingItem.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testPostingItem.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostingItem.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostingItem.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createPostingItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postingItemRepository.findAll().size();

        // Create the PostingItem with an existing ID
        postingItem.setId(1L);
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostingItem in the database
        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = postingItemRepository.findAll().size();
        // set the field null
        postingItem.setTitle(null);

        // Create the PostingItem, which fails.
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPickUpTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = postingItemRepository.findAll().size();
        // set the field null
        postingItem.setPickUpTime(null);

        // Create the PostingItem, which fails.
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postingItemRepository.findAll().size();
        // set the field null
        postingItem.setStartDate(null);

        // Create the PostingItem, which fails.
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postingItemRepository.findAll().size();
        // set the field null
        postingItem.setEndDate(null);

        // Create the PostingItem, which fails.
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPickupAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = postingItemRepository.findAll().size();
        // set the field null
        postingItem.setPickupAddress(null);

        // Create the PostingItem, which fails.
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        restPostingItemMockMvc.perform(post("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPostingItems() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList
        restPostingItemMockMvc.perform(get("/api/posting-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pickUpTime").value(hasItem(DEFAULT_PICK_UP_TIME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getPostingItem() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get the postingItem
        restPostingItemMockMvc.perform(get("/api/posting-items/{id}", postingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postingItem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.pickUpTime").value(DEFAULT_PICK_UP_TIME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.pickupAddress").value(DEFAULT_PICKUP_ADDRESS.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllPostingItemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where title equals to DEFAULT_TITLE
        defaultPostingItemShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the postingItemList where title equals to UPDATED_TITLE
        defaultPostingItemShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPostingItemShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the postingItemList where title equals to UPDATED_TITLE
        defaultPostingItemShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where title is not null
        defaultPostingItemShouldBeFound("title.specified=true");

        // Get all the postingItemList where title is null
        defaultPostingItemShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultPostingItemShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the postingItemList where imageUrl equals to UPDATED_IMAGE_URL
        defaultPostingItemShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultPostingItemShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the postingItemList where imageUrl equals to UPDATED_IMAGE_URL
        defaultPostingItemShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where imageUrl is not null
        defaultPostingItemShouldBeFound("imageUrl.specified=true");

        // Get all the postingItemList where imageUrl is null
        defaultPostingItemShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where description equals to DEFAULT_DESCRIPTION
        defaultPostingItemShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the postingItemList where description equals to UPDATED_DESCRIPTION
        defaultPostingItemShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPostingItemShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the postingItemList where description equals to UPDATED_DESCRIPTION
        defaultPostingItemShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where description is not null
        defaultPostingItemShouldBeFound("description.specified=true");

        // Get all the postingItemList where description is null
        defaultPostingItemShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickUpTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickUpTime equals to DEFAULT_PICK_UP_TIME
        defaultPostingItemShouldBeFound("pickUpTime.equals=" + DEFAULT_PICK_UP_TIME);

        // Get all the postingItemList where pickUpTime equals to UPDATED_PICK_UP_TIME
        defaultPostingItemShouldNotBeFound("pickUpTime.equals=" + UPDATED_PICK_UP_TIME);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickUpTimeIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickUpTime in DEFAULT_PICK_UP_TIME or UPDATED_PICK_UP_TIME
        defaultPostingItemShouldBeFound("pickUpTime.in=" + DEFAULT_PICK_UP_TIME + "," + UPDATED_PICK_UP_TIME);

        // Get all the postingItemList where pickUpTime equals to UPDATED_PICK_UP_TIME
        defaultPostingItemShouldNotBeFound("pickUpTime.in=" + UPDATED_PICK_UP_TIME);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickUpTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickUpTime is not null
        defaultPostingItemShouldBeFound("pickUpTime.specified=true");

        // Get all the postingItemList where pickUpTime is null
        defaultPostingItemShouldNotBeFound("pickUpTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where startDate equals to DEFAULT_START_DATE
        defaultPostingItemShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the postingItemList where startDate equals to UPDATED_START_DATE
        defaultPostingItemShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultPostingItemShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the postingItemList where startDate equals to UPDATED_START_DATE
        defaultPostingItemShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where startDate is not null
        defaultPostingItemShouldBeFound("startDate.specified=true");

        // Get all the postingItemList where startDate is null
        defaultPostingItemShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where endDate equals to DEFAULT_END_DATE
        defaultPostingItemShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the postingItemList where endDate equals to UPDATED_END_DATE
        defaultPostingItemShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultPostingItemShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the postingItemList where endDate equals to UPDATED_END_DATE
        defaultPostingItemShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where endDate is not null
        defaultPostingItemShouldBeFound("endDate.specified=true");

        // Get all the postingItemList where endDate is null
        defaultPostingItemShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickupAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickupAddress equals to DEFAULT_PICKUP_ADDRESS
        defaultPostingItemShouldBeFound("pickupAddress.equals=" + DEFAULT_PICKUP_ADDRESS);

        // Get all the postingItemList where pickupAddress equals to UPDATED_PICKUP_ADDRESS
        defaultPostingItemShouldNotBeFound("pickupAddress.equals=" + UPDATED_PICKUP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickupAddressIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickupAddress in DEFAULT_PICKUP_ADDRESS or UPDATED_PICKUP_ADDRESS
        defaultPostingItemShouldBeFound("pickupAddress.in=" + DEFAULT_PICKUP_ADDRESS + "," + UPDATED_PICKUP_ADDRESS);

        // Get all the postingItemList where pickupAddress equals to UPDATED_PICKUP_ADDRESS
        defaultPostingItemShouldNotBeFound("pickupAddress.in=" + UPDATED_PICKUP_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByPickupAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where pickupAddress is not null
        defaultPostingItemShouldBeFound("pickupAddress.specified=true");

        // Get all the postingItemList where pickupAddress is null
        defaultPostingItemShouldNotBeFound("pickupAddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude equals to DEFAULT_LATITUDE
        defaultPostingItemShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the postingItemList where latitude equals to UPDATED_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultPostingItemShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the postingItemList where latitude equals to UPDATED_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude is not null
        defaultPostingItemShouldBeFound("latitude.specified=true");

        // Get all the postingItemList where latitude is null
        defaultPostingItemShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultPostingItemShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the postingItemList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultPostingItemShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the postingItemList where latitude is less than or equal to SMALLER_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude is less than DEFAULT_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the postingItemList where latitude is less than UPDATED_LATITUDE
        defaultPostingItemShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where latitude is greater than DEFAULT_LATITUDE
        defaultPostingItemShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the postingItemList where latitude is greater than SMALLER_LATITUDE
        defaultPostingItemShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude equals to DEFAULT_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the postingItemList where longitude equals to UPDATED_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the postingItemList where longitude equals to UPDATED_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude is not null
        defaultPostingItemShouldBeFound("longitude.specified=true");

        // Get all the postingItemList where longitude is null
        defaultPostingItemShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the postingItemList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the postingItemList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude is less than DEFAULT_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the postingItemList where longitude is less than UPDATED_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where longitude is greater than DEFAULT_LONGITUDE
        defaultPostingItemShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the postingItemList where longitude is greater than SMALLER_LONGITUDE
        defaultPostingItemShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllPostingItemsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where city equals to DEFAULT_CITY
        defaultPostingItemShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the postingItemList where city equals to UPDATED_CITY
        defaultPostingItemShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where city in DEFAULT_CITY or UPDATED_CITY
        defaultPostingItemShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the postingItemList where city equals to UPDATED_CITY
        defaultPostingItemShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where city is not null
        defaultPostingItemShouldBeFound("city.specified=true");

        // Get all the postingItemList where city is null
        defaultPostingItemShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where district equals to DEFAULT_DISTRICT
        defaultPostingItemShouldBeFound("district.equals=" + DEFAULT_DISTRICT);

        // Get all the postingItemList where district equals to UPDATED_DISTRICT
        defaultPostingItemShouldNotBeFound("district.equals=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDistrictIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where district in DEFAULT_DISTRICT or UPDATED_DISTRICT
        defaultPostingItemShouldBeFound("district.in=" + DEFAULT_DISTRICT + "," + UPDATED_DISTRICT);

        // Get all the postingItemList where district equals to UPDATED_DISTRICT
        defaultPostingItemShouldNotBeFound("district.in=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByDistrictIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where district is not null
        defaultPostingItemShouldBeFound("district.specified=true");

        // Get all the postingItemList where district is null
        defaultPostingItemShouldNotBeFound("district.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPostingItemShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the postingItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPostingItemShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPostingItemShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the postingItemList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPostingItemShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedBy is not null
        defaultPostingItemShouldBeFound("lastModifiedBy.specified=true");

        // Get all the postingItemList where lastModifiedBy is null
        defaultPostingItemShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPostingItemShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the postingItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPostingItemShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultPostingItemShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the postingItemList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPostingItemShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where lastModifiedDate is not null
        defaultPostingItemShouldBeFound("lastModifiedDate.specified=true");

        // Get all the postingItemList where lastModifiedDate is null
        defaultPostingItemShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where status equals to DEFAULT_STATUS
        defaultPostingItemShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the postingItemList where status equals to UPDATED_STATUS
        defaultPostingItemShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPostingItemShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the postingItemList where status equals to UPDATED_STATUS
        defaultPostingItemShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllPostingItemsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        // Get all the postingItemList where status is not null
        defaultPostingItemShouldBeFound("status.specified=true");

        // Get all the postingItemList where status is null
        defaultPostingItemShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostingItemsByCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        Category category = postingItem.getCategory();
        postingItemRepository.saveAndFlush(postingItem);
        Long categoryId = category.getId();

        // Get all the postingItemList where category equals to categoryId
        defaultPostingItemShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the postingItemList where category equals to categoryId + 1
        defaultPostingItemShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostingItemShouldBeFound(String filter) throws Exception {
        restPostingItemMockMvc.perform(get("/api/posting-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pickUpTime").value(hasItem(DEFAULT_PICK_UP_TIME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restPostingItemMockMvc.perform(get("/api/posting-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostingItemShouldNotBeFound(String filter) throws Exception {
        restPostingItemMockMvc.perform(get("/api/posting-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostingItemMockMvc.perform(get("/api/posting-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPostingItem() throws Exception {
        // Get the postingItem
        restPostingItemMockMvc.perform(get("/api/posting-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostingItem() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        int databaseSizeBeforeUpdate = postingItemRepository.findAll().size();

        // Update the postingItem
        PostingItem updatedPostingItem = postingItemRepository.findById(postingItem.getId()).get();
        // Disconnect from session so that the updates on updatedPostingItem are not directly saved in db
        em.detach(updatedPostingItem);
        updatedPostingItem
            .title(UPDATED_TITLE)
            .imageUrl(UPDATED_IMAGE_URL)
            .description(UPDATED_DESCRIPTION)
            .pickUpTime(UPDATED_PICK_UP_TIME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .pickupAddress(UPDATED_PICKUP_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .city(UPDATED_CITY)
            .district(UPDATED_DISTRICT)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .status(UPDATED_STATUS);
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(updatedPostingItem);

        restPostingItemMockMvc.perform(put("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isOk());

        // Validate the PostingItem in the database
        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeUpdate);
        PostingItem testPostingItem = postingItemList.get(postingItemList.size() - 1);
        assertThat(testPostingItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPostingItem.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testPostingItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPostingItem.getPickUpTime()).isEqualTo(UPDATED_PICK_UP_TIME);
        assertThat(testPostingItem.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPostingItem.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostingItem.getPickupAddress()).isEqualTo(UPDATED_PICKUP_ADDRESS);
        assertThat(testPostingItem.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPostingItem.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testPostingItem.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPostingItem.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testPostingItem.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostingItem.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostingItem.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingPostingItem() throws Exception {
        int databaseSizeBeforeUpdate = postingItemRepository.findAll().size();

        // Create the PostingItem
        PostingItemDTO postingItemDTO = postingItemMapper.toDto(postingItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostingItemMockMvc.perform(put("/api/posting-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postingItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostingItem in the database
        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostingItem() throws Exception {
        // Initialize the database
        postingItemRepository.saveAndFlush(postingItem);

        int databaseSizeBeforeDelete = postingItemRepository.findAll().size();

        // Delete the postingItem
        restPostingItemMockMvc.perform(delete("/api/posting-items/{id}", postingItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostingItem> postingItemList = postingItemRepository.findAll();
        assertThat(postingItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostingItem.class);
        PostingItem postingItem1 = new PostingItem();
        postingItem1.setId(1L);
        PostingItem postingItem2 = new PostingItem();
        postingItem2.setId(postingItem1.getId());
        assertThat(postingItem1).isEqualTo(postingItem2);
        postingItem2.setId(2L);
        assertThat(postingItem1).isNotEqualTo(postingItem2);
        postingItem1.setId(null);
        assertThat(postingItem1).isNotEqualTo(postingItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostingItemDTO.class);
        PostingItemDTO postingItemDTO1 = new PostingItemDTO();
        postingItemDTO1.setId(1L);
        PostingItemDTO postingItemDTO2 = new PostingItemDTO();
        assertThat(postingItemDTO1).isNotEqualTo(postingItemDTO2);
        postingItemDTO2.setId(postingItemDTO1.getId());
        assertThat(postingItemDTO1).isEqualTo(postingItemDTO2);
        postingItemDTO2.setId(2L);
        assertThat(postingItemDTO1).isNotEqualTo(postingItemDTO2);
        postingItemDTO1.setId(null);
        assertThat(postingItemDTO1).isNotEqualTo(postingItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postingItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(postingItemMapper.fromId(null)).isNull();
    }
}
