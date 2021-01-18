package com.kiselev.enemy.network.instagram.api.internal2.requests;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

import java.util.concurrent.CompletableFuture;

public interface IGPaginatedRequest<T extends IGPaginatedResponse> {
    void setMax_id(String s);
    
    Class<T> getResponseType();

    CompletableFuture<T> execute(IGClient client);
}
