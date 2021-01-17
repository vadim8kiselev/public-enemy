package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.direct.DirectInboxResponse;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true, chain = true)
public class DirectInboxRequest extends IGGetRequest<DirectInboxResponse> {
    private String cursor;
    private String visual_message_return_type;
    private Integer thread_message_limit;
    private Integer limit;
    private Boolean persistent_badging;
    private String fetch_reason;

    @Override
    public String path() {
        return "direct_v2/inbox/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("cursor", cursor,
                "visual_message_return_type", visual_message_return_type,
                "thread_message_limit", thread_message_limit,
                "limit", limit,
                "persistent_badging", persistent_badging,
                "fetch_reason", fetch_reason);
    }

    @Override
    public Class<DirectInboxResponse> getResponseType() {
        return DirectInboxResponse.class;
    }

}
