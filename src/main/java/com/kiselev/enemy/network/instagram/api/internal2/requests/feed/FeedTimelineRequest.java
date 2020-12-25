package com.kiselev.enemy.network.instagram.api.internal2.requests.feed;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGPayload;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPostRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.feed.FeedTimelineResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class FeedTimelineRequest extends IGPostRequest<FeedTimelineResponse>
        implements IGPaginatedRequest<FeedTimelineResponse> {
    @Setter
    private String max_id = "";

    @Override
    public IGPayload getPayload(IGClient client) {
        FeedTimelinePayload payload = new FeedTimelinePayload();
        if (!max_id.isEmpty()) {
            payload.setMax_id(max_id);
            payload.setReason("pagination");
        }
        return payload;
    }

    @Override
    public String path() {
        return "feed/timeline/";
    }

    @Override
    public Class<FeedTimelineResponse> getResponseType() {
        return FeedTimelineResponse.class;
    }

    @Data
    @JsonInclude(Include.NON_NULL)
    public static class FeedTimelinePayload extends IGPayload {
        private String max_id;
        private String reason = "cold_start_fetch";
        private String is_pull_to_refresh = "0";
        private String is_prefetch = "0";
    }
}
