package com.kiselev.enemy.network.instagram.api.internal2.requests.highlights;

import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.highlights.HighlightsUserTrayResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HighlightsUserTrayRequest extends IGGetRequest<HighlightsUserTrayResponse> {
    @NonNull
    private Long pk;

    @Override
    public String path() {
        return "highlights/" + pk + "/highlights_tray/";
    }

    @Override
    public Class<HighlightsUserTrayResponse> getResponseType() {
        return HighlightsUserTrayResponse.class;
    }

}
