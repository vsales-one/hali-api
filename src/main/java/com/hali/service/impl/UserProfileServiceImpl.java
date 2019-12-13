package com.hali.service.impl;

import com.hali.service.UserProfileService;
import com.hali.domain.UserProfile;
import com.hali.repository.UserProfileRepository;
import com.hali.service.dto.UserProfileDTO;
import com.hali.service.mapper.UserProfileMapper;
import com.hali.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll(pageable)
            .map(userProfileMapper::toDto);
    }


    /**
     * Get one userProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id)
            .map(userProfileMapper::toDto);
    }

    /**
     * Delete the userProfile by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfileDTO updateByUserId(String userId, UserProfileDTO userProfileDTO) {
       Optional<UserProfile> optionalUserProfile = userProfileRepository.findUserProfileByUserId(userId);
       if(!optionalUserProfile.isPresent())
           throw new BadRequestAlertException("A new userProfile cannot already have an ID", "UserProfile", "idexists");

       UserProfile userProfile = optionalUserProfile.get();
        userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
       userProfile.setAddress(userProfileDTO.getAddress());
       userProfile.setCity(userProfileDTO.getCity());
       userProfile.setDistrict(userProfileDTO.getDistrict());
       userProfile.setImageUrl(userProfileDTO.getImageUrl());
       userProfile.setPhoneNumber(userProfileDTO.getPhoneNumber());
       userProfile.setLatitude(userProfileDTO.getLatitude());
       userProfile.setLongitude(userProfileDTO.getLongitude());
       userProfile.setDisplayName(userProfileDTO.getDisplayName());

       userProfile = userProfileRepository.save(userProfile);

       return userProfileMapper.toDto(userProfile);

    }
}
