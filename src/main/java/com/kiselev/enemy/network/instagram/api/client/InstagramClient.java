package com.kiselev.enemy.network.instagram.api.client;

import com.google.common.collect.Lists;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import com.kiselev.enemy.network.instagram.utils.InstagramUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstagramClient {

    private List<IGClient> clients;

    @Value("${com.kiselev.enemy.instagram.credentials}")
    private List<String> credentials;

    public <T extends IGResponse> T request(IGRequest<T> request) {
        authenticate();
//        reanimate();
        IGClient client = client(clients);
        InstagramUtils.timeout();

        try {
            return client.sendRequest(request).join();
        } catch (Exception exception) {
            client.markUnavailable();
            return request(request);
        }
    }

    private void authenticate() {
        if (clients == null) {
            clients = credentials.stream()
                    .map(credential -> {
                        String[] usernameAndPassword = credential.split(":");

                        String username = usernameAndPassword[0];
                        String password = usernameAndPassword[1];

                        return IGClient.authenticate(username, password);
                    })
                    .collect(Collectors.toList());
        }
    }

    private IGClient client(List<IGClient> clients) {
        return clients.stream()
                .filter(IGClient::isAvailable)
                .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2))
                .findAny()
                .orElseGet(this::lastChance);
    }

    private void reanimate() {
        for (IGClient client : clients) {
            if (client.isNotAvailable()) {
                try {
                    InstagramUtils.timeout();
                    client.actions().account().currentUser().join();
                    client.markAvailable();
                } catch (Exception exception) {
                    // Skip
                    boolean debug = true;
                }
            }
        }
    }

    private IGClient lastChance() {
        InstagramUtils.sleep(60000);

        List<IGClient> availableClients = Lists.newArrayList();

        for (IGClient client : clients) {
            try {
                client.actions().account().currentUser().join();
                client.markAvailable();
                availableClients.add(client);
            } catch (Exception exception) {
                // Skip
            }
        }

        return availableClients.stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No clients available"));
    }
}
