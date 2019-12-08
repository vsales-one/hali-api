package com.hali.service.impl;

import com.hali.service.PostFavoriteService;
import com.hali.domain.PostFavorite;
import com.hali.repository.PostFavoriteRepository;
import com.hali.service.dto.PostFavoriteDTO;
import com.hali.service.mapper.PostFavoriteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PostFavorite}.
 */
@Service
@Transactional
public class PostFavoriteServiceImpl implements PostFavoriteService {

    private final Logger log = LoggerFactory.getLogger(PostFavoriteServiceImpl.class);

    private final PostFavoriteRepository postFavoriteRepository;

    private final PostFavoriteMapper postFavoriteMapper;

    public PostFavoriteServiceImpl(PostFavoriteRepository postFavoriteRepository, PostFavoriteMapper postFavoriteMapper) {
        this.postFavoriteRepository = postFavoriteRepository;
        this.postFavoriteMapper = postFavoriteMapper;
    }

    /**
     * Save a postFavorite.
     *
     * @param postFavoriteDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PostFavoriteDTO save(PostFavoriteDTO postFavoriteDTO) {
        log.debug("Request to save PostFavorite : {}", postFavoriteDTO);
        PostFavorite postFavorite = postFavoriteMapper.toEntity(postFavoriteDTO);
        postFavorite = postFavoriteRepository.save(postFavorite);
        return postFavoriteMapper.toDto(postFavorite);
    }

    /**
     * Get all the postFavorites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostFavoriteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFavorites");
        return postFavoriteRepository.findAll(pageable)
            .map(postFavoriteMapper::toDto);
    }


    /**
     * Get one postFavorite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostFavoriteDTO> findOne(Long id) {
        log.debug("Request to get PostFavorite : {}", id);
        return postFavoriteRepository.findById(id)
            .map(postFavoriteMapper::toDto);
    }

    /**
     * Delete the postFavorite by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostFavorite : {}", id);
        postFavoriteRepository.deleteById(id);
    }
}
