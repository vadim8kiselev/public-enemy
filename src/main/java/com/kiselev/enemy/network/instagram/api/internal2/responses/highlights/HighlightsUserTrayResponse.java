package com.kiselev.enemy.network.instagram.api.internal2.responses.highlights;

import com.kiselev.enemy.network.instagram.api.internal2.models.highlights.Highlight;
import com.kiselev.enemy.network.instagram.api.internal2.models.igtv.Channel;
import com.kiselev.enemy.network.instagram.api.internal2.responses.IGResponse;
import lombok.Data;

import java.util.List;

@Data
public class HighlightsUserTrayResponse extends IGResponse {
    private List<Highlight> tray;
    private Channel tv_channel;
}
