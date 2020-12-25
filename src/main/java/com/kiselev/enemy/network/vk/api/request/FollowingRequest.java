package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.internal.ApiRequest;
import com.kiselev.enemy.network.vk.api.response.FollowingResponse;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Query for Users.getSubscriptions method
 */
public class FollowingRequest extends ApiRequest<FollowingRequest, FollowingResponse> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public FollowingRequest(String token) {
        super(token, "users.getSubscriptions", FollowingResponse.class);
    }

    /**
     * User ID.
     *
     * @param value value of "user id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    /**
     * '1' — to return a combined list of users and communities, '0' — to return separate lists of users and communities (default)
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * Offset needed to return a specific subset of subscriptions.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of users and communities to return.
     *
     * @param value value of "count" parameter. Maximum is 200. Minimum is 0. By default 20.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * fields
     * Set fields
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Set fields
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FollowingRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected FollowingRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}

