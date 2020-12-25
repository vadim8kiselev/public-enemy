package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.internal.ApiRequest;
import com.kiselev.enemy.network.vk.api.response.WallResponse;
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.enums.WallFilter;

import java.util.Collections;
import java.util.List;

/**
 * Query for Wall.get method
 */
public class WallRequest extends ApiRequest<WallRequest, WallResponse> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public WallRequest(String token) {
        super(token, "wall.get", WallResponse.class);
    }

    /**
     * ID of the user or community that owns the wall. By default, current user ID. Use a negative value to designate a community ID.
     *
     * @param value value of "owner id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest ownerId(String value) {
        return unsafeParam("owner_id", value);
    }

    /**
     * User or community short address.
     *
     * @param value value of "domain" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest domain(String value) {
        return unsafeParam("domain", value);
    }

    /**
     * Offset needed to return a specific subset of posts.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of posts to return (maximum 100).
     *
     * @param value value of "count" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * Filter to apply: 'owner' — posts by the wall owner, 'others' — posts by someone else, 'all' — posts by the wall owner and others (default), 'postponed' — timed posts (only available for calls with an 'access_token'), 'suggests' — suggested posts on a community wall
     *
     * @param value value of "filter" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest filter(WallFilter value) {
        return unsafeParam("filter", value);
    }

    /**
     * '1' — to return 'wall', 'profiles', and 'groups' fields, '0' — to return no additional fields (default)
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * fields
     * Set fields
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest fields(UserGroupFields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Set fields
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public WallRequest fields(List<UserGroupFields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected WallRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
