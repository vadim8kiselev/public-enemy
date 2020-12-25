package com.kiselev.enemy.network.instagram.service.tracker.flow;

import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.processor.AbstractFlowProcessor;
import com.kiselev.enemy.network.instagram.model.InstagramProfile;

import java.util.List;

public interface InstagramFlowProcessor extends AbstractFlowProcessor<InstagramProfile> {

    List<Message<InstagramProfile>> process(InstagramProfile actual, InstagramProfile latest);
}
