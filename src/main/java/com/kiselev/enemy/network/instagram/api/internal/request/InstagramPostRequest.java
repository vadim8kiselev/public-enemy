package com.kiselev.enemy.network.instagram.api.internal.request;

import com.kiselev.enemy.network.instagram.api.internal.constants.InstagramConstants;
import com.kiselev.enemy.network.instagram.api.internal.util.InstagramHashUtil;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Log4j
public abstract class InstagramPostRequest<T> extends InstagramRequest<T> {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public T execute() throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(InstagramConstants.API_URL + getUrl());

        this.applyHeaders(post);

        log.debug("User-Agent: " + InstagramConstants.USER_AGENT);

        post.setEntity(getPayloadEntity());

        HttpResponse response = api.executeHttpRequest(post);

        int resultCode = response.getStatusLine().getStatusCode();
        String content = EntityUtils.toString(response.getEntity());

        post.releaseConnection();

        return parseResult(resultCode, content);
    }

    @Override
    public HttpEntity getPayloadEntity() throws UnsupportedEncodingException {
        String payload = getPayload();
        log.debug("Base Payload: " + payload);

        if (isSigned()) {
            payload = InstagramHashUtil.generateSignature(payload);
        }
        log.debug("Final Payload: " + payload);

        return new StringEntity(payload);
    }

}
