package com.hali.service;

import com.hali.service.dto.DistrictDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.hali.domain.District}.
 */
public interface DistrictService {

    /**
     * Save a district.
     *
     * @param districtDTO the entity to save.
     * @return the persisted entity.
     */
    DistrictDTO save(DistrictDTO districtDTO);

    /**
     * Get all the districts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DistrictDTO> findAll(Pageable pageable);


    /**
     * Get the "id" district.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DistrictDTO> findOne(Long id);

    /**
     * Delete the "id" district.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
