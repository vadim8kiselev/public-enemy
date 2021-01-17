package com.kiselev.enemy.network.instagram.api.internal2.requests.direct;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

public class DirectThreadsApproveRequest extends IGPostRequest<IGResponse> {
    private final String[] _thread_ids;

    public DirectThreadsApproveRequest(String... thread_ids) {
        this._thread_ids = thread_ids;
    }

    @Override
    protected IGPayload getPayload(IGClient client) {
        return new DirectThreadsApproveRequestPayload();
    }

    @Override
    public String path() {
        return String.format("direct_v2/threads/%s/",
                _thread_ids.length > 1 ? "approve_multiple" : (_thread_ids[0] + "/approve"));
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

    @Data
    @JsonInclude(Include.NON_NULL)
    public class DirectThreadsApproveRequestPayload extends IGPayload {
        private final String[] thread_ids = _thread_ids.length > 1 ? _thread_ids : null;
    }
}
