package com.kiselev.enemy.network.instagram.api.internal2.responses.direct;

import com.kiselev.enemy.network.instagram.api.internal2.models.direct.IGThread;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

@Data
public class DirectThreadsResponse extends IGResponse {
    private IGThread thread;
}
