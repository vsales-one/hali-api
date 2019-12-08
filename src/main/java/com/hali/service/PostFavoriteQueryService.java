package com.hali.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.hali.domain.PostFavorite;
import com.hali.domain.*; // for static metamodels
import com.hali.repository.PostFavoriteRepository;
import com.hali.service.dto.PostFavoriteCriteria;
import com.hali.service.dto.PostFavoriteDTO;
import com.hali.service.mapper.PostFavoriteMapper;

/**
 * Service for executing complex queries for {@link PostFavorite} entities in the database.
 * The main input is a {@link PostFavoriteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostFavoriteDTO} or a {@link Page} of {@link PostFavoriteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostFavoriteQueryService extends QueryService<PostFavorite> {

    private final Logger log = LoggerFactory.getLogger(PostFavoriteQueryService.class);

    private final PostFavoriteRepository postFavoriteRepository;

    private final PostFavoriteMapper postFavoriteMapper;

    public PostFavoriteQueryService(PostFavoriteRepository postFavoriteRepository, PostFavoriteMapper postFavoriteMapper) {
        this.postFavoriteRepository = postFavoriteRepository;
        this.postFavoriteMapper = postFavoriteMapper;
    }

    /**
     * Return a {@link List} of {@link PostFavoriteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostFavoriteDTO> findByCriteria(PostFavoriteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PostFavorite> specification = createSpecification(criteria);
        return postFavoriteMapper.toDto(postFavoriteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostFavoriteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostFavoriteDTO> findByCriteria(PostFavoriteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PostFavorite> specification = createSpecification(criteria);
        return postFavoriteRepository.findAll(specification, page)
            .map(postFavoriteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostFavoriteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PostFavorite> specification = createSpecification(criteria);
        return postFavoriteRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<PostFavorite> createSpecification(PostFavoriteCriteria criteria) {
        Specification<PostFavorite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PostFavorite_.id));
            }
            if (criteria.getDateFavorited() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFavorited(), PostFavorite_.dateFavorited));
            }
            if (criteria.getPostingItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostingItemId(),
                    root -> root.join(PostFavorite_.postingItem, JoinType.LEFT).get(PostingItem_.id)));
            }
            if (criteria.getUserProfileId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserProfileId(),
                    root -> root.join(PostFavorite_.userProfile, JoinType.LEFT).get(UserProfile_.id)));
            }
        }
        return specification;
    }
}
