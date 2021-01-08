package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.FollowerResponse;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Collections;
import java.util.List;

/**
 * Query for Users.getFollowers method
 */
public class FollowersRequest extends ApiRequest<FollowersRequest, FollowerResponse> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public FollowersRequest(String token) {
        super(token, "users.getFollowers", FollowerResponse.class);
    }

    /**
     * User ID.
     *
     * @param value value of "user id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowersRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    /**
     * Offset needed to return a specific subset of followers.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowersRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of followers to return.
     *
     * @param value value of "count" parameter. Maximum is 1000. Minimum is 0. By default 100.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowersRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * fields
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'rate', 'contacts', 'education', 'online'.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowersRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'rate', 'contacts', 'education', 'online'.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowersRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected FollowersRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
