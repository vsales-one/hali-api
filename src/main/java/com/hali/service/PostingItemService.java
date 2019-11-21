package com.hali.service;

import com.hali.service.dto.PostingItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.hali.domain.PostingItem}.
 */
public interface PostingItemService {

    /**
     * Save a postingItem.
     *
     * @param postingItemDTO the entity to save.
     * @return the persisted entity.
     */
    PostingItemDTO save(PostingItemDTO postingItemDTO);

    /**
     * Get all the postingItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostingItemDTO> findAll(Pageable pageable);


    /**
     * Get the "id" postingItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostingItemDTO> findOne(Long id);

    /**
     * Delete the "id" postingItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
