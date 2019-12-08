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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.hali.domain.PostingItem} entity. This class is used
 * in {@link com.hali.web.rest.PostingItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posting-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostingItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter imageUrl;

    private InstantFilter last_modified_date;

    private StringFilter last_modified_by;

    private StringFilter description;

    private StringFilter pickUpTime;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter pickupAddress;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private StringFilter city;

    private StringFilter district;

    private LongFilter categoryId;

    private LongFilter postFavoriteId;

    public PostingItemCriteria(){
    }

    public PostingItemCriteria(PostingItemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.last_modified_date = other.last_modified_date == null ? null : other.last_modified_date.copy();
        this.last_modified_by = other.last_modified_by == null ? null : other.last_modified_by.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.pickUpTime = other.pickUpTime == null ? null : other.pickUpTime.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.pickupAddress = other.pickupAddress == null ? null : other.pickupAddress.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.district = other.district == null ? null : other.district.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.postFavoriteId = other.postFavoriteId == null ? null : other.postFavoriteId.copy();
    }

    @Override
    public PostingItemCriteria copy() {
        return new PostingItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public InstantFilter getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(InstantFilter last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public StringFilter getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(StringFilter last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(StringFilter pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(StringFilter pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getDistrict() {
        return district;
    }

    public void setDistrict(StringFilter district) {
        this.district = district;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getPostFavoriteId() {
        return postFavoriteId;
    }

    public void setPostFavoriteId(LongFilter postFavoriteId) {
        this.postFavoriteId = postFavoriteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostingItemCriteria that = (PostingItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(last_modified_date, that.last_modified_date) &&
            Objects.equals(last_modified_by, that.last_modified_by) &&
            Objects.equals(description, that.description) &&
            Objects.equals(pickUpTime, that.pickUpTime) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(pickupAddress, that.pickupAddress) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(city, that.city) &&
            Objects.equals(district, that.district) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(postFavoriteId, that.postFavoriteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        imageUrl,
        last_modified_date,
        last_modified_by,
        description,
        pickUpTime,
        startDate,
        endDate,
        pickupAddress,
        latitude,
        longitude,
        city,
        district,
        categoryId,
        postFavoriteId
        );
    }

    @Override
    public String toString() {
        return "PostingItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (last_modified_date != null ? "last_modified_date=" + last_modified_date + ", " : "") +
                (last_modified_by != null ? "last_modified_by=" + last_modified_by + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (pickUpTime != null ? "pickUpTime=" + pickUpTime + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (pickupAddress != null ? "pickupAddress=" + pickupAddress + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (district != null ? "district=" + district + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (postFavoriteId != null ? "postFavoriteId=" + postFavoriteId + ", " : "") +
            "}";
    }

}
