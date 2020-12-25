package com.kiselev.enemy.utils.flow;

import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.model.Info;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface SocialNetwork<Profile extends Info> {

    Profile profile(String identifier);

    List<Message<Profile>> track(String identifier);

    Analysis<Profile> analyze(String identifier);
}
