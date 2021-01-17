package com.kiselev.enemy.network.instagram.api.internal2.models.media.reel.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@Data
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class StoryPollsItem extends ReelMetadataItem {
    @NonNull
    private String question;
    @Builder.Default
    @NonNull
    private List<Tally> tallies = Arrays.asList(YES, NO);
    public static final Tally YES = Tally.builder().text("YES").build(),
            NO = Tally.builder().text("NO").build();

    @Override
    public String key() {
        return "story_polls";
    }

    @Data
    @Builder
    public static class Tally {
        @NonNull
        private String text;
        @Builder.Default
        private int count = 0;
        @Builder.Default
        private double font_size = 35;
    }

}
