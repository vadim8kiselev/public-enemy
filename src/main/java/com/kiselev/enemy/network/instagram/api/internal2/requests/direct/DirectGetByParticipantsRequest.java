package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectThreadsResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;

public class DirectGetByParticipantsRequest extends IGGetRequest<DirectThreadsResponse> {
    private Long[] _participants;

    public DirectGetByParticipantsRequest(Long... participants) {
        this._participants = participants;
    }

    @Override
    public String path() {
        return "direct_v2/threads/get_by_participants/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("recipient_users", IGUtils.objectToJson(_participants));
    }

    @Override
    public Class<DirectThreadsResponse> getResponseType() {
        return DirectThreadsResponse.class;
    }

}
