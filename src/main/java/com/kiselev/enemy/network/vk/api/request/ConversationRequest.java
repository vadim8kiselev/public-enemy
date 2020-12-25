package com.kiselev.enemy.network.vk.api.request;

import com.kiselev.enemy.network.vk.api.internal.ApiRequest;
import com.kiselev.enemy.network.vk.api.response.ConversationsResponse;
import com.vk.api.sdk.objects.base.UserGroupFields;
import com.vk.api.sdk.objects.enums.MessagesFilter;

import java.util.Collections;
import java.util.List;

/**
 * Query for Messages.getConversations method
 */
public class ConversationRequest extends ApiRequest<ConversationRequest, ConversationsResponse> {
    
    /**
     * Creates a ApiRequest instance that can be used to build api request with various parameters
     *
     * @param token access token
     */
    public ConversationRequest(String token) {
        super(token, "messages.getConversations", ConversationsResponse.class);
    }

    /**
     * Offset needed to return a specific subset of conversations.
     *
     * @param value value of "offset" parameter. Minimum is 0. By default 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    /**
     * Number of conversations to return.
     *
     * @param value value of "count" parameter. Maximum is 200. Minimum is 0. By default 20.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    /**
     * Filter to apply: 'all' — all conversations, 'unread' — conversations with unread messages, 'important' — conversations, marked as important (only for community messages), 'unanswered' — conversations, marked as unanswered (only for community messages)
     *
     * @param value value of "filter" parameter. By default all.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest filter(MessagesFilter value) {
        return unsafeParam("filter", value);
    }

    /**
     * '1' — return extra information about users and communities
     *
     * @param value value of "extended" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest extended(Boolean value) {
        return unsafeParam("extended", value);
    }

    /**
     * ID of the message from what to return dialogs.
     *
     * @param value value of "start message id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest startMessageId(Integer value) {
        return unsafeParam("start_message_id", value);
    }

    /**
     * Group ID (for group messages with group access token)
     *
     * @param value value of "group id" parameter. Minimum is 0.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest groupId(Integer value) {
        return unsafeParam("group_id", value);
    }

    /**
     * fields
     * Profile and communities fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest fields(UserGroupFields... value) {
        return unsafeParam("fields", value);
    }

    /**
     * Profile and communities fields to return.
     *
     * @param value value of "fields" parameter.
     * @return a reference to this {@code ApiRequest} object to fulfill the "Builder" pattern.
     */
    public ConversationRequest fields(List<UserGroupFields> value) {
        return unsafeParam("fields", value);
    }

    @Override
    protected ConversationRequest getThis() {
        return this;
    }

    @Override
    protected List<String> essentialKeys() {
        return Collections.singletonList("access_token");
    }
}
