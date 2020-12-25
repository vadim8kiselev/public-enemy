package com.kiselev.enemy.network.instagram.api.internal.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginResult;
import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramLoginTwoFactorPayload;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

/**
 * Login TwoFactorRequest
 *
 * @author Ozan Karaali
 */
@AllArgsConstructor
@Log4j
public class InstagramLoginTwoFactorRequest extends InstagramPostRequest<InstagramLoginResult> {

    private InstagramLoginTwoFactorPayload payload;

    @Override
    public String getUrl() {
        return "accounts/two_factor_login/";
    }

    @Override
    @SneakyThrows
    public String getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(payload);

        return payloadJson;
    }

    @Override
    @SneakyThrows
    public InstagramLoginResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramLoginResult.class);
    }

    @Override
    public boolean requiresLogin() {
        return false;
    }

}
