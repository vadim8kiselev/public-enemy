package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.FriendResponse;
import com.vk.api.sdk.objects.enums.FriendsOrder;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Collections;
import java.util.List;

/**
 * Query for Friends.get method
 */
public class FriendRequest extends ApiRequest<FriendRequest, FriendResponse> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public FriendRequest(String token) {
        super(token, "friends.get", FriendResponse.class);
    }

    /**
     * User ID. By default, the current user ID.
     *
     * @param value value of "user id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    /**
     * Sort order: , 'name' — by name (enabled only if the 'fields' parameter is used), 'hints' — by rating, similar to how friends are sorted in My friends section, , This parameter is available only for [vk.com/dev/standalone|desktop applications].
     *
     * @param value value of "order" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest order(FriendsOrder value) {
        return unsafeParam("order", value);
    }

    /**
     * Number of friends to return.
     *
     * @param value value of "count" parameter. Minimum is 0. By default 5000.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * Offset needed to return a specific subset of friends.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * fields
     * Profile fields to return. Sample values: 'uid', 'first_name', 'last_name', 'nickname', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'domain', 'has_mobile', 'rate', 'contacts', 'education'.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return. Sample values: 'uid', 'first_name', 'last_name', 'nickname', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'domain', 'has_mobile', 'rate', 'contacts', 'education'.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public FriendRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected FriendRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}

