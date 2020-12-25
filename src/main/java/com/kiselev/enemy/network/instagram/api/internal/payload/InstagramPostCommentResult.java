package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Builder;

import java.util.ArrayList;


@Builder
public class InstagramPostCommentResult extends StatusResult {

    private InstagramComment comment;
    private ArrayList<InstagramUser> users;

}