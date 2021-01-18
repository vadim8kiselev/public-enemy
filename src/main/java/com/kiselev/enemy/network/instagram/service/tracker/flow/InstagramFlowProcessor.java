package com.kiselev.enemy.network.instagram.service.tracker.flow;

import com.kiselev.enemy.network.instagram.model.InstagramProfile;
import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.processor.AbstractFlowProcessor;

import java.util.List;

public interface InstagramFlowProcessor extends AbstractFlowProcessor<InstagramProfile> {

    List<EnemyMessage<InstagramProfile>> process(InstagramProfile actual, InstagramProfile latest);
}
