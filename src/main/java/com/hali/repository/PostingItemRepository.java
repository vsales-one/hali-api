package com.hali.repository;

import com.hali.domain.PostingItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PostingItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostingItemRepository extends JpaRepository<PostingItem, Long>, JpaSpecificationExecutor<PostingItem> {

}
