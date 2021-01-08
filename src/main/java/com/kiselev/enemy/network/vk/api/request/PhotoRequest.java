package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.PhotoResponse;

import java.util.Collections;
import java.util.List;

/**
 * Query for Photos.get method
 */
public class PhotoRequest extends ApiRequest<PhotoRequest, PhotoResponse> {

    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public PhotoRequest(String token) {
        super(token, "photos.get", PhotoResponse.class);
    }

    /**
     * ID of the user or community that owns the photos. Use a negative value to designate a community ID.
     *
     * @param value value of "owner id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest ownerId(String value) {
        return unsafeParam("owner_id", value);
    }

    /**
     * Photo album ID. To return information about photos from service albums, use the following string values: 'profile, wall, saved'.
     *
     * @param value value of "album id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest albumId(String value) {
        return unsafeParam("album_id", value);
    }

    /**
     * '1' — to return additional 'likes', 'comments', and 'tags' fields, '0' — (default)
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * '1' — to return photo sizes in a [vk.com/dev/photo_sizes|special format]
     *
     * @param value value of "photo sizes" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest photoSizes(Boolean value) {
        return unsafeParam("photo_sizes", value);
    }

    /**
     * Set offset
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Set count
     *
     * @param value value of "count" parameter. Maximum is 1000. Minimum is 0. By default 50.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public PhotoRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    @Override
    protected PhotoRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
