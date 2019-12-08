package com.hali.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A PostFavorite.
 */
@Entity
@Table(name = "post_favorite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_favorited")
    private Instant dateFavorited;

    @ManyToOne
    @JsonIgnoreProperties("postFavorites")
    private PostingItem postingItem;

    @ManyToOne
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateFavorited() {
        return dateFavorited;
    }

    public PostFavorite dateFavorited(Instant dateFavorited) {
        this.dateFavorited = dateFavorited;
        return this;
    }

    public void setDateFavorited(Instant dateFavorited) {
        this.dateFavorited = dateFavorited;
    }

    public PostingItem getPostingItem() {
        return postingItem;
    }

    public PostFavorite postingItem(PostingItem postingItem) {
        this.postingItem = postingItem;
        return this;
    }

    public void setPostingItem(PostingItem postingItem) {
        this.postingItem = postingItem;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public PostFavorite userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostFavorite)) {
            return false;
        }
        return id != null && id.equals(((PostFavorite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PostFavorite{" +
            "id=" + getId() +
            ", dateFavorited='" + getDateFavorited() + "'" +
            "}";
    }
}
