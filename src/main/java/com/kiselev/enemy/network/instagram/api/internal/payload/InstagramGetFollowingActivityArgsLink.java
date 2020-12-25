package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InstagramGetFollowingActivityArgsLink {

    private Long start;
    private Long end;
    private String type;
    private String id;
}
