package com.kiselev.enemy.network.instagram.api.internal2.responses.live;

import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class LiveCreateResponse extends IGResponse {
    private String broadcast_id;
    private String upload_url;

    public String getBroadcastUrl() {
        return upload_url.split(broadcast_id, 2)[0];
    }

    public String getBroadcastKey() {
        return broadcast_id + upload_url.split(broadcast_id, 2)[1];
    }
}
