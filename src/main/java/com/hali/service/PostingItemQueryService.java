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

import com.hali.domain.PostingItem;
import com.hali.domain.*; // for static metamodels
import com.hali.repository.PostingItemRepository;
import com.hali.service.dto.PostingItemCriteria;
import com.hali.service.dto.PostingItemDTO;
import com.hali.service.mapper.PostingItemMapper;

/**
 * Service for executing complex queries for {@link PostingItem} entities in the database.
 * The main input is a {@link PostingItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostingItemDTO} or a {@link Page} of {@link PostingItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostingItemQueryService extends QueryService<PostingItem> {

    private final Logger log = LoggerFactory.getLogger(PostingItemQueryService.class);

    private final PostingItemRepository postingItemRepository;

    private final PostingItemMapper postingItemMapper;

    public PostingItemQueryService(PostingItemRepository postingItemRepository, PostingItemMapper postingItemMapper) {
        this.postingItemRepository = postingItemRepository;
        this.postingItemMapper = postingItemMapper;
    }

    /**
     * Return a {@link List} of {@link PostingItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostingItemDTO> findByCriteria(PostingItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PostingItem> specification = createSpecification(criteria);
        return postingItemMapper.toDto(postingItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostingItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostingItemDTO> findByCriteria(PostingItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PostingItem> specification = createSpecification(criteria);
        return postingItemRepository.findAll(specification, page)
            .map(postingItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostingItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PostingItem> specification = createSpecification(criteria);
        return postingItemRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<PostingItem> createSpecification(PostingItemCriteria criteria) {
        Specification<PostingItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PostingItem_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), PostingItem_.title));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), PostingItem_.imageUrl));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PostingItem_.description));
            }
            if (criteria.getPickUpTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPickUpTime(), PostingItem_.pickUpTime));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), PostingItem_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), PostingItem_.endDate));
            }
            if (criteria.getPickupAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPickupAddress(), PostingItem_.pickupAddress));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), PostingItem_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), PostingItem_.longitude));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), PostingItem_.city));
            }
            if (criteria.getDistrict() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDistrict(), PostingItem_.district));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), PostingItem_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), PostingItem_.lastModifiedDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), PostingItem_.status));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(PostingItem_.category, JoinType.LEFT).get(Category_.id)));
            }
        }
        return specification;
    }
}
