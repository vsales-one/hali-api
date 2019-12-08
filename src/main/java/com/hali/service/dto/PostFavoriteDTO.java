package com.hali.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.hali.domain.PostFavorite} entity.
 */
public class PostFavoriteDTO implements Serializable {

    private Long id;

    private Instant dateFavorited;


    private Long postingItemId;

    private Long userProfileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFavorited() {
        return dateFavorited;
    }

    public void setDateFavorited(Instant dateFavorited) {
        this.dateFavorited = dateFavorited;
    }

    public Long getPostingItemId() {
        return postingItemId;
    }

    public void setPostingItemId(Long postingItemId) {
        this.postingItemId = postingItemId;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostFavoriteDTO postFavoriteDTO = (PostFavoriteDTO) o;
        if (postFavoriteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postFavoriteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostFavoriteDTO{" +
            "id=" + getId() +
            ", dateFavorited='" + getDateFavorited() + "'" +
            ", postingItem=" + getPostingItemId() +
            ", userProfile=" + getUserProfileId() +
            "}";
    }
}
