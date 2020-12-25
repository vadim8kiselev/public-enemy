package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

public class DirectCreateGroupThreadRequest extends IGPostRequest<IGResponse> {
    private String title;
    private String[] userIds;

    public DirectCreateGroupThreadRequest(String title, String... user_ids) {
        this.title = title;
        this.userIds = user_ids;
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new DirectCreateGroupThreadPayload();
    }

    @Override
    public String path() {
        return "direct_v2/create_group_thread/";
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    public class DirectCreateGroupThreadPayload extends IGPayload {
        private String[] recipient_users = userIds;
        private String thread_title = title;
    }

}
