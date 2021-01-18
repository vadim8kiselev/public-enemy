package com.kiselev.enemy.network.instagram.api.internal2;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class IGDevice implements Serializable {
    private static final long serialVersionUID = -823447845648l;
    private final String userAgent;
    private final String capabilities;
    private final Map<String, Object> deviceMap;
}
