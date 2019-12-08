package com.hali.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.hali.domain.PostFavorite} entity. This class is used
 * in {@link com.hali.web.rest.PostFavoriteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /post-favorites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostFavoriteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateFavorited;

    private LongFilter postingItemId;

    private LongFilter userProfileId;

    public PostFavoriteCriteria(){
    }

    public PostFavoriteCriteria(PostFavoriteCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.dateFavorited = other.dateFavorited == null ? null : other.dateFavorited.copy();
        this.postingItemId = other.postingItemId == null ? null : other.postingItemId.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
    }

    @Override
    public PostFavoriteCriteria copy() {
        return new PostFavoriteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateFavorited() {
        return dateFavorited;
    }

    public void setDateFavorited(InstantFilter dateFavorited) {
        this.dateFavorited = dateFavorited;
    }

    public LongFilter getPostingItemId() {
        return postingItemId;
    }

    public void setPostingItemId(LongFilter postingItemId) {
        this.postingItemId = postingItemId;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
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
        final PostFavoriteCriteria that = (PostFavoriteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateFavorited, that.dateFavorited) &&
            Objects.equals(postingItemId, that.postingItemId) &&
            Objects.equals(userProfileId, that.userProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateFavorited,
        postingItemId,
        userProfileId
        );
    }

    @Override
    public String toString() {
        return "PostFavoriteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateFavorited != null ? "dateFavorited=" + dateFavorited + ", " : "") +
                (postingItemId != null ? "postingItemId=" + postingItemId + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
            "}";
    }

}
