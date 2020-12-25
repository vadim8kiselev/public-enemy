package com.kiselev.enemy.network.vk.service.tracker.flow;

import com.kiselev.enemy.utils.flow.message.Message;
import com.kiselev.enemy.utils.flow.processor.AbstractFlowProcessor;
import com.kiselev.enemy.network.vk.model.VKProfile;

import java.util.List;

public interface VKFlowProcessor extends AbstractFlowProcessor<VKProfile> {

    List<Message<VKProfile>> process(VKProfile actual, VKProfile latest);
}
