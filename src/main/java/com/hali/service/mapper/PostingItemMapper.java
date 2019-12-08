package com.hali.service.mapper;

import com.hali.domain.*;
import com.hali.service.dto.PostingItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostingItem} and its DTO {@link PostingItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface PostingItemMapper extends EntityMapper<PostingItemDTO, PostingItem> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryCategoryName")
    @Mapping(source = "userProfile.displayName", target = "userProfileDisplayName")
    @Mapping(source = "userProfile.imageUrl", target = "userProfileImageUrl")
    PostingItemDTO toDto(PostingItem postingItem);

    @Mapping(source = "categoryId", target = "category")
    PostingItem toEntity(PostingItemDTO postingItemDTO);

    default PostingItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        PostingItem postingItem = new PostingItem();
        postingItem.setId(id);
        return postingItem;
    }
}
