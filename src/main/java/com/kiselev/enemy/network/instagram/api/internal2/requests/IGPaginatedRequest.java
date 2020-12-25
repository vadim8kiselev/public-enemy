package com.kiselev.enemy.network.instagram.api.internal2.requests;

import java.util.concurrent.CompletableFuture;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

public interface IGPaginatedRequest<T extends IGPaginatedResponse> {
    void setMax_id(String s);
    
    Class<T> getResponseType();

    CompletableFuture<T> execute(IGClient client);
}
