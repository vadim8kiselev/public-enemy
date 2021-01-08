package com.kiselev.enemy.network.vk.service.tracker.flow;

import com.kiselev.enemy.utils.flow.message.EnemyMessage;
import com.kiselev.enemy.utils.flow.processor.AbstractFlowProcessor;
import com.kiselev.enemy.network.vk.model.VKProfile;

import java.util.List;

public interface VKFlowProcessor extends AbstractFlowProcessor<VKProfile> {

    List<EnemyMessage<VKProfile>> process(VKProfile actual, VKProfile latest);
}
