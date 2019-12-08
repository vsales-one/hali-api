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

/**
 * Criteria class for the {@link com.hali.domain.UserProfile} entity. This class is used
 * in {@link com.hali.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userId;

    private StringFilter imageUrl;

    private StringFilter city;

    private StringFilter address;

    private StringFilter district;

    private StringFilter phoneNumber;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private StringFilter displayName;

    public UserProfileCriteria(){
    }

    public UserProfileCriteria(UserProfileCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.district = other.district == null ? null : other.district.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.displayName = other.displayName == null ? null : other.displayName.copy();
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getDistrict() {
        return district;
    }

    public void setDistrict(StringFilter district) {
        this.district = district;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public StringFilter getDisplayName() {
        return displayName;
    }

    public void setDisplayName(StringFilter displayName) {
        this.displayName = displayName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(city, that.city) &&
            Objects.equals(address, that.address) &&
            Objects.equals(district, that.district) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userId,
        imageUrl,
        city,
        address,
        district,
        phoneNumber,
        latitude,
        longitude,
        displayName
        );
    }

    @Override
    public String toString() {
        return "UserProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (district != null ? "district=" + district + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (displayName != null ? "displayName=" + displayName + ", " : "") +
            "}";
    }

}
