package com.hali.web.rest;

import com.hali.service.PostingItemService;
import com.hali.web.rest.errors.BadRequestAlertException;
import com.hali.service.dto.PostingItemDTO;
import com.hali.service.dto.PostingItemCriteria;
import com.hali.service.PostingItemQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hali.domain.PostingItem}.
 */
@RestController
@RequestMapping("/api")
public class PostingItemResource {

    private final Logger log = LoggerFactory.getLogger(PostingItemResource.class);

    private static final String ENTITY_NAME = "postingItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostingItemService postingItemService;

    private final PostingItemQueryService postingItemQueryService;

    public PostingItemResource(PostingItemService postingItemService, PostingItemQueryService postingItemQueryService) {
        this.postingItemService = postingItemService;
        this.postingItemQueryService = postingItemQueryService;
    }

    /**
     * {@code POST  /posting-items} : Create a new postingItem.
     *
     * @param postingItemDTO the postingItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postingItemDTO, or with status {@code 400 (Bad Request)} if the postingItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/posting-items")
    public ResponseEntity<PostingItemDTO> createPostingItem(@Valid @RequestBody PostingItemDTO postingItemDTO) throws URISyntaxException {
        log.debug("REST request to save PostingItem : {}", postingItemDTO);
        if (postingItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new postingItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostingItemDTO result = postingItemService.save(postingItemDTO);
        return ResponseEntity.created(new URI("/api/posting-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posting-items} : Updates an existing postingItem.
     *
     * @param postingItemDTO the postingItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postingItemDTO,
     * or with status {@code 400 (Bad Request)} if the postingItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postingItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/posting-items")
    public ResponseEntity<PostingItemDTO> updatePostingItem(@Valid @RequestBody PostingItemDTO postingItemDTO) throws URISyntaxException {
        log.debug("REST request to update PostingItem : {}", postingItemDTO);
        if (postingItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostingItemDTO result = postingItemService.save(postingItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postingItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /posting-items} : get all the postingItems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postingItems in body.
     */
    @GetMapping("/posting-items")
    public ResponseEntity<List<PostingItemDTO>> getAllPostingItems(PostingItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PostingItems by criteria: {}", criteria);
        Page<PostingItemDTO> page = postingItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /posting-items/count} : count all the postingItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/posting-items/count")
    public ResponseEntity<Long> countPostingItems(PostingItemCriteria criteria) {
        log.debug("REST request to count PostingItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(postingItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /posting-items/:id} : get the "id" postingItem.
     *
     * @param id the id of the postingItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postingItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/posting-items/{id}")
    public ResponseEntity<PostingItemDTO> getPostingItem(@PathVariable Long id) {
        log.debug("REST request to get PostingItem : {}", id);
        Optional<PostingItemDTO> postingItemDTO = postingItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postingItemDTO);
    }

    /**
     * {@code DELETE  /posting-items/:id} : delete the "id" postingItem.
     *
     * @param id the id of the postingItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/posting-items/{id}")
    public ResponseEntity<Void> deletePostingItem(@PathVariable Long id) {
        log.debug("REST request to delete PostingItem : {}", id);
        postingItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
