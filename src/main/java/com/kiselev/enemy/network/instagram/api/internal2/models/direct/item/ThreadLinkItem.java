package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import com.kiselev.enemy.network.instagram.api.internal2.models.media.Link;

import lombok.Data;

@Data
@JsonTypeName("link")
public class ThreadLinkItem extends ThreadItem {
    private Link link;
}
