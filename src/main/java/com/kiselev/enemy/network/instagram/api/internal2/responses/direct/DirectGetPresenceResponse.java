package com.kiselev.enemy.network.instagram.api.internal2.responses.direct;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

import java.util.Map;

@Data
public class DirectGetPresenceResponse extends IGResponse {
    private Map<Long, UserPresence> user_presence;

    @Data
    public static class UserPresence {
        @JsonProperty("is_active")
        private boolean is_active;
        private long last_activity_at_ms;
    }
}
