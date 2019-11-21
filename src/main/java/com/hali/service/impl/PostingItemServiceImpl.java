package com.hali.service.impl;

import com.hali.service.PostingItemService;
import com.hali.domain.PostingItem;
import com.hali.repository.PostingItemRepository;
import com.hali.service.dto.PostingItemDTO;
import com.hali.service.mapper.PostingItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PostingItem}.
 */
@Service
@Transactional
public class PostingItemServiceImpl implements PostingItemService {

    private final Logger log = LoggerFactory.getLogger(PostingItemServiceImpl.class);

    private final PostingItemRepository postingItemRepository;

    private final PostingItemMapper postingItemMapper;

    public PostingItemServiceImpl(PostingItemRepository postingItemRepository, PostingItemMapper postingItemMapper) {
        this.postingItemRepository = postingItemRepository;
        this.postingItemMapper = postingItemMapper;
    }

    /**
     * Save a postingItem.
     *
     * @param postingItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PostingItemDTO save(PostingItemDTO postingItemDTO) {
        log.debug("Request to save PostingItem : {}", postingItemDTO);
        PostingItem postingItem = postingItemMapper.toEntity(postingItemDTO);
        postingItem = postingItemRepository.save(postingItem);
        return postingItemMapper.toDto(postingItem);
    }

    /**
     * Get all the postingItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostingItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostingItems");
        return postingItemRepository.findAll(pageable)
            .map(postingItemMapper::toDto);
    }


    /**
     * Get one postingItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PostingItemDTO> findOne(Long id) {
        log.debug("Request to get PostingItem : {}", id);
        return postingItemRepository.findById(id)
            .map(postingItemMapper::toDto);
    }

    /**
     * Delete the postingItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostingItem : {}", id);
        postingItemRepository.deleteById(id);
    }
}
