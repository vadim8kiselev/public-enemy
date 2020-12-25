package com.kiselev.enemy.network.instagram.api.internal2.actions.status;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.status.StatusSetStatusRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import java.util.concurrent.CompletableFuture;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StatusAction {
    @NonNull
    private IGClient client;

    /**
     * There is a daily limit to the use of setStatus.
     *
     * @param text The thread app limit the String to a length to 30 characters, but there is no server verification
     * @param emoji The thread app prevent you to put anything else that an emoji, but there is no server verification
     * @param expires_at Must be in second (Example System.currentTimeMillis()/1000+60 for 60 seconds)
     * @param should_notify Close friend should be notify or not
     * @param status_type Must be "auto" or "manual"
     * @return IGResponse
     */
    public CompletableFuture<IGResponse> setStatus(String text, String emoji, long expires_at, boolean should_notify, String status_type) {
        return new StatusSetStatusRequest(text,emoji,expires_at,should_notify,status_type).execute(client);
    }

    /**
     * @param text The thread app limit the String to a length to 30 characters, but there is no server verification
     * @param emoji The thread app prevent you to put anything else that an emoji, but there is no server verification
     * @param expires_at Must be in second (Example System.currentTimeMillis()/1000+60 for 60 seconds)
     * @return IGResponse
     */
    public CompletableFuture<IGResponse> setStatus(String text, String emoji, long expires_at) {
        return new StatusSetStatusRequest(text,emoji,expires_at).execute(client);
    }
}
