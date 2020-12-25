package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class StoryMention extends StoryMetadata {
    /**
     * Collection of ReelMention
     */
    private Collection<ReelMention> reel_mentions;

    private List<Object> map() {
        return reel_mentions.stream().map(m -> m.map()).collect(Collectors.toList());
    }

    @Override
    public String key() {
        return "reel_mentions";
    }

    @Override
    @SneakyThrows
    public String metadata() {
        return new ObjectMapper().writeValueAsString(this.map());
    }

    @Override
    public boolean check() throws IllegalArgumentException {

        return true;
    }

}
