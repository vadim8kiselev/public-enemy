package com.kiselev.enemy.utils.flow.processor;

import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.model.Info;

import java.util.List;

public interface AbstractFlowProcessor<Profile extends Info> {

    List<Message<Profile>> process(Profile actual, Profile latest);
}
