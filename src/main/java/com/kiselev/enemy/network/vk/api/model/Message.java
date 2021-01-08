package com.kiselev.enemy.network.vk.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {

    @EqualsAndHashCode.Include
    private Integer id;

    private Integer fromId;

    private String text;

    private Integer date;
}
