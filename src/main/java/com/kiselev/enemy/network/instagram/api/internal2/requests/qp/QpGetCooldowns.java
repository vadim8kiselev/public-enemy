package com.kiselev.enemy.network.instagram.api.internal2.requests.qp;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.api.internal2.utils.IGUtils;

public class QpGetCooldowns extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "qp/get_cooldowns/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("signature", IGUtils.generateSignature("{}"));
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
