package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramSearchLocationsResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * Search Locations Request
 *
 * @author Yumaev
 */
@RequiredArgsConstructor
public class InstagramSearchLocationsRequest extends InstagramGetRequest<InstagramSearchLocationsResult> {

    @NonNull
    private final String latitude;
    @NonNull
    private final String longitude;
    @NonNull
    private final String query;
    private final String timestamp = String.valueOf(System.currentTimeMillis());

    @Override
    public String getUrl() {
        return "location_search/?ig_sig_key_version=" + InstagramConstants.API_KEY_VERSION
                + "&search_query=" + query
                + "&rank_token=" + api.getRankToken()
                + "&latitude=" + latitude
                + "&longitude=" + longitude
                + "&timestamp=" + timestamp;
    }

    @Override
    @SneakyThrows
    public InstagramSearchLocationsResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramSearchLocationsResult.class);
    }

}
