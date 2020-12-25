package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetReelsTrayFeedResult;
import lombok.SneakyThrows;

/**
 * Get Story Request
 *
 * @author Ozan Karaali
 */
public class InstagramGetReelsTrayFeedRequest extends InstagramGetRequest<InstagramGetReelsTrayFeedResult> {
    @Override
    public String getPayload() {
        return null;
    }

    @Override
    public String getUrl() {
        return "feed/reels_tray/?";
    }

    @Override
    @SneakyThrows
    public InstagramGetReelsTrayFeedResult parseResult(int resultCode, String content) {
        return parseJson(resultCode, content, InstagramGetReelsTrayFeedResult.class);
    }
}
