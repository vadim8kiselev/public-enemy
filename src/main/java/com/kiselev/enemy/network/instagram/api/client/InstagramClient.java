package com.kiselev.enemy.network.instagram.api.client;

import com.kiselev.enemy.network.instagram.api.credentials.InstagramCredentials;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstagramClient {

    private final InstagramCredentials credentials;

    private IGClient instagram;

    public IGClient getInstagram() {
        return instagram;
    }

    @SneakyThrows
    public <T extends IGResponse> T request(IGRequest<T> request) {
        authenticate();
        InstagramUtils.randomWait();
        return instagram.sendRequest(request).join();
    }

    private void authenticate() {
        if (instagram == null) {
            instagram = IGClient.authenticate(
                    credentials.getUsername(),
                    credentials.getPassword());
        }
    }
}
