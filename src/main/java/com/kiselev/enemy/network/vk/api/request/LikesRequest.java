package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.LikeResponse;
import com.vk.api.sdk.objects.enums.LikesFilter;
import com.vk.api.sdk.objects.enums.LikesFriendsOnly;
import com.vk.api.sdk.objects.likes.Type;

import java.util.Arrays;
import java.util.List;

/**
 * Query for Likes.getList method
 */
public class LikesRequest extends ApiRequest<LikesRequest, LikeResponse> {
    
    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     *
     * @param type value of "type" parameter.
     */
    public LikesRequest(String token, Type type) {
        super(token, "likes.getList", LikeResponse.class);
        type(type);
    }

    /**
     * , Object type: 'post' — post on user or community wall, 'comment' — comment on a wall post, 'photo' — photo, 'audio' — audio, 'video' — video, 'note' — note, 'photo_comment' — comment on the photo, 'video_comment' — comment on the video, 'topic_comment' — comment in the discussion, 'sitepage' — page of the site where the [vk.com/dev/Like|Like widget] is installed
     *
     * @param value value of "type" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    protected LikesRequest type(Type value) {
        return unsafeParam("type", value);
    }

    /**
     * ID of the user, community, or application that owns the object. If the 'type' parameter is set as 'sitepage', the application ID is passed as 'owner_id'. Use negative value for a community id. If the 'type' parameter is not set, the 'owner_id' is assumed to be either the current user or the same application ID as if the 'type' parameter was set to 'sitepage'.
     *
     * @param value value of "owner id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest ownerId(String value) {
        return unsafeParam("owner_id", value);
    }

    /**
     * Object ID. If 'type' is set as 'sitepage', 'item_id' can include the 'page_id' parameter value used during initialization of the [vk.com/dev/Like|Like widget].
     *
     * @param value value of "item id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest itemId(String value) {
        return unsafeParam("item_id", value);
    }

    /**
     * URL of the page where the [vk.com/dev/Like|Like widget] is installed. Used instead of the 'item_id' parameter.
     *
     * @param value value of "page url" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest pageUrl(String value) {
        return unsafeParam("page_url", value);
    }

    /**
     * Filters to apply: 'likes' — returns information about all users who liked the object (default), 'copies' — returns information only about users who told their friends about the object
     *
     * @param value value of "filter" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest filter(LikesFilter value) {
        return unsafeParam("filter", value);
    }

    /**
     * Specifies which users are returned: '1' — to return only the current user's friends, '0' — to return all users (default)
     *
     * @param value value of "friends only" parameter. By default 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest friendsOnly(LikesFriendsOnly value) {
        return unsafeParam("friends_only", value);
    }

    /**
     * Specifies whether extended information will be returned. '1' — to return extended information about users and communities from the 'Likes' list, '0' — to return no additional information (default)
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * Offset needed to select a specific subset of users.
     *
     * @param value value of "offset" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of user IDs to return (maximum '1000'). Default is '100' if 'friends_only' is set to '0', otherwise, the default is '10' if 'friends_only' is set to '1'.
     *
     * @param value value of "count" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * Set skip own
     *
     * @param value value of "skip own" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public LikesRequest skipOwn(Boolean value) {
        return unsafeParam("skip_own", value);
    }

    @Override
    protected LikesRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Arrays.asList("type", "access_token");
    }
}
