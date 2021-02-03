package com.kiselev.enemy.utils.flow;

import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.model.Info;

public interface SocialNetwork<Profile extends Info> {

    Profile me();

    Profile profile(String identifier);

    EnemyMessage<Profile> info(String identifier);

    EnemyMessage<Profile> version(String identifier);
}
