package com.hali.service;

import com.hali.service.dto.PostFavoriteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.hali.domain.PostFavorite}.
 */
public interface PostFavoriteService {

    /**
     * Save a postFavorite.
     *
     * @param postFavoriteDTO the entity to save.
     * @return the persisted entity.
     */
    PostFavoriteDTO save(PostFavoriteDTO postFavoriteDTO);

    /**
     * Get all the postFavorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFavoriteDTO> findAll(Pageable pageable);


    /**
     * Get the "id" postFavorite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostFavoriteDTO> findOne(Long id);

    /**
     * Delete the "id" postFavorite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
