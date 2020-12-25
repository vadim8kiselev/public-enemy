package com.kiselev.enemy.network.instagram.api.internal2.requests.zr;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

public class ZrTokenResultRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "zr/token/result/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("device_id", client.getDeviceId(), "custom_device_id",
                client.getGuid(), "fetch_reason", "token_stale");
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
