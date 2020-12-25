package com.kiselev.enemy.network.instagram.api.internal2.models.friendships;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Friendship {
    private boolean blocking;
    private boolean followed_by;
    private boolean following;
    private boolean incomng_request;
    @JsonProperty("is_bestie")
    private boolean is_bestie;
    @JsonProperty("is_blocking_reel")
    private boolean is_blocking_reel;
    @JsonProperty("is_muting_reel")
    private boolean is_muting_reel;
    @JsonProperty("is_private")
    private boolean is_private;
    @JsonProperty("is_restricted")
    private boolean is_restricted;
    private boolean muting;
    private boolean outgoing_request;
}
