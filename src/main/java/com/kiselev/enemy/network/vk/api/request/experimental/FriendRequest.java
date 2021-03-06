package com.kiselev.enemy.network.vk.api.request.experimental;

import com.kiselev.enemy.network.vk.api.request.ApiRequest;
import com.kiselev.enemy.network.vk.api.response.FriendResponse;
import com.vk.api.sdk.objects.enums.FriendsOrder;
import com.vk.api.sdk.objects.users.Fields;

import java.util.Collections;
import java.util.List;

public class FriendRequest extends ApiRequest<FriendRequest, FriendResponse> {





    public FriendRequest(String token) {
        super(token, "friends.get", FriendResponse.class);
    }


    // count

    // all

    // page


    public FriendRequest userId(String value) {
        return unsafeParam("user_id", value);
    }

    public FriendRequest order(FriendsOrder value) {
        return unsafeParam("order", value);
    }

    public FriendRequest count(Integer value) {
        return unsafeParam("count", value);
    }

    public FriendRequest offset(Integer value) {
        return unsafeParam("offset", value);
    }

    public FriendRequest fields(Fields... value) {
        return unsafeParam("fields", value);
    }

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

