package com.kiselev.enemy.network.instagram.api.internal2.responses.creatives;

import java.util.List;

import com.kiselev.enemy.network.instagram.api.internal2.models.creatives.StaticSticker;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;

import lombok.Data;

@Data
public class CreativesAssetsResponse extends IGResponse {
    private List<StaticSticker> static_stickers;
}