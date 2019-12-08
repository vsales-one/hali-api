package com.hali.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.hali.domain.PostingItem} entity.
 */
public class PostingItemDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String imageUrl;

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

    private Instant lastModifiedDate;

    @NotNull
    private String lastModifiedBy;

    private String description;

    @NotNull
    private String pickUpTime;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    @NotNull
    private String pickupAddress;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String city;

    private String district;

    public String getUserProfileDisplayName() {
        return userProfileDisplayName;
    }

    public void setUserProfileDisplayName(String userProfileDisplayName) {
        this.userProfileDisplayName = userProfileDisplayName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    private String userProfileDisplayName;

    private String userProfileImageUrl;


    private Long categoryId;

    private String categoryCategoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCategoryName() {
        return categoryCategoryName;
    }

    public void setCategoryCategoryName(String categoryCategoryName) {
        this.categoryCategoryName = categoryCategoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostingItemDTO postingItemDTO = (PostingItemDTO) o;
        if (postingItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postingItemDTO.getId());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostingItemDTO{" +
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
            ", category=" + getCategoryId() +
            ", category='" + getCategoryCategoryName() + "'" +
            "}";
    }
}
