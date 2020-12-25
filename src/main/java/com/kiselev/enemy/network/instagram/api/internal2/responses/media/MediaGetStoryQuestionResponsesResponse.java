package com.kiselev.enemy.network.instagram.api.internal2.responses.media;

import com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.ResponderInfo;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

import lombok.Data;

@Data
public class MediaGetStoryQuestionResponsesResponse extends IGPaginatedResponse {
    private ResponderInfo responder_info;

    @Override
    public String getNext_max_id() {
        return this.responder_info.getMax_id();
    }

    @Override
    public boolean isMore_available() {
        return this.responder_info.isMore_available();
    }
}
