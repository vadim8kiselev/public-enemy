package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramGetMediaCommentsResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Get media comments request
 *
 * @author Evgeny Bondarenko (evgbondarenko@gmail.com)
 */
@AllArgsConstructor
public class InstagramGetMediaCommentsRequest extends InstagramGetRequest<InstagramGetMediaCommentsResult> {
    @NonNull
    private String mediaId;
    private String maxId;

    @Override
    public String getUrl() {
        String url = "media/" + mediaId + "/comments/?ig_sig_key_version=" + InstagramConstants.API_KEY_VERSION;
        if (maxId != null && !maxId.isEmpty()) {
            url += "&max_id=" + maxId;
        }
        return url;
    }

    @Override
    public InstagramGetMediaCommentsResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetMediaCommentsResult.class);
    }
}