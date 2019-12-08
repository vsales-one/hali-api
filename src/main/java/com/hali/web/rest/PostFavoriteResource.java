package com.hali.web.rest;

import com.hali.service.PostFavoriteService;
import com.hali.web.rest.errors.BadRequestAlertException;
import com.hali.service.dto.PostFavoriteDTO;
import com.hali.service.dto.PostFavoriteCriteria;
import com.hali.service.PostFavoriteQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hali.domain.PostFavorite}.
 */
@RestController
@RequestMapping("/api")
public class PostFavoriteResource {

    private final Logger log = LoggerFactory.getLogger(PostFavoriteResource.class);

    private static final String ENTITY_NAME = "postFavorite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostFavoriteService postFavoriteService;

    private final PostFavoriteQueryService postFavoriteQueryService;

    public PostFavoriteResource(PostFavoriteService postFavoriteService, PostFavoriteQueryService postFavoriteQueryService) {
        this.postFavoriteService = postFavoriteService;
        this.postFavoriteQueryService = postFavoriteQueryService;
    }

    /**
     * {@code POST  /post-favorites} : Create a new postFavorite.
     *
     * @param postFavoriteDTO the postFavoriteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postFavoriteDTO, or with status {@code 400 (Bad Request)} if the postFavorite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/post-favorites")
    public ResponseEntity<PostFavoriteDTO> createPostFavorite(@RequestBody PostFavoriteDTO postFavoriteDTO) throws URISyntaxException {
        log.debug("REST request to save PostFavorite : {}", postFavoriteDTO);
        if (postFavoriteDTO.getId() != null) {
            throw new BadRequestAlertException("A new postFavorite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostFavoriteDTO result = postFavoriteService.save(postFavoriteDTO);
        return ResponseEntity.created(new URI("/api/post-favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-favorites} : Updates an existing postFavorite.
     *
     * @param postFavoriteDTO the postFavoriteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFavoriteDTO,
     * or with status {@code 400 (Bad Request)} if the postFavoriteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postFavoriteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/post-favorites")
    public ResponseEntity<PostFavoriteDTO> updatePostFavorite(@RequestBody PostFavoriteDTO postFavoriteDTO) throws URISyntaxException {
        log.debug("REST request to update PostFavorite : {}", postFavoriteDTO);
        if (postFavoriteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostFavoriteDTO result = postFavoriteService.save(postFavoriteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postFavoriteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /post-favorites} : get all the postFavorites.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postFavorites in body.
     */
    @GetMapping("/post-favorites")
    public ResponseEntity<List<PostFavoriteDTO>> getAllPostFavorites(PostFavoriteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PostFavorites by criteria: {}", criteria);
        Page<PostFavoriteDTO> page = postFavoriteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /post-favorites/count} : count all the postFavorites.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/post-favorites/count")
    public ResponseEntity<Long> countPostFavorites(PostFavoriteCriteria criteria) {
        log.debug("REST request to count PostFavorites by criteria: {}", criteria);
        return ResponseEntity.ok().body(postFavoriteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /post-favorites/:id} : get the "id" postFavorite.
     *
     * @param id the id of the postFavoriteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFavoriteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/post-favorites/{id}")
    public ResponseEntity<PostFavoriteDTO> getPostFavorite(@PathVariable Long id) {
        log.debug("REST request to get PostFavorite : {}", id);
        Optional<PostFavoriteDTO> postFavoriteDTO = postFavoriteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postFavoriteDTO);
    }

    /**
     * {@code DELETE  /post-favorites/:id} : delete the "id" postFavorite.
     *
     * @param id the id of the postFavoriteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/post-favorites/{id}")
    public ResponseEntity<Void> deletePostFavorite(@PathVariable Long id) {
        log.debug("REST request to delete PostFavorite : {}", id);
        postFavoriteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
