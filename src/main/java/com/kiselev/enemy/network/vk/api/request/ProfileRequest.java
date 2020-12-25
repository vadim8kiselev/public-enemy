package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.internal.ApiRequest;
import com.kiselev.enemy.network.vk.api.model.Profile;
import com.vk.api.sdk.client.Utils;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Collections;
import java.util.List;

/**
 * Query for Users.get method
 */
public class ProfileRequest extends ApiRequest<ProfileRequest, List<Profile>> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public ProfileRequest(String token) {
        super(token, "users.get", Utils.buildParametrizedType(List.class, Profile.class));
    }

    /**
     * user_id
     *
     * @param value value of "user id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ProfileRequest userId(String value) {
        return unsafeParam("user_ids", value);
    }

    /**
     * user_ids
     * User IDs or screen names ('screen_name'). By default, current user ID.
     *
     * @param value value of "user ids" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ProfileRequest userIds(String... value) {
        return unsafeParam("user_ids", value);
    }

    /**
     * User IDs or screen names ('screen_name'). By default, current user ID.
     *
     * @param value value of "user ids" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ProfileRequest userIds(List<String> value) {
        return unsafeParam("user_ids", value);
    }

    /**
     * fields
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'contacts', 'education', 'online', 'counters', 'relation', 'last_seen', 'activity', 'can_write_private_message', 'can_see_all_posts', 'can_post', 'universities',
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ProfileRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return. Sample values: 'nickname', 'screen_name', 'sex', 'bdate' (birthdate), 'city', 'country', 'timezone', 'photo', 'photo_medium', 'photo_big', 'has_mobile', 'contacts', 'education', 'online', 'counters', 'relation', 'last_seen', 'activity', 'can_write_private_message', 'can_see_all_posts', 'can_post', 'universities',
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ProfileRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected ProfileRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
