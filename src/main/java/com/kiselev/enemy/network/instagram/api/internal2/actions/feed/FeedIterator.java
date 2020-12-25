package com.kiselev.enemy.network.instagram.api.internal2.actions.feed;

import com.kiselev.enemy.network.instagram.api.internal2.IGClient;
import com.kiselev.enemy.network.instagram.api.internal2.actions.feed.CursorIterator;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGPaginatedRequest;
import com.kiselev.enemy.network.instagram.api.internal2.requests.IGRequest;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGPaginatedResponse;

public class FeedIterator<T extends IGRequest<R> & IGPaginatedRequest<R>, R extends IGPaginatedResponse> extends CursorIterator<IGRequest<R>, R> {
    
    public FeedIterator(IGClient client, T t) {
        super(client, t, (o, s) -> ((IGPaginatedRequest<?>) o).setMax_id(s), IGPaginatedResponse::getNext_max_id, IGPaginatedResponse::isMore_available);
    }

}
