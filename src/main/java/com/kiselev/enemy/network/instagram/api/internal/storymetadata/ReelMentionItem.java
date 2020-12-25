package com.kiselev.enemy.network.instagram.api.internal.storymetadata;

import com.kiselev.enemy.network.instagram.api.internal.payload.InstagramUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReelMentionItem extends StoryItem {
    private InstagramUser user;
}
