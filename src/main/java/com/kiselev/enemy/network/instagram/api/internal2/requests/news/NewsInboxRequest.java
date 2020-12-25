package com.kiselev.enemy.network.instagram.api.internal2.requests.news;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGGetRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.news.NewsInboxResponse;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NewsInboxRequest extends IGGetRequest<NewsInboxResponse> {
    private Boolean mark_as_seen;

    @Override
    public String path() {
        return "news/inbox/";
    }

    @Override
    public String getQueryString(IGClient client) {
        return mapQueryString("mark_as_seen", mark_as_seen);
    }

    @Override
    public Class<NewsInboxResponse> getResponseType() {
        return NewsInboxResponse.class;
    }

}
