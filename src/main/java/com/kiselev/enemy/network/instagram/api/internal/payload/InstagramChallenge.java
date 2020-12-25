package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;

@Data
public class InstagramChallenge {
    private String url;
    private String api_path;
    private Boolean hide_webview_header;
    private Boolean lock;
    private Boolean logout;
    private Boolean native_flow;
}