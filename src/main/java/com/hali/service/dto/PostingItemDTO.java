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

    private Instant last_modified_date;

    private String last_modified_by;

    private String description;

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

    public Instant getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(Instant last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
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
            ", last_modified_date='" + getLast_modified_date() + "'" +
            ", last_modified_by='" + getLast_modified_by() + "'" +
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
