package com.kiselev.enemy.network.instagram.api.internal2.responses;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

public abstract class IGPaginatedResponse extends IGResponse {
    public abstract String getNext_max_id();

    public abstract boolean isMore_available();
}
