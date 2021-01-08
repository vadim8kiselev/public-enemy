package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.PhotoAllResponse;

import java.util.Collections;
import java.util.List;

/**
 * Query for Photos.getAll method
 */
public class PhotoAllRequest extends ApiRequest<PhotoAllRequest, PhotoAllResponse> {
    /**
     * Creates a AbstractQueryBuilder instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public PhotoAllRequest(String token) {
        super(token, "photos.getAll", PhotoAllResponse.class);
    }

    /**
     * ID of a user or community that owns the photos. Use a negative value to designate a community ID.
     *
     * @param value value of "owner id" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest ownerId(String value) {
        return unsafeParam("owner_id", value);
    }

    /**
     * '1' — to return detailed information about photos
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * Offset needed to return a specific subset of photos. By default, '0'.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of photos to return.
     *
     * @param value value of "count" parameter. Maximum is 200. Minimum is 0. By default 20.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * '1' – to return image sizes in [vk.com/dev/photo_sizes|special format].
     *
     * @param value value of "photo sizes" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest photoSizes(Boolean value) {
        return unsafeParam("photo_sizes", value);
    }

    /**
     * '1' – to return photos only from standard albums, '0' – to return all photos including those in service albums, e.g., 'My wall photos' (default)
     *
     * @param value value of "no service albums" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest noServiceAlbums(Boolean value) {
        return unsafeParam("no_service_albums", value);
    }

    /**
     * '1' – to show information about photos being hidden from the block above the wall.
     *
     * @param value value of "need hidden" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest needHidden(Boolean value) {
        return unsafeParam("need_hidden", value);
    }

    /**
     * '1' – not to return photos being hidden from the block above the wall. Works only with owner_id>0, no_service_albums is ignored.
     *
     * @param value value of "skip hidden" parameter.
     * @return a reference to this {@code AbstractQueryBuilder} object to fulfill the "Builder" pattern.
     */
    public PhotoAllRequest skipHidden(Boolean value) {
        return unsafeParam("skip_hidden", value);
    }

    @Override
    protected PhotoAllRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
