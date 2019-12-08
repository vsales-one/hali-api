package com.hali.service.mapper;

import com.hali.domain.*;
import com.hali.service.dto.PostFavoriteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PostFavorite} and its DTO {@link PostFavoriteDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostingItemMapper.class, UserProfileMapper.class})
public interface PostFavoriteMapper extends EntityMapper<PostFavoriteDTO, PostFavorite> {

    @Mapping(source = "postingItem.id", target = "postingItemId")
    @Mapping(source = "userProfile.id", target = "userProfileId")
    PostFavoriteDTO toDto(PostFavorite postFavorite);

    @Mapping(source = "postingItemId", target = "postingItem")
    @Mapping(source = "userProfileId", target = "userProfile")
    PostFavorite toEntity(PostFavoriteDTO postFavoriteDTO);

    default PostFavorite fromId(Long id) {
        if (id == null) {
            return null;
        }
        PostFavorite postFavorite = new PostFavorite();
        postFavorite.setId(id);
        return postFavorite;
    }
}
