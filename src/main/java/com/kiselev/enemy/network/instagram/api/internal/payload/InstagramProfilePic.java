package com.kiselev.enemy.network.instagram.api.internal.payload;

import lombok.Data;

import java.net.URL;

@Data
public class InstagramProfilePic {

    public URL url;
    public int width;
    public int height;

}