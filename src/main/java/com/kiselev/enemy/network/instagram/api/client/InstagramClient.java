package com.kiselev.enemy.network.instagram.api.client;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstagramClient {

    @Value("${com.kiselev.enemy.instagram.credentials}")
    private List<String> credentials;

    private Queue<IGClient> clients;

    private Queue<IGClient> unavailableClients;

    public <T extends IGResponse> T request(IGRequest<T> request) {
        authenticate();
        IGClient client = client();
        InstagramUtils.timeout();

        try {
            return client.sendRequest(request).join();
        } catch (Exception exception) {
            if ("Not authorized to view user".equals(exception.getCause().getMessage())) {
                // Private profile
                return null;
            }
            unavailable(client);
            return request(request);
        }
    }

    private void authenticate() {
        if (this.clients == null) {
            this.clients = credentials.stream()
                    .map(credential -> {
                        try {
                            String[] usernameAndPassword = credential.split(":");

                            String username = usernameAndPassword[0];
                            String password = usernameAndPassword[1];

                            return IGClient.authenticate(username, password);
                        } catch (Exception exception) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(Lists::newLinkedList));
        }
        if (this.unavailableClients == null) {
            this.unavailableClients = Lists.newLinkedList();
        }
    }

    private IGClient client() {
        IGClient client = clients.poll();
        if (client != null) {
            clients.add(client);
            return client;
        } else {
            reanimate();

            IGClient reanimatedClient = clients.poll();
            if (reanimatedClient != null) {
                return reanimatedClient;
            } else {
                throw new RuntimeException("All clients are currently unavailable");
            }
        }
    }

    private void unavailable(IGClient client) {
        client.markUnavailable();
        clients.remove(client);
        unavailableClients.add(client);
    }

    private void reanimate() {
        for (IGClient client : unavailableClients) {
            if (client.isNotAvailable()) {
                try {
                    InstagramUtils.timeout();
                    client.actions().account().currentUser().join();
                    client.markAvailable();
                } catch (Exception exception) {
                    log.error(exception.getMessage(), exception);
                }
            }
        }
    }
}
