package com.hali.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A PostingItem.
 */
@Entity
@Table(name = "posting_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostingItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    private Instant lastModifiedDate;

    @Column(name = "status")
    private String status;


    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "pick_up_time", nullable = false)
    private String pickUpTime;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @NotNull
    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "latitude", precision = 21, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("postingItems")
    private Category category;

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name="last_modified_by", nullable=false,referencedColumnName = "user_id", insertable =  false, updatable = false)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public PostingItem title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PostingItem imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getDescription() {
        return description;
    }

    public PostingItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public PostingItem pickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
        return this;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public PostingItem startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public PostingItem endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public PostingItem pickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
        return this;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public PostingItem latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public PostingItem longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public PostingItem city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public PostingItem district(String district) {
        this.district = district;
        return this;
    }

    public PostingItem lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public PostingItem lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Category getCategory() {
        return category;
    }

    public PostingItem category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostingItem)) {
            return false;
        }
        return id != null && id.equals(((PostingItem) o).id);
    }

    public String getStatus() {
        return status;
    }

    public PostingItem status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PostingItem{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", pickUpTime='" + getPickUpTime() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", pickupAddress='" + getPickupAddress() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", city='" + getCity() + "'" +
            ", district='" + getDistrict() + "'" +
            "}";
    }
}
