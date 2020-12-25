package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;

@Data
public class InstagramFriendshipStatus {
    public boolean following;
    public boolean followed_by;
    public boolean incoming_request;
    public boolean outgoing_request;
    public boolean is_private;
    public boolean is_blocking_reel;
    public boolean is_muting_reel;
    public boolean blocking;
    public boolean is_bestie;

}