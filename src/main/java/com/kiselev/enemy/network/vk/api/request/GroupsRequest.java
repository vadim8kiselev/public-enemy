package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.constants.VKConstants;
import com.kiselev.enemy.network.vk.api.internal.ApiRequest;
import com.kiselev.enemy.network.vk.api.response.GroupResponse;
import com.vk.api.sdk.objects.groups.Fields;
import com.vk.api.sdk.objects.groups.Filter;

import java.util.Collections;
import java.util.List;

/**
 * Query for Groups.get method
 */
public class GroupsRequest extends ApiRequest<GroupsRequest, GroupResponse> {
    
    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public GroupsRequest(String token) {
        super(token, "groups.get", GroupResponse.class);
    }

    /**
     * User ID.
     *
     * @param value value of "user id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    /**
     * '1' — to return complete information about a user's communities, '0' — to return a list of community IDs without any additional fields (default),
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * Offset needed to return a specific subset of communities.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of communities to return.
     *
     * @param value value of "count" parameter. Maximum is 1000. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * filter
     * Types of communities to return: 'admin' — to return communities administered by the user , 'editor' — to return communities where the user is an administrator or editor, 'moder' — to return communities where the user is an administrator, editor, or moderator, 'groups' — to return only groups, 'publics' — to return only public pages, 'events' — to return only events
     *
     * @param value value of "filter" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest filter(Filter... value) {
        return unsafeParam("filter", value);
    }

    /**
     * Types of communities to return: 'admin' — to return communities administered by the user , 'editor' — to return communities where the user is an administrator or editor, 'moder' — to return communities where the user is an administrator, editor, or moderator, 'groups' — to return only groups, 'publics' — to return only public pages, 'events' — to return only events
     *
     * @param value value of "filter" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest filter(List<Filter> value) {
        return unsafeParam("filter", value);
    }

    /**
     * fields
     * Profile fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public GroupsRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected GroupsRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
