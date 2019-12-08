package com.hali.repository;

import com.hali.domain.PostFavorite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PostFavorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long>, JpaSpecificationExecutor<PostFavorite> {

}
