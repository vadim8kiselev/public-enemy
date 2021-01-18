package com.kiselev.enemy.network.instagram.api.internal2.responses;

public abstract class IGPaginatedResponse extends IGResponse {
    public abstract String getNext_max_id();

    public abstract boolean isMore_available();
}
