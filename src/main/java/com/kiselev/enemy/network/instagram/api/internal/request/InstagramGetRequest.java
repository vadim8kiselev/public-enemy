package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public abstract class InstagramGetRequest<T> extends InstagramRequest<T> {

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public T execute() throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(InstagramConstants.API_URL + getUrl());

        this.applyHeaders(get);

        HttpResponse response = api.executeHttpRequest(get);

        int resultCode = response.getStatusLine().getStatusCode();
        String content = EntityUtils.toString(response.getEntity());

        get.releaseConnection();

        return parseResult(resultCode, content);
    }

    @Override
    public String getPayload() {
        return null;
    }


}
