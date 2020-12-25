package com.kiselev.enemy.network.instagram.api.internal2.requests.archive;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

public class ArchiveReelRequest extends IGGetRequest<IGResponse> {

    @Override
    public String path() {
        return "archive/reel/day_shells/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("include_suggested_highlights", "false",
                "include_cover", "0",
                "is_in_archive_home", "false",
                "timezone_offset", "0");
    }

    @Override
    public Class<IGResponse> getResponseType() {
        return IGResponse.class;
    }

}
