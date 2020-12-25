package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetStoryViewersResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * {@link InstagramGetRequest} class to get story viewers of a certain story id
 *
 * @author Daniele Pancottini
 */

@AllArgsConstructor
public class InstagramGetStoryViewersRequest extends InstagramGetRequest<InstagramGetStoryViewersResult> {

    private String storyPk;

    @Override
    public String getUrl() {
        return "media/" + storyPk + "/list_reel_media_viewer/";
    }

    @Override
    @SneakyThrows
    public InstagramGetStoryViewersResult parseResult(int statusCode, String content) {

        return parseJson(statusCode, content, InstagramGetStoryViewersResult.class);

    }

}
