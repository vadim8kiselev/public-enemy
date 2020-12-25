package com.kiselev.enemy.network.instagram.api.internal2.actions.feed;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;
import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CursorIterator<T extends IGRequest<R>, R extends IGResponse> implements Iterator<R> {
    @NonNull
    private IGClient client;
    @NonNull
    private T request;
    @NonNull
    private BiConsumer<T, String> set_cursor;
    @NonNull
    private Function<R, String> get_next_cursor;
    @NonNull
    private Function<R, Boolean> has_next;
    
    protected R response = null;
    
    @Override
    public boolean hasNext() {
        return response == null || (response.getStatusCode() == 200 && has_next.apply(response)); 
    }

    @Override
    public R next() {
        response = client.sendRequest(request).join();
        String next_cursor = get_next_cursor.apply(response);
        set_cursor.accept(request, next_cursor);
        
        return response;
    }

}
