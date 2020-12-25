package com.kiselev.enemy.network.instagram.api.internal2.models.direct.item;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.kiselev.enemy.network.instagram.api.internal2.models.direct.item.ThreadItem;
import lombok.Data;

@Data
@JsonTypeName("text")
public class ThreadTextItem extends ThreadItem {
    private String text;
}
