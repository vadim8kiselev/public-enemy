package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.SearchResponse;
import com.vk.api.sdk.objects.enums.UsersSex;
import com.vk.api.sdk.objects.enums.UsersSort;
import com.vk.api.sdk.objects.enums.UsersStatus;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Arrays;
import java.util.List;

/**
 * Query for Users.search method
 */
public class SearchRequest extends ApiRequest<SearchRequest, SearchResponse> {
    
    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public SearchRequest(String token) {
        super(token, "users.search", SearchResponse.class);
    }

    /**
     * Search query string (e.g., 'Vasya Babich').
     *
     * @param value value of "q" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest q(String value) {
        return unsafeParam("q", value);
    }

    /**
     * Sort order: '1' — by date registered, '0' — by rating
     *
     * @param value value of "sort" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest sort(UsersSort value) {
        return unsafeParam("sort", value);
    }

    /**
     * Offset needed to return a specific subset of users.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of users to return.
     *
     * @param value value of "count" parameter. Maximum is 1000. Minimum is 0. By default 20.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * City ID.
     *
     * @param value value of "city" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest city(String value) {
        return unsafeParam("city", value);
    }

    /**
     * Country ID.
     *
     * @param value value of "country" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest country(String value) {
        return unsafeParam("country", value);
    }

    /**
     * City name in a string.
     *
     * @param value value of "hometown" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest hometown(String value) {
        return unsafeParam("hometown", value);
    }

    /**
     * ID of the country where the user graduated.
     *
     * @param value value of "university country" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest universityCountry(Integer value) {
        return unsafeParam("university_country", value);
    }

    /**
     * ID of the institution of higher education.
     *
     * @param value value of "university" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest university(Integer value) {
        return unsafeParam("university", value);
    }

    /**
     * Year of graduation from an institution of higher education.
     *
     * @param value value of "university year" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest universityYear(Integer value) {
        return unsafeParam("university_year", value);
    }

    /**
     * Faculty ID.
     *
     * @param value value of "university faculty" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest universityFaculty(Integer value) {
        return unsafeParam("university_faculty", value);
    }

    /**
     * Chair ID.
     *
     * @param value value of "university chair" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest universityChair(Integer value) {
        return unsafeParam("university_chair", value);
    }

    /**
     * '1' — female, '2' — male, '0' — any (default)
     *
     * @param value value of "sex" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest sex(UsersSex value) {
        return unsafeParam("sex", value);
    }

    /**
     * Relationship status: '1' — Not married, '2' — In a relationship, '3' — Engaged, '4' — Married, '5' — It's complicated, '6' — Actively searching, '7' — In love
     *
     * @param value value of "status" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest status(UsersStatus value) {
        return unsafeParam("status", value);
    }

    /**
     * Minimum age.
     *
     * @param value value of "age from" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest ageFrom(Integer value) {
        return unsafeParam("age_from", value);
    }

    /**
     * Maximum age.
     *
     * @param value value of "age to" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest ageTo(Integer value) {
        return unsafeParam("age_to", value);
    }

    /**
     * Day of birth.
     *
     * @param value value of "birth day" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest birthDay(Integer value) {
        return unsafeParam("birth_day", value);
    }

    /**
     * Month of birth.
     *
     * @param value value of "birth month" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest birthMonth(Integer value) {
        return unsafeParam("birth_month", value);
    }

    /**
     * Year of birth.
     *
     * @param value value of "birth year" parameter. Maximum is 2100. Minimum is 1900.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest birthYear(Integer value) {
        return unsafeParam("birth_year", value);
    }

    /**
     * '1' — online only, '0' — all users
     *
     * @param value value of "online" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest online(Boolean value) {
        return unsafeParam("online", value);
    }

    /**
     * '1' — with photo only, '0' — all users
     *
     * @param value value of "has photo" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest hasPhoto(Boolean value) {
        return unsafeParam("has_photo", value);
    }

    /**
     * ID of the country where users finished school.
     *
     * @param value value of "school country" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest schoolCountry(Integer value) {
        return unsafeParam("school_country", value);
    }

    /**
     * ID of the city where users finished school.
     *
     * @param value value of "school city" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest schoolCity(Integer value) {
        return unsafeParam("school_city", value);
    }

    /**
     * Set school class
     *
     * @param value value of "school class" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest schoolClass(Integer value) {
        return unsafeParam("school_class", value);
    }

    /**
     * ID of the school.
     *
     * @param value value of "school" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest school(Integer value) {
        return unsafeParam("school", value);
    }

    /**
     * School graduation year.
     *
     * @param value value of "school year" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest schoolYear(Integer value) {
        return unsafeParam("school_year", value);
    }

    /**
     * Users' religious affiliation.
     *
     * @param value value of "religion" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest religion(String value) {
        return unsafeParam("religion", value);
    }

    /**
     * Users' interests.
     *
     * @param value value of "interests" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest interests(String value) {
        return unsafeParam("interests", value);
    }

    /**
     * Name of the company where users work.
     *
     * @param value value of "company" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest company(String value) {
        return unsafeParam("company", value);
    }

    /**
     * Job position.
     *
     * @param value value of "position" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest position(String value) {
        return unsafeParam("position", value);
    }

    /**
     * ID of a community to search in communities.
     *
     * @param value value of "group id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest groupId(Integer value) {
        return unsafeParam("group_id", value);
    }

    /**
     * from_list
     * Set from list
     *
     * @param value value of "from list" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest fromList(String... value) {
        return unsafeParam("from_list", value);
    }

    /**
     * Set from list
     *
     * @param value value of "from list" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest fromList(List<String> value) {
        return unsafeParam("from_list", value);
    }

    /**
     * fields
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'rate', 'contacts', 'education', 'online',
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'rate', 'contacts', 'education', 'online',
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public SearchRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected SearchRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Arrays.asList("access_token");
    }

    @FunctionalInterface
    public interface Query {

        SearchRequest build(SearchRequest request);
    }
}
