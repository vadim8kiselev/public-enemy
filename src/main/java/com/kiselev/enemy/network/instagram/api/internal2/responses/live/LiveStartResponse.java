package com.kiselev.enemy.network.instagram.api.internal2.responses.live;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class LiveStartResponse extends IGResponse {
    private String media_id;
}
