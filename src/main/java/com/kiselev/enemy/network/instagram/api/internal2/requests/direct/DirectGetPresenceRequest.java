package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectGetPresenceResponse;

public class DirectGetPresenceRequest extends IGGetRequest<DirectGetPresenceResponse> {

    @Override
    public String path() {
        return "direct_v2/get_presence/";
    }

    @Override
    public Class<DirectGetPresenceResponse> getResponseType() {
        return DirectGetPresenceResponse.class;
    }

}
