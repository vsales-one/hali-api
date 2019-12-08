package com.hali.web.rest;

import com.hali.HaliApp;
import com.hali.domain.UserProfile;
import com.hali.repository.UserProfileRepository;
import com.hali.service.UserProfileService;
import com.hali.service.dto.UserProfileDTO;
import com.hali.service.mapper.UserProfileMapper;
import com.hali.web.rest.errors.ExceptionTranslator;
import com.hali.service.dto.UserProfileCriteria;
import com.hali.service.UserProfileQueryService;

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
import java.util.List;

import static com.hali.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@SpringBootTest(classes = HaliApp.class)
public class UserProfileResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String DEFAULT_DISPLAY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBBBBBBB";

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileQueryService userProfileQueryService;

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

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserProfileResource userProfileResource = new UserProfileResource(userProfileService, userProfileQueryService);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
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
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userId(DEFAULT_USER_ID)
            .imageUrl(DEFAULT_IMAGE_URL)
            .city(DEFAULT_CITY)
            .address(DEFAULT_ADDRESS)
            .district(DEFAULT_DISTRICT)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .displayName(DEFAULT_DISPLAY_NAME);
        return userProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userId(UPDATED_USER_ID)
            .imageUrl(UPDATED_IMAGE_URL)
            .city(UPDATED_CITY)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .displayName(UPDATED_DISPLAY_NAME);
        return userProfile;
    }

    @BeforeEach
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserProfile.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testUserProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserProfile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserProfile.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testUserProfile.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testUserProfile.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testUserProfile.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testUserProfile.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void createUserProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setUserId(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc.perform(post("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.intValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.intValue()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId equals to DEFAULT_USER_ID
        defaultUserProfileShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userProfileList where userId equals to UPDATED_USER_ID
        defaultUserProfileShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserProfileShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userProfileList where userId equals to UPDATED_USER_ID
        defaultUserProfileShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId is not null
        defaultUserProfileShouldBeFound("userId.specified=true");

        // Get all the userProfileList where userId is null
        defaultUserProfileShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultUserProfileShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the userProfileList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUserProfileShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultUserProfileShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the userProfileList where imageUrl equals to UPDATED_IMAGE_URL
        defaultUserProfileShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where imageUrl is not null
        defaultUserProfileShouldBeFound("imageUrl.specified=true");

        // Get all the userProfileList where imageUrl is null
        defaultUserProfileShouldNotBeFound("imageUrl.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city equals to DEFAULT_CITY
        defaultUserProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city is not null
        defaultUserProfileShouldBeFound("city.specified=true");

        // Get all the userProfileList where city is null
        defaultUserProfileShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address equals to DEFAULT_ADDRESS
        defaultUserProfileShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the userProfileList where address equals to UPDATED_ADDRESS
        defaultUserProfileShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultUserProfileShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the userProfileList where address equals to UPDATED_ADDRESS
        defaultUserProfileShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where address is not null
        defaultUserProfileShouldBeFound("address.specified=true");

        // Get all the userProfileList where address is null
        defaultUserProfileShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where district equals to DEFAULT_DISTRICT
        defaultUserProfileShouldBeFound("district.equals=" + DEFAULT_DISTRICT);

        // Get all the userProfileList where district equals to UPDATED_DISTRICT
        defaultUserProfileShouldNotBeFound("district.equals=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByDistrictIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where district in DEFAULT_DISTRICT or UPDATED_DISTRICT
        defaultUserProfileShouldBeFound("district.in=" + DEFAULT_DISTRICT + "," + UPDATED_DISTRICT);

        // Get all the userProfileList where district equals to UPDATED_DISTRICT
        defaultUserProfileShouldNotBeFound("district.in=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByDistrictIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where district is not null
        defaultUserProfileShouldBeFound("district.specified=true");

        // Get all the userProfileList where district is null
        defaultUserProfileShouldNotBeFound("district.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultUserProfileShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the userProfileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultUserProfileShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultUserProfileShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the userProfileList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultUserProfileShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phoneNumber is not null
        defaultUserProfileShouldBeFound("phoneNumber.specified=true");

        // Get all the userProfileList where phoneNumber is null
        defaultUserProfileShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude equals to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude equals to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultUserProfileShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the userProfileList where latitude equals to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is not null
        defaultUserProfileShouldBeFound("latitude.specified=true");

        // Get all the userProfileList where latitude is null
        defaultUserProfileShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultUserProfileShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is less than or equal to SMALLER_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is less than DEFAULT_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is less than UPDATED_LATITUDE
        defaultUserProfileShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where latitude is greater than DEFAULT_LATITUDE
        defaultUserProfileShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the userProfileList where latitude is greater than SMALLER_LATITUDE
        defaultUserProfileShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude equals to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude equals to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the userProfileList where longitude equals to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is not null
        defaultUserProfileShouldBeFound("longitude.specified=true");

        // Get all the userProfileList where longitude is null
        defaultUserProfileShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is less than DEFAULT_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is less than UPDATED_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where longitude is greater than DEFAULT_LONGITUDE
        defaultUserProfileShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the userProfileList where longitude is greater than SMALLER_LONGITUDE
        defaultUserProfileShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }


    @Test
    @Transactional
    public void getAllUserProfilesByDisplayNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where displayName equals to DEFAULT_DISPLAY_NAME
        defaultUserProfileShouldBeFound("displayName.equals=" + DEFAULT_DISPLAY_NAME);

        // Get all the userProfileList where displayName equals to UPDATED_DISPLAY_NAME
        defaultUserProfileShouldNotBeFound("displayName.equals=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByDisplayNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where displayName in DEFAULT_DISPLAY_NAME or UPDATED_DISPLAY_NAME
        defaultUserProfileShouldBeFound("displayName.in=" + DEFAULT_DISPLAY_NAME + "," + UPDATED_DISPLAY_NAME);

        // Get all the userProfileList where displayName equals to UPDATED_DISPLAY_NAME
        defaultUserProfileShouldNotBeFound("displayName.in=" + UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void getAllUserProfilesByDisplayNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where displayName is not null
        defaultUserProfileShouldBeFound("displayName.specified=true");

        // Get all the userProfileList where displayName is null
        defaultUserProfileShouldNotBeFound("displayName.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.intValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.intValue())))
            .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME)));

        // Check, that the count call also returns 1
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc.perform(get("/api/user-profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc.perform(get("/api/user-profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/user-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).get();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .userId(UPDATED_USER_ID)
            .imageUrl(UPDATED_IMAGE_URL)
            .city(UPDATED_CITY)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .displayName(UPDATED_DISPLAY_NAME);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserProfile.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testUserProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserProfile.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testUserProfile.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testUserProfile.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testUserProfile.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testUserProfile.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc.perform(put("/api/user-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Delete the userProfile
        restUserProfileMockMvc.perform(delete("/api/user-profiles/{id}", userProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = new UserProfile();
        userProfile1.setId(1L);
        UserProfile userProfile2 = new UserProfile();
        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);
        userProfile2.setId(2L);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
        userProfile1.setId(null);
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfileDTO.class);
        UserProfileDTO userProfileDTO1 = new UserProfileDTO();
        userProfileDTO1.setId(1L);
        UserProfileDTO userProfileDTO2 = new UserProfileDTO();
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO2.setId(userProfileDTO1.getId());
        assertThat(userProfileDTO1).isEqualTo(userProfileDTO2);
        userProfileDTO2.setId(2L);
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
        userProfileDTO1.setId(null);
        assertThat(userProfileDTO1).isNotEqualTo(userProfileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userProfileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userProfileMapper.fromId(null)).isNull();
    }
}
