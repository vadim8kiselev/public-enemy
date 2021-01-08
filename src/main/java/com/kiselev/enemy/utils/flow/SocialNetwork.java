package com.kiselev.enemy.utils.flow;

import com.kiselev.enemy.utils.flow.message.Analysis;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.Info;

import java.util.List;

public interface SocialNetwork<Profile extends Info> {

    Profile profile(String identifier);

    List<EnemyMessage<Profile>> track(String identifier);

    Analysis<Profile> analyze(String identifier);
}
