package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramFeedResult;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@NoArgsConstructor
public class InstagramTimelineFeedRequest extends InstagramPostRequest<InstagramFeedResult> {
    @NonNull
    private String maxId;

    @Override
    public String getUrl() {
        return "feed/timeline/";
    }

    @Override
    @SneakyThrows
    public HttpEntity getPayloadEntity() {
        List<NameValuePair> params = new ArrayList<>();

        if (maxId != null && !maxId.isEmpty()) {
            params.add(new BasicNameValuePair("reason", "pagination"));
            params.add(new BasicNameValuePair("max_id", maxId));
            params.add(new BasicNameValuePair("is_pull_to_refresh", "0"));
        } else {
            params.add(new BasicNameValuePair("reason", "cold_start_fetch"));
            params.add(new BasicNameValuePair("is_pull_to_refresh", "0"));
            params.add(new BasicNameValuePair("seen_posts", ""));
            params.add(new BasicNameValuePair("unseen_posts", ""));
        }
        params.add(new BasicNameValuePair("is_pull_to_refresh", "0"));
        params.add(new BasicNameValuePair("_csrftoken", api.getOrFetchCsrf()));
        params.add(new BasicNameValuePair("_uuid", api.getUuid()));
        params.add(new BasicNameValuePair("is_prefetch", "0"));
        params.add(new BasicNameValuePair("phone_id", api.getDeviceId()));
        params.add(new BasicNameValuePair("device_id", api.getUuid()));
        params.add(new BasicNameValuePair("battery_level", ThreadLocalRandom.current().nextInt(50, 100) + ""));
        params.add(new BasicNameValuePair("is_charging", "0"));
        params.add(new BasicNameValuePair("will_sound_on", "0"));
        params.add(new BasicNameValuePair("timezone_offset", "0"));
        params.add(new BasicNameValuePair("is_async_ads_in_headload_enabled", "0"));
        params.add(new BasicNameValuePair("rti_delivery_backend", "0"));
        params.add(new BasicNameValuePair("is_async_ads_double_request", "0"));
        params.add(new BasicNameValuePair("is_async_ads_rti", "0"));

        return new UrlEncodedFormEntity(params);
    }

    @Override
    public InstagramFeedResult parseResult(int statusCode, String content) {
        return this.parseJson(statusCode, content, InstagramFeedResult.class);
    }

}