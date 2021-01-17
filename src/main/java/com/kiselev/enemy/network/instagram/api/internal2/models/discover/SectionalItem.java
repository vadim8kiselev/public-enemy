package com.kiselev.enemy.network.instagram.api.internal2.models.discover;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kiselev.enemy.network.instagram.api.internal2.models.IGBaseModel;
import lombok.Data;


@Data
@JsonTypeInfo(defaultImpl = SectionalItem.class, use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "layout_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SectionalMediaGridItem.class)
})
public class SectionalItem extends IGBaseModel {
    private String layout_type;
    private String feed_type;
}
