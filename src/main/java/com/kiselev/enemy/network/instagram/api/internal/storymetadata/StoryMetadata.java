package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

public abstract class StoryMetadata {

    public abstract String key();

    public abstract String metadata();

    public abstract boolean check() throws IllegalArgumentException;
}
