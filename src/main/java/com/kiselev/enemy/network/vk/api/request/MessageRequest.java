package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.response.MessageResponse;
import com.vk.api.sdk.objects.enums.MessagesRev;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Collections;
import java.util.List;

/**
 * Query for Messages.getHistory method
 */
public class MessageRequest extends ApiRequest<MessageRequest, MessageResponse> {
    
    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public MessageRequest(String token) {
        super(token, "messages.getHistory", MessageResponse.class);
    }

    /**
     * Offset needed to return a specific subset of messages.
     *
     * @param value value of "offset" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of messages to return.
     *
     * @param value value of "count" parameter. Maximum is 200. Minimum is 0. By default 20.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * ID of the user whose message history you want to return.
     *
     * @param value value of "user id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    /**
     * Set peer id
     *
     * @param value value of "peer id" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest peerId(Integer value) {
        return unsafeParam("peer_id", value);
    }

    /**
     * Starting message ID from which to return history.
     *
     * @param value value of "start message id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest startMessageId(Integer value) {
        return unsafeParam("start_message_id", value);
    }

    /**
     * Sort order: '1' — return messages in chronological order. '0' — return messages in reverse chronological order.
     *
     * @param value value of "rev" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest rev(MessagesRev value) {
        return unsafeParam("rev", value);
    }

    /**
     * Information whether the response should be extended
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * Group ID (for group messages with group access token)
     *
     * @param value value of "group id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest groupId(Integer value) {
        return unsafeParam("group_id", value);
    }

    /**
     * Profile fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public MessageRequest fields(List<Fields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected MessageRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
